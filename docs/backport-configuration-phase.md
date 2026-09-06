# PortLib：在 Forge 1.20.1 上补全“配置阶段（CONFIGURATION）”（方案 C）

> 范围：本仓库（`MesdagPortLib+neoforge1.21.1 to forge1.20.1`，ModDevGradle `legacyforge` 2.0.141 /
> Forge 47.4.20 / MC 1.20.1）。
> 状态：方案 C 已实现并通过运行验证（含断线加固、注册表兜底）；增强项（按连接任务事件 /
> LOGIN 大包分片 / 客户端看门狗）已落地，见文末 TODO。

---

## 1. 背景与目标

- **1.21.1 语义**：`handshake → login → configuration → play`。CONFIGURATION 阶段里，服务端按
  `ConfigurationTask` 队列逐任务发送数据（注册表/协商类 payload），每任务等客户端回执，
  队列末尾 `JoinWorldTask` 发出“配置结束”，客户端回执后服务端才 `placeNewPlayer`。
- **1.20.1 现实**（已代码核实，见 `_mc120src`，与 47.4.20 一致）：
    - `ConnectionProtocol` 只有 `HANDSHAKING/PLAY/STATUS/LOGIN`，**没有 CONFIGURATION、没有
      LoginAcknowledgedPacket**（`net/minecraft/network/ConnectionProtocol.java` L205-208）；
    - 服务端 `ServerLoginPacketListenerImpl.handleAcceptedLogin()` 发完
      `ClientboundGameProfilePacket`
      后**无任何往返**、同一调用内 `placeNewPlayer` 进世界（L117 → L126）；玩家进世界唯一收口是
      私有 `placeNewPlayer(ServerPlayer)`（L138-140，委托 `PlayerList.placeNewPlayer`）；
    - 注册表同步内嵌在 PLAY 首包 `ClientboundLoginPacket.registryHolder`
      （`RegistrySynchronization.NETWORK_CODEC`，`PlayerList` L182），**1.20.1 没有
      `ClientboundRegistryDataPacket`**，所以这部分无法搬到“配置窗口”，保持原版；
    - 客户端 `ClientHandshakePacketListenerImpl.handleGameProfile`（L128-134）收到 profile 即
      `setProtocol(PLAY)` + `new ClientPacketListener`，登录期没有频道回执，只有 vanilla
      custom query 的 transactionId 回执；
    - Forge 把登录期 mod 消息封装进 **`fml:loginwrapper` 信封**（内层 `[目标频道][长度][载荷]`）
      （`net/minecraftforge/network/LoginWrapper.java`），收包侧拆封后按内层频道分发——这是
      1.20.1 登录阶段唯一可靠的双向频道通道；
    - Forge 的“类配置窗口”= vanilla 状态机里的 `NEGOTIATING` 态 + `HandshakeHandler` 逐 tick 发
      payload、按 index 等回执，发生在**认证之后、发 GameProfile 之前**；且握手结束时双方已在
      channel attr `FML_CONNECTION_DATA`（`ConnectionData`）里交换了**频道清单**。

**为什么需要“补全”**：PortLib 1.20.1 现有实现把本应在进服前完成的协商放到 join 之后
（datamap Known/Reply 挂在 `OnDatapackSyncEvent` 上，而内容同步又依赖 Reply 写入的频道属性），
导致**新玩家首次加入必然跳过 datamap 内容同步**（只有 `/reload` 会补发）。方案 C 在
“发完 profile 之后、放行进世界之前”开一段真正可跨 tick 等待回执的配置窗口，与 1.21.1 位置一致。

**不做的事**：不新增 `ConnectionProtocol` 状态、不改任何 wire 包 ID/结构、不要求 vanilla 或
未装 PortLib 的 Forge 端配合。

---

## 2. 方案 C 架构

```
服务端                                           客户端
────────────────────────────────────────────     ─────────────────────────────────────────
[Forge NEGOTIATING 握手完成]
READY_TO_ACCEPT
handleAcceptedLogin:
  · ACCEPTED / 压缩协商
  · send ClientboundGameProfilePacket ─────────► ClientHandshakePacketListenerImpl
                                                  handleGameProfile【Mixin 拦截】
  · getPlayerForLogin（建 ServerPlayer）             · 若对端是 PortLib：推迟原版收尾
  · 调私有 placeNewPlayer ─────────────────────────    （不 setProtocol(PLAY)、不建 ClientPacketListener）
      【Mixin 拦截】                                  · 存 continuation 到连接 attr
      创建 PortConfigurationManager.ServerStage       · 返回（继续处于 LOGIN 监听器）
      并 cancel 原调用                                · 照常处理 LOGIN custom query
                │ 下一个服务端 tick 起
                ▼
ServerStage.tick（每服务端 tick，通用任务队列）
  · 当前任务 = IPortCustomConfigurationTask
  · 任务：datamap Known 协商（fml:loginwrapper 信封 ─► 客户端 PortKnownRegistryDataMapsPayload.handle
    按 portlib:main 分发 Known payload）               校验 mandatory → context.reply(Reply)
  · 等任务完成请求（网络线程入队）           ◄────── 客户端回 PortKnownRegistryDataMapsReplyPayload
  · Reply 处理函数先把 known 清单写入                    （服务端侧 handle 写 channel attr，供内容同步用）
    连接 attr，再 finishCurrentTask(KNOWN_TASK_TYPE)
  · 队列空 → finish()：send configuration_finished ─► 客户端 FinishedPayload.handle
                                                        → PortConfigurationManager.clientFinish
                │                                        执行被推迟的原版收尾：
                ▼                                        setProtocol(PLAY)+handleClientLoginSuccess
  恢复原版私有 placeNewPlayer（@Invoker）                    +new ClientPacketListener
  → PlayerList.placeNewPlayer → ClientboundLoginPacket ─► 正常进入 PLAY，注册表在 LoginPacket 内
    （PLAY 首包，含注册表）                                 随后 join 期 OnDatapackSyncEvent 内容同步
    此刻 attr 已协商好 → 首次加入 datamap 内容可下发 ✅
```

### 2.1 传输层（为什么是 fml:loginwrapper 信封）

1.20.1 的登录自定义查询只有 `ClientboundCustomQueryPacket(transactionId, channel, data)` /
`ServerboundCustomQueryPacket(transactionId, data)`；**回复包不带频道名**，Forge 用
`fml:loginwrapper` 外层信封承载“真实频道”来保证双向可路由（`LoginWrapper.java`）。
`PortNetworkHandler.sendLoginToClient(...)` 复刻同一格式：
`外信封 = [portlib:main][VarInt 长度][SimpleChannel 编码载荷]`，外层 channel = `fml:loginwrapper`，
transactionId 用阶段内自增 `seq`。收包侧由 Forge `LoginWrapper` 拆封 → `NetworkRegistry.findTarget`
→ `portlib:main` 的 SimpleChannel → indexed codec 反序列化 → 我们的登录期 handler。
因此：**不需要把任何消息塞进 Forge 握手 payload 列表**（那会对“无 PortLib 的 Forge 客户端”
产生未知频道回执问题），全部由 PortLib 自己的阶段窗口按需发送。

### 2.2 能力判定（零探测包）

是否进入配置阶段 = `isPortLibPeer(connection)`：

- 非内存连接，且 `NetworkHooks.getConnectionData(connection).getChannels()` 含 `portlib:main`
  （Forge 握手结束时双方已交换频道清单，服务端见客户端频道、客户端见服务端频道，同一判定两端一致）。
- vanilla 客户端 / Forge 无 PortLib → false → 两端都走原版路径，行为不变；
- 单机 / 局域网本机（内存连接）→ false（同一进程无需网络同步）。

### 2.3 消息清单（全部 LOGIN 方向，`portlib:main`）

| 类                                       | 方向  | 内容                                         | 触发            |
|-----------------------------------------|-----|--------------------------------------------|---------------|
| `PortKnownRegistryDataMapsPayload`      | S2C | 服务器已知 data map 清单（registry → KnownDataMap） | 阶段任务 1        |
| `PortKnownRegistryDataMapsReplyPayload` | C2S | 客户端已知清单（写入服务端连接 attr）                      | 客户端处理 Known 后 |
| `PortConfigurationFragmentPayload`      | S2C | 大负载分片（首/尾标记 + 数据块 + 目标类型）                  | 单包超限时替代单条消息   |
| `PortConfigurationFinishedPayload`      | S2C | 无负载“配置结束”标记                                | 阶段任务队列排空后     |

### 2.4 窗口内消息的“注册表可用性”约束（重要）

1.20.1 的**远端（datapack 同步）注册表要等 `ClientboundLoginPacket.registryHolder` 才到达客户端**；
配置窗口发生在该包之前、客户端 `ClientPacketListener` 创建之前。因此：

- 窗口内消息的 codec **只能携带注册表无关数据**（ResourceLocation / ResourceKey / 原始类型等）；
- 凡解码时需要远端注册表的内容（datamap 内容、数据组件化负载、附件负载等）**必须留在进服后
  （PLAY）阶段**——这与实现一致：配置阶段只协商 Known，内容同步仍走 join/reload；
- 客户端在窗口内的 `PortEnvironment.registryAccess()` 不再返回 `RegistryAccess.EMPTY`，
  而是退回**客户端本地静态注册表层**
  （`ClientRegistryLayer.createRegistryAccess().compositeAccess()`，
  见 `wrapper/PortEnvironment.java#registryAccess()`），保证只依赖静态注册表的 codec 可正常解码；
  若某个 codec 需要的是服务端 datapack 注册表（如自定义维度/生物群系里的值），静态层无法覆盖，
  这类数据不应出现在配置阶段，请放回 PLAY 阶段同步。

---

## 3. 代码变更清单

新增：

- `src/main/java/org/mesdag/portlib/network/config/IPortCustomConfigurationTask.java`
  —— 配置阶段任务抽象（`type()` + `start(PortConfigurationContext)`，语义对应 1.21 的
  `ICustomConfigurationTask`）；
- `src/main/java/org/mesdag/portlib/network/config/PortConfigurationContext.java`
  —— 任务执行上下文：`send(...)` / `finish()` / `connection()` / `disconnect(...)`；
- `src/main/java/org/mesdag/portlib/network/config/PortConfigurationManager.java`
  —— 通用任务队列执行器：每次连接触发 `PortRegisterConfigurationTasksEvent` 收集任务；
  `ServerStage` 逐任务执行并经 `finishCurrentTask(type)` 推进；LOGIN 大包自动分片/重组；
  客户端等待 `configuration_finished` 的看门狗；能力判定与 continuation 存取；
- `src/main/java/org/mesdag/portlib/network/config/PortConfigurationFinishedPayload.java`
  —— 配置结束标记；
- `src/main/java/org/mesdag/portlib/network/config/PortConfigurationFragmentPayload.java`
  —— LOGIN 大负载分片消息（`first/last` 标记 + 目标类型 + 数据块）；
- `src/main/java/org/mesdag/portlib/event/network/PortRegisterConfigurationTasksEvent.java`
  —— 按连接的任务注册事件（对齐 1.21 `RegisterConfigurationTasksEvent`）；
- `src/main/java/org/mesdag/portlib/diff/mixin/ServerLoginPacketListenerImplMixin.java`
  —— tick 驱动阶段；`placeNewPlayer` HEAD 拦截推迟进世界；`@Invoker placeNewPlayer` 恢复；
- `src/main/java/org/mesdag/portlib/diff/mixin/ClientHandshakePacketListenerImplMixin.java`
  —— `handleGameProfile` HEAD 拦截推迟切 PLAY；`@Invoker handleGameProfile` 恢复收尾。

修改：

- `src/main/java/org/mesdag/portlib/network/PortNetworkHandler.java`
  —— 启用 `registerLoginS2C/registerLoginC2S`（含默认 handler 重载）、`channelName()`、
  `sendLoginToClient(...)`（fml:loginwrapper 信封发送）；
- `src/main/java/org/mesdag/portlib/network/IPortPacket.java`
  —— `Context` 增加 `connection()` 访问器；
- `src/main/java/org/mesdag/portlib/diff/datamap/PortRegistryDataMapNegotiation.java`
  —— Known/Reply 改注册为 LOGIN 方向；删除 join 时 `OnDatapackSyncEvent` 发 Known 的旧路径；
  类本身**直接实现 `IPortCustomConfigurationTask`**（类型 `KNOWN_TASK_TYPE`，单例 `INSTANCE`），
  经 `PortRegisterConfigurationTasksEvent` 按连接注册；Reply 到达后调用
  `PortConfigurationManager.finishCurrentTask(conn, KNOWN_TASK_TYPE)` 推进；
- `src/main/java/org/mesdag/portlib/PortLib.java` —— 构造器调用 `PortConfigurationManager.init()`；
- `src/main/resources/portlib.mixins.json` —— 服务端表加 `ServerLoginPacketListenerImplMixin`、
  客户端表加 `ClientHandshakePacketListenerImplMixin`。

未改动（有意为之）：

- datamap **内容**同步仍留在 join/reload（`PortDataMapLoader` 的 `OnDatapackSyncEvent`/reload 监听），
  与 1.21.1 一致（配置阶段只协商 known，内容在进服/重载时按已协商清单下发）；
- `ClientboundLoginPacket.registryHolder`（注册表数据）保持 1.20.1 原样，不搬进窗口；
- 附件初始同步位置保持现状（玩家附件在 `placeNewPlayer` 尾、level 附件在 `sendLevelInfo`，
  与 1.21.1 的位置本来一致）。

---

## 4. 线程 / 超时 / 边界

- 服务端阶段只在登录监听器 `tick()`（服务线程）推进；网络线程（收 Reply / `finishCurrentTask`）
  只向线程安全队列投递“完成请求”，由服务端 tick 消费。
- 客户端任务 handler 运行在客户端网络线程（与 vanilla `handleGameProfile` 所在线程一致）；
  被推迟的收尾同样在网络线程执行——与 1.20.1 原版行为一致。
- 超时：单个任务存活 > `MAX_TASK_TICKS`(200) 按 `multiplayer.disconnect.slow_login` 断开；
  服务端原登录超时（600 tick）继续兜底。
- 客户端看门狗：推迟收尾时会调度一次性断线（`CLIENT_CONFIGURATION_TIMEOUT_MILLIS` = 20s），
  收不到 `configuration_finished`（老服务端 / 中间代理）时给出友好提示断开，不再依赖裸 read-timeout。
- 大负载：单条登录负载 > `MAX_SINGLE_LOGIN_PAYLOAD_BYTES`(512 KiB) 时自动切成
  `PortConfigurationFragmentPayload`（每片 ≤ 64 KiB），客户端重组后按原类型解码分发；
  重组/解码失败会给出友好断线提示。
- 边界与兼容：
    - vanilla 客户端 / Forge 无 PortLib / 单机内存连接：判定 false，完全原版路径；
    - 服务器有 datamap、客户端没有（异常 mod 组合）：客户端仍会应答 Reply，服务端据此正常完成；
      mandatory 不一致由 `PortKnownRegistryDataMapsPayload.handle` 断开（沿用旧逻辑）；
    - 老版本 PortLib 服务端（无本阶段）连新客户端：客户端 20s 看门狗友好断线（版本不匹配场景）；
    - 同 UUID 顶号：服务端 DELAY_ACCEPT 路径最终也会经过 `placeNewPlayer` 拦截点，同样先配置后进世界。

---

## 5. 与 1.21.1 的语义对照

| 1.21.1                                          | 本实现                                                                                    |
|-------------------------------------------------|----------------------------------------------------------------------------------------|
| CONFIGURATION 协议状态（新 wire）                      | 不新增状态；窗口位于 LOGIN 协议内（profile 后、LoginPacket 前）                                          |
| `ServerConfigurationPacketListenerImpl` 任务队列    | `PortConfigurationManager.ServerStage`（挂在登录监听器 tick 上）                                 |
| `ConfigurationTask` / `finishCurrentTask(Type)` | `IPortCustomConfigurationTask` 队列 + `PortConfigurationManager.finishCurrentTask(type)` |
| `RegisterConfigurationTasksEvent`（IModBus）      | `PortRegisterConfigurationTasksEvent`（event/network，每次连接、启动阶段前经                        
 `PortEventHandler` 触发）；datamap 任务即通过该事件注册       |
| `JoinWorldTask` → FinishConfiguration → ack     | `PortConfigurationFinishedPayload`（S2C）作为窗口结束信号                                        |
| `ConnectionType` / `hasChannel`                 | Forge 握手交换的 `ConnectionData` 频道清单（`portlib:main` 有无）                                   |
| `SyncRegistries`（frozen registry）               | 1.20.1 无对等物（注册表在 LoginPacket 内），不搬                                                     |
| datamap 协商（KNOWN）→ 进服后内容同步                      | 同构：协商入窗，内容同步留在 join/reload                                                             |

---

## 6. 状态 / TODO / 下一步

已完成：

1. 方案 C 核心（窗口/队列/双端 mixin）与 datamap 协商入窗（首次加入即协商、内容同步不再漏）；
2. 断线加固（阶段随 `onDisconnect` 清理、关闭连接不驱动、防二次 accept）；
3. 注册表兜底（客户端登录期 `PortEnvironment.registryAccess()` 退回本地静态层）；
4. 三项增强：客户端看门狗（20s 友好断线）、LOGIN 大包自动分片重组、按连接任务事件
   `PortRegisterConfigurationTasksEvent`。

下一步（联调 / 扩展建议）：

1. 回归联调矩阵：单机（不触发）→ 局域网 PortLib↔PortLib（触发）→ vanilla↔PortLib → Forge↔PortLib；
   并新增大负载构造（如上千 datamap 清单）验证分片路径、老服务端/代理验证看门狗提示。
2. 配置/语言键：`portlib.network.configuration.timeout` 等转义键目前用
   `translatableWithFallback` 兜底；如需本地化文案可加入 `src/generated/resources/assets/.../lang`。
3. 作为新 `IPortCustomConfigurationTask` 加入队列的后续候选：配置文件同步、frozen 校验等
   （现可直接通过 `PortRegisterConfigurationTasksEvent` 按连接注册）。
