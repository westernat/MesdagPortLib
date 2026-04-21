package PortLib.extensions.net.neoforged.neoforge.common.damagesource.IReductionFunction;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.neoforged.neoforge.common.damagesource.IReductionFunction;
import org.mesdag.portlib.wrapper.common.damagesource.IPortReductionFunction;

@Extension
public class PortIReductionFunctionExtension {
    public static IPortReductionFunction wrap(@This IReductionFunction thiz) {
        return new IPortReductionFunction.Delegate(thiz);
    }
}
