package org.mesdag.portlib.wrapper.common.conditions;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.mesdag.portlib.diff.Diff;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface IPortCondition {
    boolean test(IPortContext context);

    @Diff
    MapCodec<? extends ICondition> codec();

    @Diff
    default ICondition unwrap() {
        return new ICondition() {
            @Override
            public boolean test(IContext context) {
                return IPortCondition.this.test(context);
            }

            @Override
            public MapCodec<? extends ICondition> codec() {
                return IPortCondition.this.codec();
            }
        };
    }

    @Diff
    static IPortCondition wrap(ICondition condition) {
        return new Delegate(condition);
    }

    @Diff
    record Delegate(ICondition delegate) implements IPortCondition {
        @Override
        public boolean test(IPortContext context) {
            return delegate.test(context.unwrap());
        }

        @Override
        public MapCodec<? extends ICondition> codec() {
            return delegate.codec();
        }

        @Override
        public ICondition unwrap() {
            return delegate;
        }
    }

    interface IPortContext {
        default <T> Collection<Holder<T>> getTag(TagKey<T> key) {
            return getAllTags(key.registry()).getOrDefault(key.location(), Set.of());
        }

        <T> Map<ResourceLocation, Collection<Holder<T>>> getAllTags(ResourceKey<? extends Registry<T>> registry);

        @Diff
        default ICondition.IContext unwrap() {
            return this::getAllTags;
        }

        @Diff
        static IPortContext wrap(ICondition.IContext context) {
            return new Delegate(context);
        }

        @Diff
        record Delegate(ICondition.IContext delegate) implements IPortContext {
            @Override
            public <T> Collection<Holder<T>> getTag(TagKey<T> key) {
                return delegate.getTag(key);
            }

            @Override
            public <T> Map<ResourceLocation, Collection<Holder<T>>> getAllTags(ResourceKey<? extends Registry<T>> registry) {
                return delegate.getAllTags(registry);
            }

            @Override
            public ICondition.IContext unwrap() {
                return delegate;
            }
        }
    }
}
