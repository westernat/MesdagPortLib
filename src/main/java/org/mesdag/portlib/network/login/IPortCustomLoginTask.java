package org.mesdag.portlib.network.login;

import net.minecraft.network.chat.Component;
import org.mesdag.portlib.network.IPortPacket;

import java.util.function.Consumer;

public interface IPortCustomLoginTask {
    Consumer<IPortPacket> sender();

    void disconnect(Component reason);
}
