package org.mesdag.portlib;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.common.Mod;
import org.mesdag.portlib.attachment.PortAttachmentHolder;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.component.PortDataComponentType;
import org.mesdag.portlib.diff.test.TestAttachment;
import org.mesdag.portlib.diff.test.TestComponent;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.event.entity.player.PortPlayerInteractEvent;
import org.mesdag.portlib.network.PortNetworkHandler;
import org.mesdag.portlib.registries.PortAttachmentRegistration;
import org.mesdag.portlib.registries.PortDataComponentRegistration;
import org.mesdag.portlib.registries.PortRegisterHandler;
import org.mesdag.portlib.wrapper.PortEnvironment;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

@Mod(PortLib.MODID)
public class PortLib {
    public static final String MODID = "portlib";
    public static final Logger LOGGER = LoggerFactory.getLogger("PortLib");

    public PortLib() {
        PortRegisterHandler.init();
        PortNetworkHandler.init();
        PortEventHooks.init();
        if (PortEnvironment.isDeveloper()) {
            PortAttachmentRegistration attachment = PortRegisterHandler.attachment(PortLib.MODID);
            Supplier<PortAttachmentType<TestAttachment>> testAttachment = attachment.registerSimple("test", () -> PortAttachmentType.serializable(() -> new TestAttachment(true)).sync(TestAttachment.STREAM_CODEC).copyOnDeath());

            PortDataComponentRegistration dataComponent = PortRegisterHandler.dataComponent(MODID);
            Supplier<PortDataComponentType<TestComponent>> testDataComponent = dataComponent.register("test", builder -> builder.persistent(TestComponent.CODEC).networkSynchronized(TestComponent.STREAM_CODEC));

            PortEventHandler.addListener((PortPlayerInteractEvent.PortEntityInteract event) -> {
                ItemStack stack = event.getItemStack();
                if (!stack.isEmpty()) {
                    if (!event.getLevel().isClientSide) {
                        TestAttachment data = PortAttachmentHolder.of(event.getTarget()).getData(testAttachment);
                        data.setStack(stack);

                        stack.setData(testDataComponent, new TestComponent(1));
                    }
                    event.setCancellationResult(InteractionResult.SUCCESS);
                }
            });

            PortEventHandler.addListener((PortPlayerInteractEvent.PortRightClickEmpty event) -> {
                Item item = event.getItemStack().getItem();
                item.helloWorld(event.getEntity());
            });
        }
    }

    public static PortIdentifier asResource(String path) {
        return PortIdentifier.fromNamespaceAndPath(MODID, path);
    }
}
