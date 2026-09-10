package org.mesdag.portlib.diff.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.data.LanguageProvider;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.registries.PortRegistryEntry;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PortLanguageProvider extends LanguageProvider {
    private final boolean isEn;

    public PortLanguageProvider(PackOutput output, boolean isEn) {
        super(output, PortLib.MODID, isEn ? "en_us" : "zh_cn");
        this.isEn = isEn;
    }

    @Override
    protected void addTranslations() {
        addAttribute(PortLib.BLOCK_BREAK_SPEED, "Block Break Speed", "方块破坏速度");
        addAttribute(PortLib.BURNING_TIME, "Burning Time", "燃烧时间");
        addAttribute(PortLib.EXPLOSION_KNOCKBACK_RESISTANCE, "Explosion Knockback Resistance", "爆炸击退抗性");
        addAttribute(PortLib.FALL_DAMAGE_MULTIPLIER, "Fall Damage Multiplier", "摔落伤害倍率");
        addAttribute(PortLib.JUMP_STRENGTH_1211, "Jump Strength", "跳跃力度");
        addAttribute(PortLib.MAX_ABSORPTION, "Max Absorption", "最大伤害吸收");
        addAttribute(PortLib.MINING_EFFICIENCY, "Mining Efficiency", "挖掘效率");
        addAttribute(PortLib.MOVEMENT_EFFICIENCY, "Movement Efficiency", "移动效率");
        addAttribute(PortLib.OXYGEN_BONUS, "Oxygen Bonus", "氧气加成");
        addAttribute(PortLib.SAFE_FALL_DISTANCE, "Safe Fall Distance", "安全摔落距离");
        addAttribute(PortLib.SCALE, "Scale", "体型缩放");
        addAttribute(PortLib.SNEAKING_SPEED, "Sneaking Speed", "潜行速度");
        addAttribute(PortLib.SUBMERGED_MINING_SPEED, "Submerged Mining Speed", "水下挖掘速度");
        addAttribute(PortLib.SWEEPING_DAMAGE_RATIO, "Sweeping Damage Ratio", "横扫伤害比例");
        addAttribute(PortLib.WATER_MOVEMENT_EFFICIENCY, "Water Movement Efficiency", "水中移动效率");
        addAttribute(PortLib.CREATIVE_FLIGHT, "Creative Flight", "创造飞行");

        add("portlib.value.flat", "%s");
        add("portlib.value.percent", "%s%%");
        add("portlib.value.boolean.enabled", "Enabled");
        add("portlib.value.boolean.disabled", "Disabled");
        add("portlib.value.boolean.enable", "Enables");
        add("portlib.value.boolean.disable", "Disables");
        add("portlib.value.boolean.invalid", "Invalid");
        add("portlib.modifier.plus", "+%s %s");
        add("portlib.modifier.take", "%s %s");
        add("portlib.modifier.bool", "%s %s");

        add("portlib.configuration.title", "%s Configuration", "%s配置");
        add("portlib.configuration.type.client", "%s Client Configuration", "%s客户端配置");
        add("portlib.configuration.type.common", "%s Common Configuration", "%s通用配置");
        add("portlib.configuration.type.server", "%s Server Configuration", "%s服务端配置");
        add("portlib.configuration.not_loaded", "This configuration is not loaded in the current context.", "此配置在当前环境中尚未加载。");
        add("portlib.configuration.requires_world_restart", "Changes to this option require re-entering the world.", "修改此项后需要重新进入世界。");
        add("portlib.configuration.undo", "Undo", "撤销");
        add("portlib.configuration.reset", "Reset", "重置");

        add("portlib.network.advanced_add_entity.failed", "Failed to process advanced entity spawn data: %s", "处理高级实体生成数据失败：%s");
        add("portlib.network.data_maps.failed", "Failed to handle registry data map sync for registry %s: %s", "处理注册表数据映射同步失败 %s: %s");
        add("portlib.network.data_maps.missing_our", "Cannot connect to server as it is missing mandatory registry data maps present on the client: %s", "无法连接至服务器，因为客户端缺少必要的注册表数据映射：%s");
        add("portlib.network.data_maps.missing_their", "Cannot connect to server as it has mandatory registry data maps not present on the client: %s", "无法连接至服务器，因为客户端上不存在必要的注册表数据映射：%s");
        add("portlib.network.configuration.fragment_failed", "Failed to process a PortLib configuration fragment.", "处理 PortLib 配置分片失败。");
        add("portlib.network.configuration.fragment_out_of_order", "PortLib configuration fragment received out of order.", "收到乱序的 PortLib 配置分片。");
        add("portlib.network.configuration.timeout", "PortLib configuration phase timed out. The server may run an incompatible PortLib version.", "PortLib 配置阶段超时。服务器可能运行着不兼容的 PortLib 版本。");

        add(PortLib.TUFF_SLAB.get().getDescriptionId(), "Tuff Slab", "凝灰岩台阶");
        add(PortLib.TUFF_STAIRS.get().getDescriptionId(), "Tuff Stairs", "凝灰岩楼梯");
        add(PortLib.TUFF_WALL.get().getDescriptionId(), "Tuff Wall", "凝灰岩墙");
        add(PortLib.POLISHED_TUFF.get().getDescriptionId(), "Polished Tuff", "磨制凝灰岩");
        add(PortLib.POLISHED_TUFF_SLAB.get().getDescriptionId(), "Polished Tuff Slab", "磨制凝灰岩台阶");
        add(PortLib.POLISHED_TUFF_STAIRS.get().getDescriptionId(), "Polished Tuff Stairs", "磨制凝灰岩楼梯");
        add(PortLib.POLISHED_TUFF_WALL.get().getDescriptionId(), "Polished Tuff Wall", "磨制凝灰岩墙");
        add(PortLib.CHISELED_TUFF.get().getDescriptionId(), "Chiseled Tuff", "錾制凝灰岩");
        add(PortLib.TUFF_BRICKS.get().getDescriptionId(), "Tuff Bricks", "凝灰岩砖");
        add(PortLib.TUFF_BRICK_SLAB.get().getDescriptionId(), "Tuff Brick Slab", "凝灰岩砖台阶");
        add(PortLib.TUFF_BRICK_STAIRS.get().getDescriptionId(), "Tuff Brick Stairs", "凝灰岩砖楼梯");
        add(PortLib.TUFF_BRICK_WALL.get().getDescriptionId(), "Tuff Brick Wall", "凝灰岩砖墙");
        add(PortLib.CHISELED_TUFF_BRICKS.get().getDescriptionId(), "Chiseled Tuff Bricks", "錾制凝灰岩砖");
    }

    private void addAttribute(PortRegistryEntry<Attribute, ?> entry, String en, String zh) {
        add(entry.get().getDescriptionId(), en, zh);
    }

    private static String toTitleCase(String raw) {
        return Arrays.stream(raw.split("_"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    private void add(String key, String en, String zh) {
        add(key, isEn ? en : zh);
    }
}
