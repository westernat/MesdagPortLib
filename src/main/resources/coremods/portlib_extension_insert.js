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
        }
    };
}
