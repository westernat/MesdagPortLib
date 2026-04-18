package org.mesdag.portlib.event.client;

import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.eventbus.api.Event;
import org.mesdag.portlib.diff.Diff;

import java.util.ArrayList;
import java.util.List;

public class PortGatherEffectScreenTooltipsEvent extends Event {
    protected final EffectRenderingInventoryScreen<?> screen;
    protected final MobEffectInstance effectInst;
    protected final List<Component> originalTooltip;
    protected List<Component> tooltip;

    @Diff
    public PortGatherEffectScreenTooltipsEvent(EffectRenderingInventoryScreen<?> screen, MobEffectInstance effectInst, List<Component> tooltip) {
        this.screen = screen;
        this.effectInst = effectInst;
        this.originalTooltip = tooltip;
    }

    public EffectRenderingInventoryScreen<?> getScreen() {
        return screen;
    }

    public MobEffectInstance getEffectInstance() {
        return effectInst;
    }

    public List<Component> getTooltip() {
        if (tooltip == null) {
            this.tooltip = new ArrayList<>(originalTooltip);
        }
        return tooltip;
    }
}
