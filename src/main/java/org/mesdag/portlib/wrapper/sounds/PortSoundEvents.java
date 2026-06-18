package org.mesdag.portlib.wrapper.sounds;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.ApiStatus;
import org.mesdag.portlib.PortLib;

public class PortSoundEvents {
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, PortLib.MODID);

    public static final RegistryObject<SoundEvent> TUFF_BRICKS_BREAK = register("block.tuff_bricks.break");
    public static final RegistryObject<SoundEvent> TUFF_BRICKS_FALL = register("block.tuff_bricks.fall");
    public static final RegistryObject<SoundEvent> TUFF_BRICKS_HIT = register("block.tuff_bricks.hit");
    public static final RegistryObject<SoundEvent> TUFF_BRICKS_PLACE = register("block.tuff_bricks.place");
    public static final RegistryObject<SoundEvent> TUFF_BRICKS_STEP = register("block.tuff_bricks.step");

    public static final RegistryObject<SoundEvent> WET_SPONGE_BREAK = register("block.wet_sponge.break");
    public static final RegistryObject<SoundEvent> WET_SPONGE_DRIES = register("block.wet_sponge.dries");
    public static final RegistryObject<SoundEvent> WET_SPONGE_FALL = register("block.wet_sponge.fall");
    public static final RegistryObject<SoundEvent> WET_SPONGE_HIT = register("block.wet_sponge.hit");
    public static final RegistryObject<SoundEvent> WET_SPONGE_PLACE = register("block.wet_sponge.place");
    public static final RegistryObject<SoundEvent> WET_SPONGE_STEP = register("block.wet_sponge.step");

    public static final RegistryObject<SoundEvent> COPPER_BULB_BREAK = register("block.copper_bulb.break");
    public static final RegistryObject<SoundEvent> COPPER_BULB_STEP = register("block.copper_bulb.step");
    public static final RegistryObject<SoundEvent> COPPER_BULB_PLACE = register("block.copper_bulb.place");
    public static final RegistryObject<SoundEvent> COPPER_BULB_HIT = register("block.copper_bulb.hit");
    public static final RegistryObject<SoundEvent> COPPER_BULB_FALL = register("block.copper_bulb.fall");
    public static final RegistryObject<SoundEvent> COPPER_BULB_TURN_ON = register("block.copper_bulb.turn_on");
    public static final RegistryObject<SoundEvent> COPPER_BULB_TURN_OFF = register("block.copper_bulb.turn_off");

    public static final RegistryObject<SoundEvent> COPPER_DOOR_CLOSE = register("block.copper_door.close");
    public static final RegistryObject<SoundEvent> COPPER_DOOR_OPEN = register("block.copper_door.open");
    public static final RegistryObject<SoundEvent> COPPER_TRAPDOOR_CLOSE = register("block.copper_trapdoor.close");
    public static final RegistryObject<SoundEvent> COPPER_TRAPDOOR_OPEN = register("block.copper_trapdoor.open");

    private static RegistryObject<SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(PortLib.asResource(name)));
    }

    @ApiStatus.Internal
    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
