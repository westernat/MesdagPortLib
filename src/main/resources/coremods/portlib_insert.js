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
                //       float speed = (float) this.getAttributeValue(Attributes.SUBMERGED_MINING_SPEED);
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

                        // float speed = (float) this.getAttributeValue(Attributes.SUBMERGED_MINING_SPEED);
                        newCode.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        newCode.add(new FieldInsnNode(Opcodes.GETSTATIC,
                            'net/minecraft/world/entity/ai/attributes/Attributes',
                            'SUBMERGED_MINING_SPEED',
                            'Lnet/minecraft/core/Holder;'));
                        newCode.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                            'net/minecraft/world/entity/LivingEntity',
                            ASMAPI.mapMethod('m_246858_'), // getAttributeValue
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
                var insns = node.instructions

                // Skip if this method has already been modified.
                for (var j = 0; j < insns.size(); j++) {
                    var insn = insns.get(j)
                    if (insn instanceof MethodInsnNode && insn.getOpcode() == Opcodes.INVOKEVIRTUAL &&
                        insn.owner === 'org/mesdag/portlib/wrapper/common/damagesource/PortDamageContainer' &&
                        insn.name === 'setReduction') {
                        return node
                    }
                }

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
                var insns = node.instructions

                // Skip if this method has already been modified.
                for (var i = 0; i < insns.size(); i++) {
                    var insn = insns.get(i)
                    if (insn instanceof MethodInsnNode && insn.getOpcode() == Opcodes.INVOKESTATIC &&
                        insn.owner === 'org/mesdag/portlib/event/entity/player/PortPlayerEvent$HarvestCheck' &&
                        insn.name === 'doPlayerHarvestCheck') {
                        return node
                    }
                }

                function prevReal(from) {
                    for (var j = from - 1; j >= 0; j--) {
                        if (insns.get(j).getOpcode() >= 0) return j
                    }
                    return -1
                }

                // Transform:
                //   return ForgeHooks.isCorrectToolForDrops(state, player);
                // Into:
                //   boolean success = /* original return value */;
                //   return PortPlayerEvent.HarvestCheck.doPlayerHarvestCheck(player, state, level, pos, success);
                //
                // Locate the pattern IRETURN <- INVOKESTATIC ForgeHooks.isCorrectToolForDrops <-
                // ALOAD player(4) <- ALOAD state(1) instead of relying on a fixed instruction count.
                for (var k = 0; k < insns.size(); k++) {
                    var iret = insns.get(k)
                    if (!(iret instanceof InsnNode && iret.getOpcode() == Opcodes.IRETURN)) continue

                    var r1 = prevReal(k)
                    if (r1 < 0) continue
                    var call = insns.get(r1)
                    if (!(call instanceof MethodInsnNode && call.getOpcode() == Opcodes.INVOKESTATIC &&
                        call.owner === 'net/minecraftforge/common/ForgeHooks' &&
                        call.name === 'isCorrectToolForDrops' &&
                        call.desc === '(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/player/Player;)Z')) continue

                    var r2 = prevReal(r1)
                    if (r2 < 0) continue
                    var aloadPlayer = insns.get(r2)
                    if (!(aloadPlayer instanceof VarInsnNode && aloadPlayer.getOpcode() == Opcodes.ALOAD && aloadPlayer.var == 4)) continue

                    var r3 = prevReal(r2)
                    if (r3 < 0) continue
                    var aloadState = insns.get(r3)
                    if (!(aloadState instanceof VarInsnNode && aloadState.getOpcode() == Opcodes.ALOAD && aloadState.var == 1)) continue

                    var successVar = node.maxLocals
                    node.maxLocals = successVar + 1
                    if (node.maxStack < 5) node.maxStack = 5

                    var list = new InsnList()
                    // boolean success = /* original return value */;
                    list.add(new VarInsnNode(Opcodes.ISTORE, successVar))
                    // return PortPlayerEvent.HarvestCheck.doPlayerHarvestCheck(player, state, level, pos, success);
                    list.add(new VarInsnNode(Opcodes.ALOAD, 4)) // player
                    list.add(new VarInsnNode(Opcodes.ALOAD, 1)) // state
                    list.add(new VarInsnNode(Opcodes.ALOAD, 2)) // level
                    list.add(new VarInsnNode(Opcodes.ALOAD, 3)) // pos
                    list.add(new VarInsnNode(Opcodes.ILOAD, successVar)) // success
                    list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, 'org/mesdag/portlib/event/entity/player/PortPlayerEvent$HarvestCheck', 'doPlayerHarvestCheck', '(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Z)Z', false))
                    node.instructions.insertBefore(iret, list)
                    break
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
        },
        'with_tool': {
            'target': {
                'type': 'METHOD',
                'class': 'net/minecraftforge/common/extensions/IForgeItem',
                'methodName': 'isCorrectToolForDrops',
                'methodDesc': '(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/block/state/BlockState;)Z'
            },
            'transformer': function (node) {
                // Make sure we only apply this once.
                var insns = node.instructions;
                for (var i = 0; i < insns.size(); i++) {
                    var check = insns.get(i);
                    if (check instanceof MethodInsnNode && check.getOpcode() == Opcodes.INVOKESTATIC &&
                        check.owner === 'org/mesdag/portlib/diff/IPortItem' && check.name === 'isCorrectToolForDrops') {
                        return node;
                    }
                }

                // Insert at the head of the method:
                //   PortTriState triState = IPortItem.isCorrectToolForDrops(stack, state);
                //   if (!triState.isDefault()) return triState.isTrue();
                if (insns.size() == 0) return node;
                var triStateVar = node.maxLocals;
                node.maxLocals = triStateVar + 1;

                var originalBody = new LabelNode();
                var list = new InsnList();
                // triState = IPortItem.isCorrectToolForDrops(stack, state);
                list.add(new VarInsnNode(Opcodes.ALOAD, 1)); // stack
                list.add(new VarInsnNode(Opcodes.ALOAD, 2)); // state
                list.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                    'org/mesdag/portlib/diff/IPortItem',
                    'isCorrectToolForDrops',
                    '(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/block/state/BlockState;)Lorg/mesdag/portlib/wrapper/common/util/PortTriState;', true));
                list.add(new VarInsnNode(Opcodes.ASTORE, triStateVar));
                // if (triState.isDefault()) -> run original body
                list.add(new VarInsnNode(Opcodes.ALOAD, triStateVar));
                list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                    'org/mesdag/portlib/wrapper/common/util/PortTriState', 'isDefault', '()Z', false));
                list.add(new JumpInsnNode(Opcodes.IFNE, originalBody));
                // return triState.isTrue();
                list.add(new VarInsnNode(Opcodes.ALOAD, triStateVar));
                list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                    'org/mesdag/portlib/wrapper/common/util/PortTriState', 'isTrue', '()Z', false));
                list.add(new InsnNode(Opcodes.IRETURN));
                list.add(originalBody);

                node.instructions.insertBefore(node.instructions.getFirst(), list);
                return node;
            }
        }
    }
}
