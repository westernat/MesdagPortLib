package PortLib.extensions.net.neoforged.neoforge.common.util.AttributeTooltipContext;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.neoforged.neoforge.common.util.AttributeTooltipContext;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.common.util.PortAttributeTooltipContext;

@Extension
public class PortAttributeTooltipContextExtension {
    @Diff
    public static PortAttributeTooltipContext wrap(@This AttributeTooltipContext thiz) {
        return new PortAttributeTooltipContext.Delegate(thiz);
    }
}
