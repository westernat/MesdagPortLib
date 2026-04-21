var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI')
var Opcodes = Java.type('org.objectweb.asm.Opcodes')
var TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode')
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode')
var InsnList = Java.type('org.objectweb.asm.tree.InsnList')
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode')
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode')
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode')
var LocalVariableNode = Java.type('org.objectweb.asm.tree.LocalVariableNode')
var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode')

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
                for (i = 0; i < node.instructions.size(); i++) {
                    var current = node.instructions.get(i)
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
        },
        'modify_return_value_can_harvest_block': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraftforge/common/extensions/IForgeBlock',
                'methodName': 'canHarvestBlock',
                'methodDesc': '(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;)Z'
            },
            'transformer': function (node) {
                // return ForgeHooks.isCorrectToolForDrops(state, player);
                if (node.instructions.size() == 7) {
                    var L0 = node.instructions.get(0)
                    var IRETURN = node.instructions.get(5);
                    var L1 = node.instructions.get(6)
                    if (L0 instanceof LabelNode && (IRETURN instanceof InsnNode && IRETURN.getOpcode() == Opcodes.IRETURN) && L1 instanceof LabelNode) {
                        var list = new InsnList()
                        // boolean success = ForgeHooks.isCorrectToolForDrops(state, player);
                        list.add(new VarInsnNode(Opcodes.ISTORE, 5))
                        // return PortPlayerEvent.PortHarvestCheck.doPlayerHarvestCheck(player, state, level, pos, ForgeHooks.isCorrectToolForDrops(state, player));
                        list.add(new VarInsnNode(Opcodes.ALOAD, 4))
                        list.add(new VarInsnNode(Opcodes.ALOAD, 1))
                        list.add(new VarInsnNode(Opcodes.ALOAD, 2))
                        list.add(new VarInsnNode(Opcodes.ALOAD, 3))
                        list.add(new VarInsnNode(Opcodes.ILOAD, 5))
                        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "org/mesdag/portlib/event/entity/player/PortPlayerEvent$PortHarvestCheck", "doPlayerHarvestCheck", "(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Z)Z", false));
                        node.instructions.insertBefore(IRETURN, list)
                        node.visitLocalVariable('success', 'Z', null, L0.getLabel(), L1.getLabel(), 5)
                        node.visitMaxs(5, 6)
                    }
                }
                return node
            }
        }
    }
}
