package org.mesdag.portlib.wrapper.server.level;

import net.minecraft.server.level.ClientInformation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.ChatVisiblity;
import org.mesdag.portlib.diff.Diff;

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
    public ClientInformation unwrap() {
        return new ClientInformation(
                language,
                viewDistance,
                chatVisibility,
                chatColors,
                modelCustomisation,
                mainHand,
                textFilteringEnabled,
                allowsListing
        );
    }

    @Diff
    public static PortClientInformation wrap(ClientInformation information) {
        return new PortClientInformation(
                information.language(),
                information.viewDistance(),
                information.chatVisibility(),
                information.chatColors(),
                information.modelCustomisation(),
                information.mainHand(),
                information.textFilteringEnabled(),
                information.allowsListing()
        );
    }
}
