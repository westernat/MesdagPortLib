package org.mesdag.portlib.diff;

import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.DimensionTransitionScreenManager;
import net.neoforged.neoforge.client.event.RegisterDimensionTransitionScreenEvent;
import org.mesdag.portlib.event.IPortModBusEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.function.BooleanSupplier;

@Diff
public class PortRegisterDimensionTransitionScreenEvent extends PortEvent<RegisterDimensionTransitionScreenEvent> implements IPortModBusEvent {
    @Diff
    public PortRegisterDimensionTransitionScreenEvent(RegisterDimensionTransitionScreenEvent e) {
        super(e);
    }

    public boolean registerIncomingEffect(ResourceKey<Level> dimension, PortReceivingLevelScreenFactory screen) {
        return e.registerIncomingEffect(dimension, screen.unwrap());
    }

    public boolean registerOutgoingEffect(ResourceKey<Level> dimension, PortReceivingLevelScreenFactory screen) {
        return e.registerIncomingEffect(dimension, screen.unwrap());
    }

    public boolean registerConditionalEffect(ResourceKey<Level> toDimension, ResourceKey<Level> fromDimension, PortReceivingLevelScreenFactory screen) {
        return e.registerConditionalEffect(toDimension, fromDimension, screen.unwrap());
    }

    static {
        PortEventHooks.register();
    }

    @FunctionalInterface
    public interface PortReceivingLevelScreenFactory {
        ReceivingLevelScreen create(BooleanSupplier supplier, ReceivingLevelScreen.Reason reason);

        @Diff
        default DimensionTransitionScreenManager.ReceivingLevelScreenFactory unwrap() {
            return this::create;
        }

        @Diff
        record Delegate(DimensionTransitionScreenManager.ReceivingLevelScreenFactory delegate) implements PortReceivingLevelScreenFactory {
            @Override
            public ReceivingLevelScreen create(BooleanSupplier supplier, ReceivingLevelScreen.Reason reason) {
                return delegate.create(supplier, reason);
            }

            @Override
            public DimensionTransitionScreenManager.ReceivingLevelScreenFactory unwrap() {
                return delegate;
            }
        }
    }
}
