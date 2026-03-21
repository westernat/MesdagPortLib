package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.common.damagesource.PortDamageContainer;

public abstract class PortLivingDamageEvent extends PortLivingEvent {
    private PortLivingDamageEvent(LivingEntity entity) {
        super(entity);
    }

    public static class PortPre extends PortLivingDamageEvent {
        private final LivingDamageEvent.Pre e;

        @Diff
        public PortPre(LivingDamageEvent.Pre e) {
            super(e.getEntity());
            this.e = e;
        }

        public PortDamageContainer getContainer() {
            return PortDamageContainer.wrap(e.getContainer());
        }

        public DamageSource getSource() {
            return e.getSource();
        }

        public float getNewDamage() {
            return e.getNewDamage();
        }

        public float getOriginalDamage() {
            return e.getOriginalDamage();
        }

        public void setNewDamage(float newDamage) {
            e.setNewDamage(newDamage);
        }

        static {
            PortEventHooks.register(LivingDamageEvent.Pre.class, PortPre.class, PortPre::new);
        }
    }

    public static class PortPost extends PortLivingDamageEvent {
        private final LivingDamageEvent.Post e;

        @Diff
        public PortPost(LivingDamageEvent.Post e) {
            super(e.getEntity());
            this.e = e;
        }

        public float getOriginalDamage() {
            return e.getOriginalDamage();
        }

        public DamageSource getSource() {
            return e.getSource();
        }

        public float getNewDamage() {
            return e.getNewDamage();
        }

        public float getBlockedDamage() {
            return e.getBlockedDamage();
        }

        public float getShieldDamage() {
            return e.getShieldDamage();
        }

        public int getPostAttackInvulnerabilityTicks() {
            return e.getPostAttackInvulnerabilityTicks();
        }

        public float getReduction(PortDamageContainer.PortReduction reduction) {
            return e.getReduction(reduction.unwrap());
        }

        static {
            PortEventHooks.register(LivingDamageEvent.Post.class, PortPost.class, PortPost::new);
        }
    }
}
