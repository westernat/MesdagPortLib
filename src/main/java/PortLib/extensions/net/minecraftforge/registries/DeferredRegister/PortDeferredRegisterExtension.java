package PortLib.extensions.net.minecraftforge.registries.DeferredRegister;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.util.DelayedSupplier;

import java.util.function.Function;

public class PortDeferredRegisterExtension {
    @Diff
    public static <T, I extends T> RegistryObject<I> register(DeferredRegister<T> thiz, String name, Function<ResourceLocation, ? extends I> func) {
        DelayedSupplier<I> sup = new DelayedSupplier<>();
        RegistryObject<I> object = thiz.register(name, sup);
        sup.delegate = () -> func.apply(object.getId());
        return object;
    }
}
