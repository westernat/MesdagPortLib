package PortLib.extensions.net.minecraft.world.level.block.state.properties.BlockSetType;

import com.google.common.base.Suppliers;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.wrapper.sounds.PortSoundEvents;

import java.util.function.Supplier;

public class PortBlockSetTypeExtension {
    private static final Supplier<BlockSetType> COPPER = Suppliers.memoize(() -> {
        SoundEvent doorClose;
        if (PortSoundEvents.COPPER_DOOR_CLOSE.isPresent()) {
            doorClose = PortSoundEvents.COPPER_DOOR_CLOSE.get();
        } else {
            doorClose = SoundEvent.createVariableRangeEvent(PortLib.asResource("block.copper_door.close"));
        }
        SoundEvent doorOpen;
        if (PortSoundEvents.COPPER_DOOR_OPEN.isPresent()) {
            doorOpen = PortSoundEvents.COPPER_DOOR_OPEN.get();
        } else {
            doorOpen = SoundEvent.createVariableRangeEvent(PortLib.asResource("block.copper_door.open"));
        }
        SoundEvent trapdoorClose;
        if (PortSoundEvents.COPPER_TRAPDOOR_CLOSE.isPresent()) {
            trapdoorClose = PortSoundEvents.COPPER_TRAPDOOR_CLOSE.get();
        } else {
            trapdoorClose = SoundEvent.createVariableRangeEvent(PortLib.asResource("block.copper_trapdoor.close"));
        }
        SoundEvent trapdoorOpen;
        if (PortSoundEvents.COPPER_TRAPDOOR_OPEN.isPresent()) {
            trapdoorOpen = PortSoundEvents.COPPER_TRAPDOOR_OPEN.get();
        } else {
            trapdoorOpen = SoundEvent.createVariableRangeEvent(PortLib.asResource("block.copper_trapdoor.open"));
        }
        return new BlockSetType(
                "copper",
                true,
                SoundType.COPPER,
                doorClose,
                doorOpen,
                trapdoorClose,
                trapdoorOpen,
                SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF,
                SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON,
                SoundEvents.STONE_BUTTON_CLICK_OFF,
                SoundEvents.STONE_BUTTON_CLICK_ON
        );
    });

    public static BlockSetType copper() {
        return COPPER.get();
    }
}
