package org.mesdag.portlib.event.client;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenshotEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import javax.annotation.Nullable;
import java.io.File;

public class PortScreenshotEvent extends PortEvent<ScreenshotEvent> {
    @Diff
    public PortScreenshotEvent(ScreenshotEvent e) {
        super(e);
    }

    public NativeImage getImage() {
        return e.getImage();
    }

    public File getScreenshotFile() {
        return e.getScreenshotFile();
    }

    public void setScreenshotFile(File screenshotFile) {
        e.setScreenshotFile(screenshotFile);
    }

    @Nullable
    public Component getResultMessage() {
        return e.getResultMessage();
    }

    public void setResultMessage(@Nullable Component resultMessage) {
        e.setResultMessage(resultMessage);
    }

    public Component getCancelMessage() {
        return e.getCancelMessage();
    }

    public void setCanceled(boolean canceled) {
        e.setCanceled(canceled);
    }

    public boolean isCanceled() {
        return e.isCanceled();
    }

    static {
        PortEventHooks.register();
    }
}
