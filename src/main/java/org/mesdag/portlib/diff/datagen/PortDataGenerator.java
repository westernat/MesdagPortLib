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
public final class PortDataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        boolean server = event.includeServer();
        generator.addProvider(server, new PortDamageTypeTagsProvider(output, provider, helper));
        PortBlockTagsProvider blockTagsProvider = generator.addProvider(server, new PortBlockTagsProvider(output, provider, helper));
        generator.addProvider(server, new PortItemTagsProvider(output, provider, blockTagsProvider.contentsGetter(), helper));
        generator.addProvider(server, new PortBiomeTagsProvider(output, provider, helper));
        generator.addProvider(server, new PortEntityTypeTagsProvider(output, provider, helper));
        generator.addProvider(server, new PortFluidTagsProvider(output, provider, helper));

        boolean client = event.includeClient();
        generator.addProvider(client, new PortLanguageProvider(output, "en_us"));
        generator.addProvider(client, new PortLanguageProvider(output, "zh_cn"));
    }
}
