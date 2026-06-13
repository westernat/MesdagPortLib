package org.mesdag.portlib.wrapper.resource;

import PortLib.extensions.net.minecraft.core.HolderLookup.PortHolderLookupExtension;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.ApiStatus;
import org.mesdag.portlib.wrapper.common.conditions.PortConditionalOps;

public abstract class PortContextAwareReloadListener implements PreparableReloadListener {
    private ICondition.IContext conditionContext = ICondition.IContext.EMPTY;

    private HolderLookup.Provider registryLookup = RegistryAccess.EMPTY;

    @ApiStatus.Internal
    public void injectContext(ICondition.IContext context, HolderLookup.Provider registryLookup) {
        this.conditionContext = context;
        this.registryLookup = registryLookup;
    }

    protected final ICondition.IContext getContext() {
        return this.conditionContext;
    }

    protected final HolderLookup.Provider getRegistryLookup() {
        return this.registryLookup;
    }

    protected final PortConditionalOps<JsonElement> makeConditionalOps() {
        return new PortConditionalOps<>(PortHolderLookupExtension.Provider.createSerializationContext(getRegistryLookup(), JsonOps.INSTANCE), getContext());
    }
}
