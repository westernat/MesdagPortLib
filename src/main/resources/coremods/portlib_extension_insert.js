function initializeCoreMod() {
    return {
        'vertex_consumer': transformer('com/mojang/blaze3d/vertex/VertexConsumer', 'IPortVertexConsumerExtension'),
        'holder': transformer('net/minecraft/core/Holder', 'IPortHolderExtension'),
        'block_entity_renderer': transformer('net/minecraft/client/renderer/blockentity/BlockEntityRenderer', 'IPortBlockRendererExtension'),
        'id_map': transformer('net/minecraft/core/IdMap', 'IPortIdMapExtension'),
        'registry_access': transformer('net/minecraft/core/RegistryAccess', 'IPortRegistryAccessExtension'),
        'holder_lookup_provider': transformer('net/minecraft/core/HolderLookup$Provider', 'IPortHolderLookupProviderExtension'),
        'registry': transformer('net/minecraft/core/Registry', 'IPortRegistryExtension'),
        'i_forge_registry': transformer('net/minecraftforge/registries/IForgeRegistry', 'IPortForgeRegistryExtension')
    };
}

function transformer(target, extension) {
    extension = 'org/mesdag/portlib/wrapper/common/extensions/' + extension;
    return {
        'target': {
            'type': 'CLASS',
            'name': target
        },
        'transformer': function (node) {
            if (!node.interfaces.contains(extension)) {
                node.interfaces.add(extension);
            }
            return node;
        }
    };
}
