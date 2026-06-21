function initializeCoreMod() {
    return {
        'insert_port_codec_extension': {
            'target': {
                'type': 'CLASS',
                'name': 'com/mojang/serialization/Codec'
            },
            'transformer': function (node) {
                if (!node.interfaces.contains('org/mesdag/portlib/wrapper/common/extensions/IPortCodecExtension')) {
                    node.interfaces.add('org/mesdag/portlib/wrapper/common/extensions/IPortCodecExtension');
                    if (node.signature != null) {
                        node.signature = node.signature + 'Lorg/mesdag/portlib/wrapper/common/extensions/IPortCodecExtension<TA;>;';
                    }
                }
                return node;
            }
        },
        'insert_port_holder_extension': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/core/Holder'
            },
            'transformer': function (node) {
                if (!node.interfaces.contains('org/mesdag/portlib/wrapper/common/extensions/IPortHolderExtension')) {
                    node.interfaces.add('org/mesdag/portlib/wrapper/common/extensions/IPortHolderExtension');
                    if (node.signature != null) {
                        node.signature = node.signature + 'Lorg/mesdag/portlib/wrapper/common/extensions/IPortHolderExtension<TT;>;';
                    }
                }
                return node;
            }
        },
        'insert_port_block_renderer_extension': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/client/renderer/blockentity/BlockEntityRenderer'
            },
            'transformer': function (node) {
                if (!node.interfaces.contains('org/mesdag/portlib/wrapper/common/extensions/IPortBlockRendererExtension')) {
                    node.interfaces.add('org/mesdag/portlib/wrapper/common/extensions/IPortBlockRendererExtension');
                    if (node.signature != null) {
                        node.signature = node.signature + 'Lorg/mesdag/portlib/wrapper/common/extensions/IPortBlockRendererExtension<TT;>;';
                    }
                }
                return node;
            }
        },
        'insert_port_id_map_extension': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/core/IdMap'
            },
            'transformer': function (node) {
                if (!node.interfaces.contains('org/mesdag/portlib/wrapper/common/extensions/IPortIdMapExtension')) {
                    node.interfaces.add('org/mesdag/portlib/wrapper/common/extensions/IPortIdMapExtension');
                    if (node.signature != null) {
                        node.signature = node.signature + 'Lorg/mesdag/portlib/wrapper/common/extensions/IPortIdMapExtension<TT;>;';
                    }
                }
                return node;
            }
        },
        'insert_port_registry_access_extension': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/core/RegistryAccess'
            },
            'transformer': function (node) {
                if (!node.interfaces.contains('org/mesdag/portlib/wrapper/common/extensions/IPortRegistryAccessExtension')) {
                    node.interfaces.add('org/mesdag/portlib/wrapper/common/extensions/IPortRegistryAccessExtension');
                }
                return node;
            }
        },
        'insert_port_holder_lookup_extension': {
            'target': {
                'type': 'CLASS',
                'name': 'net/minecraft/core/HolderLookup$Provider'
            },
            'transformer': function (node) {
                if (!node.interfaces.contains('org/mesdag/portlib/wrapper/common/extensions/IPortHolderLookupProviderExtension')) {
                    node.interfaces.add('org/mesdag/portlib/wrapper/common/extensions/IPortHolderLookupProviderExtension');
                }
                return node;
            }
        }
    };
}
