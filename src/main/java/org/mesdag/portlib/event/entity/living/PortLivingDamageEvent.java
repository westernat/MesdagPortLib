package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.wrapper.common.damagesource.PortDamageContainer;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class PortLivingDamageEvent extends LivingEvent {
    private PortLivingDamageEvent(LivingEntity living) {
        super(living);
    }

    public static class PortPre extends PortLivingDamageEvent {
        private final PortDamageContainer container;

        @Diff
        public PortPre(LivingEntity living, PortDamageContainer container) {
            super(living);
            this.container = container;
        }

        public PortDamageContainer getContainer() {
            return container;
        }

        public DamageSource getSource() {
            return container.getSource();
        }

        public float getNewDamage() {
            return container.getNewDamage();
        }

        public float getOriginalDamage() {
            return container.getOriginalDamage();
        }

        public void setNewDamage(float newDamage) {
            container.setNewDamage(newDamage);
        }

        @Diff
        public static float onLivingDamagePre(LivingEntity entity, PortDamageContainer container) {
            return PortEventHandler.postEventWithReturn(new PortPre(entity, container)).getNewDamage();
        }
    }

    public static class PortPost extends PortLivingDamageEvent {
        private final float originalDamage;
        private final DamageSource source;
        private final float newDamage;
        private final float blockedDamage;
        private final float shieldDamage;
        private final int postAttackInvulnerabilityTicks;
        private final EnumMap<PortDamageContainer.PortReduction, Float> reductions;

        @Diff
        public PortPost(LivingEntity entity, PortDamageContainer container) {
            super(entity);
            this.originalDamage = container.getOriginalDamage();
            this.source = container.getSource();
            this.newDamage = container.getNewDamage();
            this.blockedDamage = container.getBlockedDamage();
            this.shieldDamage = container.getShieldDamage();
            this.postAttackInvulnerabilityTicks = container.getPostAttackInvulnerabilityTicks();
            Map<PortDamageContainer.PortReduction, Float> map = Arrays.stream(PortDamageContainer.PortReduction.values())
                    .map(type -> new AbstractMap.SimpleEntry<>(type, container.getReduction(type)))
                    .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
            this.reductions = new EnumMap<>(map);
        }

        public float getOriginalDamage() {
            return originalDamage;
        }

        public DamageSource getSource() {
            return source;
        }

        public float getNewDamage() {
            return newDamage;
        }

        public float getBlockedDamage() {
            return blockedDamage;
        }

        public float getShieldDamage() {
            return shieldDamage;
        }

        public int getPostAttackInvulnerabilityTicks() {
            return postAttackInvulnerabilityTicks;
        }

        public float getReduction(PortDamageContainer.PortReduction reduction) {
            return reductions.get(reduction);
        }

        @Diff
        public static void onLivingDamagePost(LivingEntity entity, PortDamageContainer container) {
            PortEventHandler.postEvent(new PortPost(entity, container));
        }
    }
}
