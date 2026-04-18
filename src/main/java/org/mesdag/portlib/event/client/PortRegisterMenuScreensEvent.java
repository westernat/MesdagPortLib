package org.mesdag.portlib.event.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRegisterMenuScreensEvent extends PortEvent<RegisterMenuScreensEvent> {
    @Diff
    public PortRegisterMenuScreensEvent(RegisterMenuScreensEvent e) {
        super(e);
    }

    public <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void register(MenuType<? extends M> menuType, MenuScreens.ScreenConstructor<M, U> screenConstructor) {
        e.register(menuType, screenConstructor);
    }

    static {
        PortEventHooks.register();
    }
}
