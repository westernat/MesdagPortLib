package PortLib.extensions.net.minecraft.advancements.Advancement;

import net.minecraft.network.chat.Component;
import org.mesdag.portlib.wrapper.advancements.PortAdvancementHolder;

public class PortAdvancementExtension {
    public static Component name(PortAdvancementHolder advancement) {
        return advancement.value().getChatComponent();
    }
}
