package org.mesdag.portlib.diff.datamap;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import net.minecraftforge.registries.tags.ITagManager;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.datamap.PortAdvancedDataMapType;
import org.mesdag.portlib.datamap.PortDataMapType;
import org.mesdag.portlib.datamap.PortDataMapValueMerger;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.PortDataPackRegistriesHooks;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.registries.datamaps.PortRegisterDataMapTypesEvent;
import org.mesdag.portlib.wrapper.core.PortHolder;
import org.mesdag.portlib.wrapper.core.PortRegistry;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

import java.io.Reader;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

@Diff
@SuppressWarnings({"rawtypes", "unchecked"})
public class PortDataMapLoader implements PreparableReloadListener {
    static PortDataMapLoader INSTANCE;
    private static Map<ResourceKey<Registry<?>>, Map<PortIdentifier, PortDataMapType<?, ?>>> dataMaps = Map.of();

    private final ICondition.IContext conditionContext;
    private final RegistryAccess registryAccess;
    private final Map<IForgeRegistry<?>, Map<PortDataMapType<?, ?>, Map<ResourceKey<?>, ?>>> byRegistries = new IdentityHashMap<>();

    private Map<ResourceKey<? extends Registry<?>>, LoadResult<?>> results;

    PortDataMapLoader(ICondition.IContext conditionContext, RegistryAccess registryAccess) {
        this.conditionContext = conditionContext;
        this.registryAccess = registryAccess;
    }

    public static void init() {
        PortRegistryDataMapNegotiation.init();
        PortLib.NETWORK_HANDLER.registerInGameS2C(
                PortRegistryDataMapSyncPayload.IDENTIFIER,
                PortRegistryDataMapSyncPayload.STREAM_CODEC,
                PortRegistryDataMapSyncPayload::handle
        );
        PortEventHandler.addListener((AddReloadListenerEvent event) -> {
            INSTANCE = new PortDataMapLoader(event.getConditionContext(), event.getRegistryAccess());
            event.addListener(INSTANCE);
        });
        PortEventHandler.addListener((TagsUpdatedEvent event) -> {
            if (event.getUpdateCause() == TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD) {
                INSTANCE.apply();
            }
        });
        PortEventHandler.addListener((OnDatapackSyncEvent event) -> {
            for (Map.Entry<ResourceKey<Registry<?>>, Map<PortIdentifier, PortDataMapType<?, ?>>> entry : getDataMaps().entrySet()) {
                IForgeRegistry<?> registry = RegistryManager.ACTIVE.getRegistry(entry.getKey().location());
                ServerPlayer player = event.getPlayer();
                if (player == null) {
                    for (ServerPlayer serverPlayer : event.getPlayers()) {
                        handleSync(serverPlayer, registry, (ResourceKey) entry.getKey());
                    }
                } else {
                    handleSync(player, registry, (ResourceKey) entry.getKey());
                }
            }
        });
    }

    private static <T> void handleSync(ServerPlayer player, IForgeRegistry<T> registry, ResourceKey<Registry<T>> registryKey) {
        if (player.connection.connection.isMemoryConnection() && PortDataPackRegistriesHooks.getSyncedRegistry((ResourceKey) registryKey) == null) {
            return;
        }
        Map<ResourceKey<? extends Registry<?>>, Collection<PortIdentifier>> map = player.connection.connection.channel().attr(PortKnownRegistryDataMapsReplyPayload.ATTRIBUTE_KNOWN_DATA_MAPS).get();
        if (map == null) return;
        Collection<PortIdentifier> attachments = map.getOrDefault(registryKey, List.of());
        if (attachments.isEmpty()) return;
        Map<PortIdentifier, Map<ResourceKey<T>, ?>> att = new HashMap<>();
        attachments.forEach(key -> {
            var attach = getDataMap(registryKey, key);
            if (attach == null || attach.networkCodec() == null) return;
            att.put(key, INSTANCE.getDataMap(registry, attach));
        });
        if (!att.isEmpty()) {
            PortLib.NETWORK_HANDLER.sendToPlayer(player, new PortRegistryDataMapSyncPayload(registryKey, att));
        }
    }

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
        return load(resourceManager, backgroundExecutor, preparationsProfiler)
                .thenCompose(preparationBarrier::wait)
                .thenAcceptAsync(values -> this.results = values, gameExecutor);
    }

    public void apply() {
        results.forEach((key, result) -> this.apply(RegistryManager.ACTIVE.getRegistry(key.location()), result));
        this.results = null;
    }

    private <T> void apply(IForgeRegistry<T> registry, LoadResult<T> result) {
        clear(registry);
        result.results().forEach((key, entries) -> getInnerMap(registry).put(
                key, this.buildDataMap(registry, key, (List) entries)));
//        NeoForge.EVENT_BUS.post(new DataMapsUpdatedEvent(registryAccess, registry, DataMapsUpdatedEvent.UpdateCause.SERVER_RELOAD));
    }

    private <T, R> Map<ResourceKey<R>, T> buildDataMap(IForgeRegistry<R> registry, PortDataMapType<R, T> attachment, List<DataMapFile<T, R>> entries) {
        record WithSource<T, R>(T attachment, Either<TagKey<R>, ResourceKey<R>> source) {}
        Map<ResourceKey<R>, WithSource<T, R>> result = new IdentityHashMap<>();
        PortDataMapValueMerger<R, T> merger = attachment instanceof PortAdvancedDataMapType<R, T, ?> adv ? adv.merger() : PortDataMapValueMerger.defaultMerger();
        entries.forEach(entry -> {
            if (entry.replace()) {
                result.clear();
            }

            entry.values().forEach((tKey, value) -> {
                if (value.isEmpty()) return;

                resolve(registry, tKey, true, holder -> {
                    var newValue = value.get().carrier();
                    var key = holder.getKey();
                    var oldValue = result.get(key);
                    if (oldValue == null || newValue.replace()) {
                        result.put(key, new WithSource<>(newValue.value(), tKey));
                    } else {
                        result.put(key, new WithSource<>(merger.merge(PortRegistry.wrap(registry), oldValue.source(), oldValue.attachment(), tKey, newValue.value()), tKey));
                    }
                });
            });

            for (var removal : entry.removals()) {
                if (removal.remover().isPresent()) {
                    var remover = removal.remover().orElseThrow();
                    resolve(registry, removal.key(), false, holder -> {
                        var key = PortHolder.getKey(holder);
                        var oldValue = result.get(key);
                        if (oldValue != null) {
                            var newValue = remover.remove(oldValue.attachment(), PortRegistry.wrap(registry), oldValue.source(), holder.value());
                            if (newValue.isEmpty()) {
                                result.remove(key);
                            } else {
                                result.put((ResourceKey<R>) key, new WithSource<>(newValue.get(), oldValue.source()));
                            }
                        }
                    });
                } else {
                    resolve(registry, removal.key(), false, holder -> result.remove(PortHolder.getKey(holder)));
                }
            }
        });
        Map<ResourceKey<R>, T> newMap = new IdentityHashMap<>();
        result.forEach((key, val) -> newMap.put(key, val.attachment()));

        return newMap;
    }

    private <R> void resolve(IForgeRegistry<R> registry, Either<TagKey<R>, ResourceKey<R>> value, boolean required, Consumer<Holder<R>> consumer) {
        if (value.left().isPresent()) {
            ITagManager<R> tags = registry.tags();
            Objects.requireNonNull(tags, "This registry not supports tags and/or has a wrapper registry");
            tags.getTag(value.left().orElseThrow()).forEach(v -> consumer.accept(registry.getHolder(v).orElseThrow()));
        } else {
            var object = registry.getHolder(value.right().orElseThrow());
            if (object.isPresent()) {
                consumer.accept(object.get());
            } else if (required) {
                PortLib.LOGGER.error("Object with ID {} specified in data map for registry {} doesn't exist", value.right().orElseThrow().location(), registry.getRegistryKey().location());
            }
        }
    }

    private CompletableFuture<Map<ResourceKey<? extends Registry<?>>, LoadResult<?>>> load(ResourceManager manager, Executor executor, ProfilerFiller profiler) {
        return CompletableFuture.supplyAsync(() -> load(manager, profiler, registryAccess, conditionContext), executor);
    }

    private static Map<ResourceKey<? extends Registry<?>>, LoadResult<?>> load(ResourceManager manager, ProfilerFiller profiler, RegistryAccess access, ICondition.IContext context) {
        Map<ResourceKey<? extends Registry<?>>, LoadResult<?>> values = new HashMap<>();
        access.registries().forEach(registryEntry -> {
            var registryKey = registryEntry.key();
            profiler.push("registry_data_maps/" + registryKey.location() + "/locating");
            ResourceLocation location = registryKey.location();
            var fileToId = FileToIdConverter.json("data_maps/" + getFolderLocation(PortIdentifier.fromNamespaceAndPath(location.getNamespace(), location.getPath())));
            for (Map.Entry<ResourceLocation, List<Resource>> entry : fileToId.listMatchingResourceStacks(manager).entrySet()) {
                ResourceLocation attachmentId = fileToId.fileToId(entry.getKey());
                var attachment = getDataMap((ResourceKey) registryKey, PortIdentifier.fromNamespaceAndPath(attachmentId.getNamespace(), attachmentId.getPath()));
                if (attachment == null) {
                    PortLib.LOGGER.warn("Found data map file for non-existent data map type '{}' on registry '{}'.", attachmentId, registryKey.location());
                    continue;
                }
                profiler.popPush("registry_data_maps/" + registryKey.location() + "/" + attachmentId + "/loading");
                values.computeIfAbsent(registryKey, k -> new LoadResult<>(new HashMap<>())).results.put(attachment, readData(
                        attachment, (ResourceKey) registryKey, entry.getValue()));
            }
            profiler.pop();
        });

        return values;
    }

    public static String getFolderLocation(PortIdentifier registryId) {
        return (registryId.getNamespace().equals(PortIdentifier.DEFAULT_NAMESPACE) ? "" : registryId.getNamespace() + "/") + registryId.getPath();
    }

    private static <A, T> List<DataMapFile<A, T>> readData(PortDataMapType<T, A> attachmentType, ResourceKey<Registry<T>> registryKey, List<Resource> resources) {
        final List<DataMapFile<A, T>> entries = new LinkedList<>();
        for (final Resource resource : resources) {
            try (Reader reader = resource.openAsReader()) {
                JsonObject jsonelement = JsonParser.parseReader(reader).getAsJsonObject();
                entries.add(DataMapFile.read(registryKey, jsonelement, attachmentType));
            } catch (Exception exception) {
                PortLib.LOGGER.error("Could not read data map of type {} for registry {}", attachmentType.id(), registryKey, exception);
            }
        }
        return entries;
    }

    private record LoadResult<T>(Map<PortDataMapType<T, ?>, List<DataMapFile<?, T>>> results) {}

    <T> void clear(IForgeRegistry<T> registry) {
        Map<PortDataMapType<?, ?>, Map<ResourceKey<?>, ?>> map = byRegistries.get(registry);
        if (map != null) {
            map.clear();
        }
    }

    public <T, A> @Nullable A getData(IForgeRegistry<T> registry, PortDataMapType<T, A> type, ResourceKey<T> key) {
        Map<PortDataMapType<?, ?>, Map<ResourceKey<?>, ?>> map = byRegistries.get(registry);
        if (map == null) return null;
        Map<ResourceKey<?>, ?> map1 = map.get(type);
        if (map1 == null) return null;
        return (A) map1.get(key);
    }

    public <T, A> Map<ResourceKey<T>, A> getDataMap(IForgeRegistry<T> registry, PortDataMapType<T, A> type) {
        return (Map<ResourceKey<T>, A>) getInnerMap(registry).getOrDefault(type, Map.of());
    }

    <T> Map<PortDataMapType<T, ?>, Map<ResourceKey<T>, ?>> getInnerMap(IForgeRegistry<T> registry) {
        return (Map<PortDataMapType<T, ?>, Map<ResourceKey<T>, ?>>) (Map) byRegistries.computeIfAbsent(registry, r -> new IdentityHashMap<>());
    }

    public static <R> @Nullable PortDataMapType<R, ?> getDataMap(ResourceKey<? extends Registry<R>> registry, PortIdentifier key) {
        var map = dataMaps.get(registry);
        return map == null ? null : (PortDataMapType<R, ?>) map.get(key);
    }

    public static Map<ResourceKey<Registry<?>>, Map<PortIdentifier, PortDataMapType<?, ?>>> getDataMaps() {
        return dataMaps;
    }

    public static void initDataMaps() {
        Map<ResourceKey<Registry<?>>, Map<PortIdentifier, PortDataMapType<?, ?>>> dataMapTypes = new HashMap<>();
        PortEventHandler.postEvent(new PortRegisterDataMapTypesEvent(dataMapTypes));
        dataMaps = new IdentityHashMap<>();
        dataMapTypes.forEach((key, values) -> dataMaps.put(key, Collections.unmodifiableMap(values)));
        dataMaps = Collections.unmodifiableMap(dataMapTypes);
    }
}
