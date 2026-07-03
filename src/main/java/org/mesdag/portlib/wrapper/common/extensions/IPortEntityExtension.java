package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.world.entity.Entity.PortEntityExtension;
import com.google.common.base.Supplier;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.diff.IPortEntity;
import org.mesdag.portlib.diff.IPortEntityDimensions;
import org.mesdag.portlib.diff.PortAdvancedAddEntityPayload;
import org.mesdag.portlib.registries.PortRegistryEntry;
import org.mesdag.portlib.util.Final;
import org.mesdag.portlib.util.Protected;
import org.mesdag.portlib.wrapper.common.PortTags;
import org.mesdag.portlib.wrapper.entity.IPortEntityWithComplexSpawn;
import org.mesdag.portlib.wrapper.world.entity.PortEntityAttachment;
import org.mesdag.portlib.wrapper.world.entity.PortEntityAttachments;
import org.mesdag.portlib.wrapper.world.entity.projectile.PortProjectileDeflection;

import java.util.Optional;
import java.util.function.Consumer;

public interface IPortEntityExtension {
    private Entity self() {
        return (Entity) this;
    }

    // region Attachment

    default void copyAttachmentsFrom(Entity other, boolean isDeath) {
        PortEntityExtension.copyAttachmentsFrom(self(), other, isDeath);
    }

    default boolean hasAttachments() {
        return PortEntityExtension.hasAttaches(self());
    }

    default boolean hasData(PortAttachmentType<?> type) {
        return PortEntityExtension.hasAttach(self(), type);
    }

    default <T> boolean hasData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortEntityExtension.hasAttach(self(), type);
    }

    default <T> T getData(PortAttachmentType<T> type) {
        return PortEntityExtension.getAttach(self(), type);
    }

    default <T> T getData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortEntityExtension.getAttach(self(), type);
    }

    default <T> Optional<T> getExistingData(PortAttachmentType<T> type) {
        return PortEntityExtension.getExistingAttach(self(), type);
    }

    default <T> Optional<T> getExistingData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortEntityExtension.getExistingAttach(self(), type);
    }

    default <T> @Nullable T getExistingDataOrNull(PortAttachmentType<T> type) {
        return PortEntityExtension.getExistingAttachOrNull(self(), type);
    }

    default <T> @Nullable T getExistingDataOrNull(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortEntityExtension.getExistingAttachOrNull(self(), type);
    }

    @MustBeInvokedByOverriders
    default <T> @Nullable T setData(PortAttachmentType<T> type, T data) {
        return PortEntityExtension.setAttach(self(), type, data);
    }

    @MustBeInvokedByOverriders
    default <T> @Nullable T setData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type, T data) {
        return PortEntityExtension.setAttach(self(), type, data);
    }

    @MustBeInvokedByOverriders
    default <T> @Nullable T removeData(PortAttachmentType<T> type) {
        return PortEntityExtension.removeAttach(self(), type);
    }

    @MustBeInvokedByOverriders
    default <T> @Nullable T removeData(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortEntityExtension.removeAttach(self(), type);
    }

    default void syncData(PortAttachmentType<?> type) {
        PortEntityExtension.syncAttach(self(), type);
    }

    default void syncData(Supplier<PortAttachmentType<?>> type) {
        PortEntityExtension.syncAttach(self(), type);
    }

    // endregion Attachment

    default RegistryAccess registryAccess() {
        return PortEntityExtension.registryAccess(self());
    }

    default void igniteForTicks(int ticks) {
        PortEntityExtension.igniteForTicks(self(), ticks);
    }

    default void igniteForSeconds(float seconds) {
        igniteForTicks((int) (seconds * 20));
    }

    default Vec3 getKnownMovement() {
        return PortEntityExtension.getKnownMovement(self());
    }

    default BlockState getInBlockState() {
        return PortEntityExtension.getInBlockState(self());
    }

    default RandomSource getRandom() {
        return PortEntityExtension.getRandom(self());
    }

    @Protected
    default double getDefaultGravity() {
        return 0.0;
    }

    @Final
    default double getGravity1211() {
        return self().isNoGravity() ? 0.0 : getDefaultGravity();
    }

    default void applyGravity() {
        double d0 = getGravity1211();
        if (d0 != 0.0) {
            self().setDeltaMovement(self().getDeltaMovement().add(0.0, -d0, 0.0));
        }
    }

    default PortProjectileDeflection deflection(Projectile projectile) {
        return self().getType().is(PortTags.EntityTypes.DEFLECTS_PROJECTILES)
                ? PortProjectileDeflection.REVERSE : PortProjectileDeflection.NONE;
    }

    default @Nullable ItemStack getWeaponItem() {
        return null;
    }

    default void sendPairingData(ServerPlayer player, Consumer<Packet<ClientGamePacketListener>> bundleBuilder) {
        if (this instanceof IPortEntityWithComplexSpawn) {
            bundleBuilder.accept(PortLib.NETWORK_HANDLER.toVanillaClientbound(new PortAdvancedAddEntityPayload(self())));
        }
    }

    @Protected
    default void positionRider1211(Entity passenger, Entity.MoveFunction callback) {
        if (self().hasPassenger(passenger)) {
            Vec3 vec3 = getPassengerRidingPosition(passenger);
            Vec3 vec31 = IPortEntityExtension.of(passenger).getVehicleAttachmentPoint(self());
            callback.accept(passenger, vec3.x - vec31.x, vec3.y - vec31.y, vec3.z - vec31.z);
        }
    }

    @Final
    default PortEntityAttachments getAttachments() {
        return IPortEntity.of(self()).portlib$getAttachments();
    }

    default Vec3 getVehicleAttachmentPoint(Entity entity) {
        return getAttachments().get(PortEntityAttachment.VEHICLE, 0, self().getYRot());
    }

    default Vec3 getPassengerRidingPosition(Entity entity) {
        return self().position().add(getPassengerAttachmentPoint(entity, self().dimensions, 1.0F));
    }

    @Protected
    default Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float partialTick) {
        return getDefaultPassengerAttachmentPoint(self(), entity, IPortEntityDimensions.of(dimensions).attachments());
    }

    @Protected
    static Vec3 getDefaultPassengerAttachmentPoint(Entity vehicle, Entity passenger, PortEntityAttachments attachments) {
        int i = vehicle.getPassengers().indexOf(passenger);
        return attachments.getClamped(PortEntityAttachment.PASSENGER, i, vehicle.getYRot());
    }

    static IPortEntityExtension of(Entity entity) {
        return (IPortEntityExtension) entity;
    }

    @ApiStatus.Internal
    static void init() {
        PortLib.NETWORK_HANDLER.registerInGameS2C(
                PortAdvancedAddEntityPayload.class,
                PortAdvancedAddEntityPayload.ID,
                PortAdvancedAddEntityPayload.STREAM_CODEC,
                PortAdvancedAddEntityPayload::handle
        );
    }

    default void onExplosionHit(Entity source) {}
}
