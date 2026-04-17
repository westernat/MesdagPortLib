package org.mesdag.portlib.wrapper;

import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface IPortNBTSerializable<T extends Tag> extends INBTSerializable<T> {}
