package PortLib.extensions.net.minecraft.world.entity.ai.attributes.Attribute;

import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.IPortAttribute;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.AttributeHolder;

public class PortAttributeExtension {
    private static final Codec<Holder<Attribute>> CODEC = BuiltInRegistries.ATTRIBUTE.holderByNameCodec();
    private static final PortStreamCodec<PortRegistryFriendlyByteBuf, Holder<Attribute>> STREAM_CODEC = PortByteBufCodecs.holderRegistry(Registries.ATTRIBUTE);

    public static Codec<Holder<Attribute>> codec() {
        return CODEC;
    }

    public static PortStreamCodec<PortRegistryFriendlyByteBuf, Holder<Attribute>> streamCodec() {
        return STREAM_CODEC;
    }

    private static final TextColor MERGED_RED = TextColor.fromRgb(0xF93131);
    private static final TextColor MERGED_BLUE = TextColor.fromRgb(0x7A7AF9);
    private static final TextColor MERGED_GRAY = TextColor.fromRgb(0xCCCCCC);

    public static ChatFormatting getStyle(Attribute thiz, boolean isPositive) {
        return IPortAttribute.of(thiz).portlib$getSentiment().getStyle(isPositive);
    }

    public static TextColor getMergedStyle(Attribute thiz, boolean isPositive) {
        return switch (IPortAttribute.of(thiz).portlib$getSentiment()) {
            case POSITIVE -> isPositive ? MERGED_BLUE : MERGED_RED;
            case NEGATIVE -> isPositive ? MERGED_RED : MERGED_BLUE;
            case NEUTRAL -> MERGED_GRAY;
        };
    }

    @Diff
    public static AttributeHolder wrap(Attribute thiz) {
        return new AttributeHolder(thiz);
    }
}
