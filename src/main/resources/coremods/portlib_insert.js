// var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI')
var Opcodes = Java.type('org.objectweb.asm.Opcodes')
var TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode')
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode')
var InsnList = Java.type('org.objectweb.asm.tree.InsnList')
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode')
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode')

function initializeCoreMod() {
    return {
        'insert_set_reduction': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraft/world/entity/LivingEntity',
                'methodName': 'getDamageAfterMagicAbsorb',
                'methodDesc': '(Lnet/minecraft/world/damagesource/DamageSource;F)F'
            },
            'transformer': function (node) {
                var last = null
                for (x = 0; x < node.instructions.size(); x++) {
                    var current = node.instructions.get(x)
                    if (current instanceof TypeInsnNode && current.getOpcode() == Opcodes.INSTANCEOF && current.desc === 'net/minecraft/server/level/ServerPlayer') {
                        if (last instanceof VarInsnNode && last.getOpcode() == Opcodes.ALOAD && last.var == 0) {
                            // ASMAPI.log('DEBUG', 'current: ' + current + ', ' + 'last: ' + last)
                            var list = new InsnList()
                            list.add(new VarInsnNode(Opcodes.ALOAD, 0))
                            list.add(new FieldInsnNode(Opcodes.GETFIELD, 'net/minecraft/world/entity/LivingEntity', 'portlib$damageContainers', 'Ljava/util/Stack;'))
                            list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, 'java/util/Stack', 'peek', '()Ljava/lang/Object;', false))
                            list.add(new TypeInsnNode(Opcodes.CHECKCAST, 'org/mesdag/portlib/wrapper/common/damagesource/PortDamageContainer'))
                            list.add(new FieldInsnNode(Opcodes.GETSTATIC, 'org/mesdag/portlib/wrapper/common/damagesource/PortDamageContainer$PortReduction', 'MOB_EFFECTS', 'Lorg/mesdag/portlib/wrapper/common/damagesource/PortDamageContainer$PortReduction;'))
                            list.add(new VarInsnNode(Opcodes.FLOAD, 7))
                            list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, 'org/mesdag/portlib/wrapper/common/damagesource/PortDamageContainer', 'setReduction', '(Lorg/mesdag/portlib/wrapper/common/damagesource/PortDamageContainer$PortReduction;F)V', false))
                            node.instructions.insertBefore(last, list)
                            break
                        }
                    }
                    last = current
                }
                return node
            }
        }
    }
}
