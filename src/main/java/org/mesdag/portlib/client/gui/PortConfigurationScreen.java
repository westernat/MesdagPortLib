package org.mesdag.portlib.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.ModContainer;

// todo
public class PortConfigurationScreen extends OptionsSubScreen {
    public PortConfigurationScreen(ModContainer mod, Screen parent) {
        super(parent, Minecraft.getInstance().options, Component.translatable(""));
    }
}
