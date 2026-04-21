package org.mesdag.portlib;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.component.PortDataComponentType;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.IPortLivingEntity;
import org.mesdag.portlib.diff.PortRegistries;
import org.mesdag.portlib.diff.attachment.PortAttachmentInternals;
import org.mesdag.portlib.diff.attachment.PortAttachmentSync;
import org.mesdag.portlib.diff.datamap.PortDataMapLoader;
import org.mesdag.portlib.diff.test.TestAttachment;
import org.mesdag.portlib.diff.test.TestComponent;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.event.other.PortModifyDefaultComponentsEvent;
import org.mesdag.portlib.network.PortNetworkHandler;
import org.mesdag.portlib.registries.*;
import org.mesdag.portlib.wrapper.PortEnvironment;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;
import org.mesdag.portlib.wrapper.world.effect.PortMobEffect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(PortLib.MODID)
public class PortLib {
    public static final String MODID = "portlib";
    public static final Logger LOGGER = LoggerFactory.getLogger("PortLib");
    @Diff
    public static final PortNetworkHandler NETWORK_HANDLER = new PortNetworkHandler(MODID, "1");

    public PortLib() {
        PortRegisterHandler.init();
        PortRegistries.init();
        PortAttachmentSync.init();
        PortEventHooks.init();
        PortAttachmentInternals.init();
        PortDataMapLoader.init();
        PortNetworkHandler.init();
        IPortLivingEntity.init();
        PortEventHandler.addListener((RegisterCapabilitiesEvent event) -> {
//            ForgeChunkManager
            PortDataMapLoader.initDataMaps();
            PortModifyDefaultComponentsEvent.modifyComponents();
//            extendPoiTypes
        });
        if (PortEnvironment.isDeveloper()) {
            PortAttachmentRegistration attachment = PortRegisterHandler.attachment(MODID);
            PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<TestAttachment>> testAttachment = attachment.registerSimple("test", () -> PortAttachmentType.serializable(() -> new TestAttachment(true)).sync(TestAttachment.STREAM_CODEC).copyOnDeath());

            PortDataComponentRegistration dataComponent = PortRegisterHandler.dataComponent(MODID);
            PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<TestComponent>> testDataComponent = dataComponent.register("test", builder -> builder.persistent(TestComponent.CODEC).networkSynchronized(TestComponent.STREAM_CODEC));

            PortEventHandler.addListener((PlayerInteractEvent.EntityInteract event) -> {
                ItemStack stack = event.getItemStack();
                if (!stack.isEmpty()) {
                    if (!event.getLevel().isClientSide) {
                        TestAttachment data = event.getTarget().getAttach(testAttachment::get);
                        data.setStack(stack);

                        stack.setData(testDataComponent, new TestComponent(1));
                    }
                    event.setCancellationResult(InteractionResult.SUCCESS);
                }
            });

            PortRegistration<MobEffect> effects = PortRegisterHandler.create(MODID, Registries.MOB_EFFECT);
            effects.register("test", () -> new PortMobEffect(MobEffectCategory.BENEFICIAL, 0xFF0000, ParticleTypes.EXPLOSION));
        }
    }

    public static PortIdentifier asResource(String path) {
        return PortIdentifier.fromNamespaceAndPath(MODID, path);
    }
}
