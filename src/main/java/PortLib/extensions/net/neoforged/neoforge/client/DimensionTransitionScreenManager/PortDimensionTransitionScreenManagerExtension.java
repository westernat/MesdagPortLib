package PortLib.extensions.net.neoforged.neoforge.client.DimensionTransitionScreenManager;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.neoforged.neoforge.client.DimensionTransitionScreenManager;
import org.mesdag.portlib.diff.PortRegisterDimensionTransitionScreenEvent;

@Extension
public class PortDimensionTransitionScreenManagerExtension {
    public static PortRegisterDimensionTransitionScreenEvent.PortReceivingLevelScreenFactory wrap(@This DimensionTransitionScreenManager.ReceivingLevelScreenFactory thiz) {
        return new PortRegisterDimensionTransitionScreenEvent.PortReceivingLevelScreenFactory.Delegate(thiz);
    }
}
