package org.mesdag.portlib.wrapper.server.level;

import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.ChatVisiblity;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.IPortPlayer;

public record PortClientInformation(
        String language,
        int viewDistance,
        ChatVisiblity chatVisibility,
        boolean chatColors,
        int modelCustomisation,
        HumanoidArm mainHand,
        boolean textFilteringEnabled,
        boolean allowsListing
) {
    @Diff
    public static PortClientInformation wrap(ServerboundClientInformationPacket packet) {
        return new PortClientInformation(
                packet.language(),
                packet.viewDistance(),
                packet.chatVisibility(),
                packet.chatColors(),
                packet.modelCustomisation(),
                packet.mainHand(),
                packet.textFilteringEnabled(),
                packet.allowsListing()
        );
    }

    @Diff
    public static PortClientInformation wrap(ServerPlayer player) {
        return new PortClientInformation(
                player.getLanguage(),
                player.server.getPlayerList().getViewDistance(),
                player.getChatVisibility(),
                player.canChatInColor(),
                IPortPlayer.of(player).portlib$getModelCustomisation(),
                player.getMainArm(),
                player.isTextFilteringEnabled(),
                player.allowsListing()
        );
    }
}
