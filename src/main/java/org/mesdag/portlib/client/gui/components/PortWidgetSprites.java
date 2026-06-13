package org.mesdag.portlib.client.gui.components;

public record PortWidgetSprites(
        PortSprite enabled,
        PortSprite disabled,
        PortSprite enabledFocused,
        PortSprite disabledFocused
) {
    public PortWidgetSprites(PortSprite normal, PortSprite highlighted) {
        this(normal, normal, highlighted, highlighted);
    }

    public PortWidgetSprites(PortSprite enabled, PortSprite disabled, PortSprite highlighted) {
        this(enabled, disabled, highlighted, disabled);
    }

    public PortSprite get(boolean enabled, boolean focused) {
        if (enabled) {
            return focused ? enabledFocused : this.enabled;
        }
        return focused ? disabledFocused : disabled;
    }
}
