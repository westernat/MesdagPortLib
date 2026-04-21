package org.mesdag.portlib.diff.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.diff.Diff;

import java.util.concurrent.CompletableFuture;

@Diff
@Mod.EventBusSubscriber(modid = PortLib.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PortDataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        generator.addProvider(event.includeServer(), new PortDamageTypeTagsProvider(output, provider, helper));
    }
}
