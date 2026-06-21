package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.world.entity.Entity.PortEntityExtension;
import com.google.common.base.Supplier;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.diff.PortAdvancedAddEntityPayload;
import org.mesdag.portlib.registries.PortRegistryEntry;
import org.mesdag.portlib.util.Final;
import org.mesdag.portlib.util.Protected;
import org.mesdag.portlib.wrapper.common.PortTags;
import org.mesdag.portlib.wrapper.entity.IPortEntityWithComplexSpawn;
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

    default boolean hasAttachment(PortAttachmentType<?> type) {
        return PortEntityExtension.hasAttach(self(), type);
    }

    default <T> boolean hasAttachment(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortEntityExtension.hasAttach(self(), type);
    }

    default <T> T getAttachment(PortAttachmentType<T> type) {
        return PortEntityExtension.getAttach(self(), type);
    }

    default <T> T getAttachment(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortEntityExtension.getAttach(self(), type);
    }

    default <T> Optional<T> getExistingAttachment(PortAttachmentType<T> type) {
        return PortEntityExtension.getExistingAttach(self(), type);
    }

    default <T> Optional<T> getExistingAttachment(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortEntityExtension.getExistingAttach(self(), type);
    }

    default <T> @Nullable T getExistingAttachmentOrNull(PortAttachmentType<T> type) {
        return PortEntityExtension.getExistingAttachOrNull(self(), type);
    }

    default <T> @Nullable T getExistingAttachmentOrNull(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortEntityExtension.getExistingAttachOrNull(self(), type);
    }

    default <T> @Nullable T setAttachment(PortAttachmentType<T> type, T data) {
        return PortEntityExtension.setAttach(self(), type, data);
    }

    default <T> @Nullable T setAttachment(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type, T data) {
        return PortEntityExtension.setAttach(self(), type, data);
    }

    default <T> @Nullable T removeAttachment(PortAttachmentType<T> type) {
        return PortEntityExtension.removeAttach(self(), type);
    }

    default <T> @Nullable T removeAttachment(PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<T>> type) {
        return PortEntityExtension.removeAttach(self(), type);
    }

    default void syncAttachment(PortAttachmentType<?> type) {
        PortEntityExtension.syncAttach(self(), type);
    }

    default void syncAttachment(Supplier<PortAttachmentType<?>> type) {
        PortEntityExtension.syncAttach(self(), type);
    }

    // endregion Attachment

    default RegistryAccess registryAccess() {
        return PortEntityExtension.registryAccess(self());
    }

    default void igniteForTicks(int ticks) {
        PortEntityExtension.igniteForTicks(self(), ticks);
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
    default double getGravity() {
        return self().isNoGravity() ? 0.0 : getDefaultGravity();
    }

    default void applyGravity() {
        double d0 = getGravity();
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
}
