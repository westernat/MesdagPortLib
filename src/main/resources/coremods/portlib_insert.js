var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI')
var Opcodes = Java.type('org.objectweb.asm.Opcodes')
var TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode')
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode')
var InsnList = Java.type('org.objectweb.asm.tree.InsnList')
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode')
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode')
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode')
var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode')
var LocalVariableNode = Java.type('org.objectweb.asm.tree.LocalVariableNode')
var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode')

function initializeCoreMod() {
    return {
        'port_get_burn_time': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraftforge/common/extensions/IForgeItem',
                'methodName': 'getBurnTime',
                'methodDesc': '(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/crafting/RecipeType;)I'
            },
            'transformer': function (node) {
                if (node.instructions.size() == 2) {
                    var iconst = node.instructions.get(0);
                    var iret = node.instructions.get(1);
                    if (iconst.getOpcode() == Opcodes.ICONST_M1 && iret.getOpcode() == Opcodes.IRETURN) {
                        var list = new InsnList();
                        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        list.add(new VarInsnNode(Opcodes.ALOAD, 1));
                        list.add(new VarInsnNode(Opcodes.ALOAD, 2));
                        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                            'PortLib/extensions/net/minecraft/world/item/Item/PortItemExtension',
                            'getBurnTime',
                            '(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/crafting/RecipeType;)I', false));
                        node.instructions.remove(iconst);
                        node.instructions.insertBefore(iret, list);
                    }
                }
                return node;
            }
        },
        'port_submerged_mining_speed': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraft/world/entity/player/Player',
                'methodName': 'getDigSpeed',
                'methodDesc': '(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)F'
            },
            'transformer': function (node) {
                // Transform:
                //   if (this.isEyeInFluid(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(this)) { f /= 5.0F; }
                // into:
                //   if (this.isEyeInFluid(FluidTags.WATER)) {
                //       float speed = (float) this.getAttributeValue(PortAttributesExtension.submergedMiningSpeed());
                //       if (EnchantmentHelper.hasAquaAffinity(this)) { speed = Math.min(speed * 5.0F, 1.0F); }
                //       f *= speed;
                //   }

                function prevReal(list, from) {
                    for (var j = from - 1; j >= 0; j--) {
                        if (list.get(j).getOpcode() >= 0) return {insn: list.get(j), index: j};
                    }
                    return null;
                }

                // Start from LDC 5.0 + FDIV, then walk backward by opcode only
                for (var i = 0; i < node.instructions.size(); i++) {
                    var insn = node.instructions.get(i);
                    if (insn.getOpcode() == Opcodes.LDC && insn.cst === 5.0) {
                        var fdivInsn = node.instructions.get(i + 1);
                        if (!(fdivInsn && fdivInsn.getOpcode() == Opcodes.FDIV)) continue;

                        // Walk backward: FDIV ← LDC ← FLOAD f
                        var r = prevReal(node.instructions, i);
                        if (!r || r.insn.getOpcode() != Opcodes.FLOAD) continue;
                        var fload_f = r.insn;
                        var fVar = fload_f.var;

                        // Walk forward from FDIV to find FSTORE f
                        var fstore_f = null;
                        for (var k = i + 2; k < node.instructions.size(); k++) {
                            var next = node.instructions.get(k);
                            if (next.getOpcode() == Opcodes.FSTORE && next.var == fVar) {
                                fstore_f = next;
                                break;
                            } else if (next.getOpcode() >= 0) {
                                break; // unexpected instruction
                            }
                        }
                        if (!fstore_f) continue;

                        // Walk backward: FLOAD ← IFNE ← INVOKESTATIC ← ALOAD 0
                        var r1 = prevReal(node.instructions, r.index);
                        if (!r1 || r1.insn.getOpcode() != Opcodes.IFNE) continue;
                        var ifne = r1.insn;

                        var r2 = prevReal(node.instructions, r1.index);
                        if (!r2 || r2.insn.getOpcode() != Opcodes.INVOKESTATIC) continue;
                        var invoke_aa = r2.insn;

                        var r3 = prevReal(node.instructions, r2.index);
                        if (!r3 || r3.insn.getOpcode() != Opcodes.ALOAD || r3.insn.var != 0) continue;
                        var aload0_aa = r3.insn;

                        // Walk backward: ALOAD 0 ← IFEQ ← INVOKEVIRTUAL ← GETSTATIC ← ALOAD 0
                        var r4 = prevReal(node.instructions, r3.index);
                        if (!r4 || r4.insn.getOpcode() != Opcodes.IFEQ) continue;
                        var ifeq = r4.insn;

                        if (ifeq.label !== ifne.label) continue;

                        var r5 = prevReal(node.instructions, r4.index);
                        if (!r5 || r5.insn.getOpcode() != Opcodes.INVOKEVIRTUAL) continue;

                        var r6 = prevReal(node.instructions, r5.index);
                        if (!r6 || r6.insn.getOpcode() != Opcodes.GETSTATIC) continue;

                        var r7 = prevReal(node.instructions, r6.index);
                        if (!r7 || r7.insn.getOpcode() != Opcodes.ALOAD || r7.insn.var != 0) continue;

                        var speedVar = node.maxLocals;
                        node.maxLocals = speedVar + 1;

                        // Remove moved/replaced instructions
                        node.instructions.remove(aload0_aa);
                        node.instructions.remove(invoke_aa);
                        node.instructions.remove(ifne);
                        node.instructions.remove(fload_f);
                        node.instructions.remove(insn);
                        node.instructions.remove(fdivInsn);
                        node.instructions.remove(fstore_f);

                        var newCode = new InsnList();

                        // float speed = (float) getAttributeValue(PortAttributesExtension.submergedMiningSpeed());
                        newCode.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        newCode.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                            'PortLib/extensions/net/minecraft/world/entity/ai/attributes/Attributes/PortAttributesExtension',
                            'submergedMiningSpeed',
                            '()Lnet/minecraft/core/Holder;', false));
                        newCode.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                            'net/minecraft/world/entity/LivingEntity',
                            'getAttributeValue',
                            '(Lnet/minecraft/core/Holder;)D', false));
                        newCode.add(new InsnNode(Opcodes.D2F));
                        newCode.add(new VarInsnNode(Opcodes.FSTORE, speedVar));

                        // MOVED: if (EnchantmentHelper.hasAquaAffinity(this))
                        newCode.add(aload0_aa);
                        newCode.add(invoke_aa);
                        var noAffinityLabel = new LabelNode();
                        newCode.add(new JumpInsnNode(Opcodes.IFEQ, noAffinityLabel));

                        // speed = Math.min(speed * 5.0F, 1.0F);
                        newCode.add(new VarInsnNode(Opcodes.FLOAD, speedVar));
                        newCode.add(ASMAPI.buildNumberLdcInsnNode(5.0, ASMAPI.NumberType.FLOAT));
                        newCode.add(new InsnNode(Opcodes.FMUL));
                        newCode.add(ASMAPI.buildNumberLdcInsnNode(1.0, ASMAPI.NumberType.FLOAT));
                        newCode.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                            'java/lang/Math', 'min', '(FF)F', false));
                        newCode.add(new VarInsnNode(Opcodes.FSTORE, speedVar));

                        newCode.add(noAffinityLabel);

                        // f *= speed;
                        newCode.add(new VarInsnNode(Opcodes.FLOAD, fVar));
                        newCode.add(new VarInsnNode(Opcodes.FLOAD, speedVar));
                        newCode.add(new InsnNode(Opcodes.FMUL));
                        newCode.add(new VarInsnNode(Opcodes.FSTORE, fVar));

                        node.instructions.insert(ifeq, newCode);
                        break;
                    }
                }
                return node;
            }
        },
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
        },
        'replace_with_equals': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraft/core/HolderOwner',
                'methodName': 'canSerializeIn',
                'methodDesc': '(Lnet/minecraft/core/HolderOwner;)Z'
            },
            'transformer': function (node) {
                node.instructions.clear();
                node.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                node.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                node.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, 'java/lang/Object', 'equals', '(Ljava/lang/Object;)Z', false));
                node.instructions.add(new InsnNode(Opcodes.IRETURN));
                node.maxStack = 2;
                node.maxLocals = 2;
                return node;
            }
        }
    }
}
