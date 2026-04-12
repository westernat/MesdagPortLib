package org.mesdag.portlib;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import org.mesdag.portlib.attachment.PortAttachmentHolder;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.component.PortDataComponentHolder;
import org.mesdag.portlib.component.PortDataComponentType;
import org.mesdag.portlib.diff.Diff;
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
import org.mesdag.portlib.registries.PortAttachmentRegistration;
import org.mesdag.portlib.registries.PortDataComponentRegistration;
import org.mesdag.portlib.registries.PortRegisterHandler;
import org.mesdag.portlib.wrapper.PortEnvironment;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

@SuppressWarnings("all")
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
        PortEventHandler.addListener((RegisterCapabilitiesEvent event) -> {
//            ForgeChunkManager
            PortDataMapLoader.initDataMaps();
            PortModifyDefaultComponentsEvent.modifyComponents();
//            extendPoiTypes
        });
        if (PortEnvironment.isDeveloper()) {
            PortAttachmentRegistration attachment = PortRegisterHandler.attachment(MODID);
            Supplier<PortAttachmentType<TestAttachment>> testAttachment = attachment.registerTyped("test", () -> PortAttachmentType.serializable(() -> new TestAttachment(true)).sync(TestAttachment.STREAM_CODEC).copyOnDeath());

            PortDataComponentRegistration dataComponent = PortRegisterHandler.dataComponent(MODID);
            Supplier<PortDataComponentType<TestComponent>> testDataComponent = dataComponent.registerTyped("test", builder -> builder.persistent(TestComponent.CODEC).networkSynchronized(TestComponent.STREAM_CODEC));

            PortEventHandler.addListener((PlayerInteractEvent.EntityInteract event) -> {
                ItemStack stack = event.getItemStack();
                if (!stack.isEmpty()) {
                    if (!event.getLevel().isClientSide) {
                        TestAttachment data = PortAttachmentHolder.of(event.getTarget()).getData(testAttachment);
                        data.setStack(stack);

                        PortDataComponentHolder.of(stack).set(testDataComponent, new TestComponent(1));
                    }
                    event.setCancellationResult(InteractionResult.SUCCESS);
                }
            });
        }
    }

    public static PortIdentifier asResource(String path) {
        return PortIdentifier.fromNamespaceAndPath(MODID, path);
    }
}
