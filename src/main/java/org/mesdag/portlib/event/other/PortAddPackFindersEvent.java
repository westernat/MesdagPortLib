package org.mesdag.portlib.event.other;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.resource.PathPackResources;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortModBusEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortAddPackFindersEvent extends PortEvent<AddPackFindersEvent> implements IPortModBusEvent {
    @Diff
    public PortAddPackFindersEvent(AddPackFindersEvent e) {
        super(e);
    }

    public void addRepositorySource(RepositorySource source) {
        e.addRepositorySource(source);
    }

    public PackType getPackType() {
        return e.getPackType();
    }

    public void addPackFinders(ResourceLocation packLocation, PackType packType, Component packNameDisplay, PackSource packSource, boolean alwaysActive, Pack.Position packPosition) {
        if (getPackType() == packType) {
            IModInfo modInfo = ModList.get().getModContainerById(packLocation.getNamespace()).orElseThrow(() -> new IllegalArgumentException("Mod not found: " + packLocation.getNamespace())).getModInfo();

            var resourcePath = modInfo.getOwningFile().getFile().findResource(packLocation.getPath());

            var pack = Pack.readMetaAndCreate(
                    "mod/" + packLocation,
                    packNameDisplay,
                    alwaysActive,
                    id -> new PathPackResources(id, true, resourcePath),
                    packType,
                    packPosition,
                    PackSource.BUILT_IN
            );

            addRepositorySource((packConsumer) -> packConsumer.accept(pack));
        }
    }

    public boolean isTrusted() {
        return true;
    }

    static {
        PortEventHooks.register();
    }
}
