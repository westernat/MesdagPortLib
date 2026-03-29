package org.mesdag.portlib.event.other;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.PlayLevelSoundEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.sounds.SoundEventHolder;

public class PortPlayLevelSoundEvent<E extends PlayLevelSoundEvent> extends PortEvent<E> implements IPortCancellableEvent {
    @Diff
    public PortPlayLevelSoundEvent(E e) {
        super(e);
    }

    public Level getLevel() {
        return e.getLevel();
    }

    public @Nullable SoundEventHolder getSound() {
        return SoundEventHolder.wrap(e.getSound());
    }

    public void setSound(@Nullable SoundEventHolder sound) {
        e.setSound(sound == null ? null : sound.delegate());
    }

    public SoundSource getSource() {
        return e.getSource();
    }

    public void setSource(SoundSource source) {
        e.setSource(source);
    }

    public float getOriginalVolume() {
        return e.getOriginalVolume();
    }

    public float getOriginalPitch() {
        return e.getOriginalPitch();
    }

    public float getNewVolume() {
        return e.getNewVolume();
    }

    public void setNewVolume(float newVolume) {
        e.setNewVolume(newVolume);
    }

    public float getNewPitch() {
        return e.getNewPitch();
    }

    public void setNewPitch(float newPitch) {
        e.setNewPitch(newPitch);
    }

    public static class PortAtEntity extends PortPlayLevelSoundEvent<PlayLevelSoundEvent.AtEntity> {
        @Diff
        public PortAtEntity(PlayLevelSoundEvent.AtEntity e) {
            super(e);
        }

        public Entity getEntity() {
            return e.getEntity();
        }

        static {
            PortEventHooks.register(PlayLevelSoundEvent.AtEntity.class, PortAtEntity.class, PortAtEntity::new);
        }
    }

    public static class PortAtPosition extends PortPlayLevelSoundEvent<PlayLevelSoundEvent.AtPosition> {
        @Diff
        public PortAtPosition(PlayLevelSoundEvent.AtPosition e) {
            super(e);
        }

        public Vec3 getPosition() {
            return e.getPosition();
        }

        static {
            PortEventHooks.register(PlayLevelSoundEvent.AtPosition.class, PortAtPosition.class, PortAtPosition::new);
        }
    }
}
