package org.mesdag.portlib.wrapper.common.conditions;

import PortLib.extensions.com.mojang.serialization.Codec.PortCodecExtension;
import PortLib.extensions.net.minecraftforge.common.crafting.conditions.ICondition.PortIConditionExtension;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.minecraft.resources.RegistryOps;
import net.minecraft.util.ExtraCodecs;
import net.minecraftforge.common.crafting.conditions.ICondition;

import java.util.List;
import java.util.Optional;

public class PortConditionalOps<T> extends RegistryOps<T> {
    public static final String DEFAULT_CONDITIONS_KEY = "portlib:conditions";
    public static final String CONDITIONAL_VALUE_KEY = "portlib:value";

    private final ICondition.IContext context;

    public PortConditionalOps(RegistryOps<T> ops, ICondition.IContext context) {
        super(ops, ops.lookupProvider);
        this.context = context;
    }

    public static MapCodec<ICondition.IContext> retrieveContext() {
        return ExtraCodecs.retrieveContext(ops -> {
            if (!(ops instanceof PortConditionalOps<?> conditionalOps))
                return DataResult.success(ICondition.IContext.EMPTY);

            return DataResult.success(conditionalOps.context);
        });
    }

    public static <T> Codec<Optional<T>> createConditionalCodec(final Codec<T> ownerCodec) {
        return createConditionalCodec(ownerCodec, DEFAULT_CONDITIONS_KEY);
    }

    public static <T> Codec<Optional<T>> createConditionalCodec(final Codec<T> ownerCodec, String conditionalsKey) {
        return createConditionalCodecWithConditions(ownerCodec, conditionalsKey).xmap(r -> r.map(PortWithConditions::carrier), r -> r.map(i -> new PortWithConditions<>(List.of(), i)));
    }

    public static <T> Codec<List<T>> decodeListWithElementConditions(final Codec<T> ownerCodec) {
        return Codec.of(ownerCodec.listOf(), PortCodecExtension.listWithOptionalElements(createConditionalCodec(ownerCodec)));
    }

    public static <T> Codec<Optional<PortWithConditions<T>>> createConditionalCodecWithConditions(final Codec<T> ownerCodec) {
        return createConditionalCodecWithConditions(ownerCodec, DEFAULT_CONDITIONS_KEY);
    }

    public static <T> Codec<Optional<PortWithConditions<T>>> createConditionalCodecWithConditions(final Codec<T> ownerCodec, String conditionalsKey) {
        return Codec.of(
                new ConditionalEncoder<>(conditionalsKey, PortIConditionExtension.listCodec(), ownerCodec),
                new ConditionalDecoder<>(conditionalsKey, PortIConditionExtension.listCodec(), retrieveContext().codec(), ownerCodec)
        );
    }

    private static final class ConditionalEncoder<A> implements Encoder<Optional<PortWithConditions<A>>> {
        private final String conditionalsPropertyKey;
        public final Codec<List<ICondition>> conditionsCodec;
        private final Encoder<A> innerCodec;

        private ConditionalEncoder(String conditionalsPropertyKey, Codec<List<ICondition>> conditionsCodec, Encoder<A> innerCodec) {
            this.conditionalsPropertyKey = conditionalsPropertyKey;
            this.conditionsCodec = conditionsCodec;
            this.innerCodec = innerCodec;
        }

        @Override
        public <T> DataResult<T> encode(Optional<PortWithConditions<A>> input, DynamicOps<T> ops, T prefix) {
            if (ops.compressMaps()) {
                return DataResult.error(() -> "Cannot use ConditionalCodec with compressing DynamicOps");
            }

            if (input.isEmpty()) {
                return DataResult.error(() -> "Cannot encode empty Optional with a ConditionalEncoder. We don't know what to encode to!");
            }

            PortWithConditions<A> withConditions = input.get();

            if (withConditions.conditions().isEmpty()) {
                return innerCodec.encode(withConditions.carrier(), ops, prefix);
            }

            RecordBuilder<T> recordBuilder = ops.mapBuilder();
            recordBuilder.add(conditionalsPropertyKey, conditionsCodec.encodeStart(ops, withConditions.conditions()));

            DataResult<T> encodedInner = innerCodec.encodeStart(ops, withConditions.carrier());

            return encodedInner.flatMap(inner -> ops.getMap(inner).map(innerMap -> {
                if (innerMap.get(conditionalsPropertyKey) != null || innerMap.get(CONDITIONAL_VALUE_KEY) != null) {
                    return DataResult.<T>error(() -> "Cannot wrap a value that already uses the condition or value key with a ConditionalCodec.");
                }
                innerMap.entries().forEach(pair -> recordBuilder.add(pair.getFirst(), pair.getSecond()));
                return recordBuilder.build(prefix);
            }).result().orElseGet(() -> {
                recordBuilder.add(CONDITIONAL_VALUE_KEY, inner);
                return recordBuilder.build(prefix);
            }));
        }

        @Override
        public String toString() {
            return "Conditional[" + innerCodec + "]";
        }
    }

    private static final class ConditionalDecoder<A> implements Decoder<Optional<PortWithConditions<A>>> {
        private final String conditionalsPropertyKey;
        public final Codec<List<ICondition>> conditionsCodec;
        private final Codec<ICondition.IContext> contextCodec;
        private final Decoder<A> innerCodec;

        private ConditionalDecoder(String conditionalsPropertyKey, Codec<List<ICondition>> conditionsCodec, Codec<ICondition.IContext> contextCodec, Decoder<A> innerCodec) {
            this.conditionalsPropertyKey = conditionalsPropertyKey;
            this.conditionsCodec = conditionsCodec;
            this.contextCodec = contextCodec;
            this.innerCodec = innerCodec;
        }

        @Override
        public <T> DataResult<Pair<Optional<PortWithConditions<A>>, T>> decode(DynamicOps<T> ops, T input) {
            if (ops.compressMaps()) {
                return DataResult.error(() -> "Cannot use ConditionalCodec with compressing DynamicOps");
            }

            return ops.getMap(input).map(inputMap -> {
                T conditionsDataCarrier = inputMap.get(conditionalsPropertyKey);
                if (conditionsDataCarrier == null) {
                    return innerCodec.decode(ops, input).map(result -> result.mapFirst(carrier -> Optional.of(new PortWithConditions<>(carrier))));
                }

                return conditionsCodec.decode(ops, conditionsDataCarrier).flatMap(conditionsCarrier -> {
                    List<ICondition> conditions = conditionsCarrier.getFirst();
                    DataResult<Pair<ICondition.IContext, T>> contextDataResult = contextCodec.decode(ops, ops.emptyMap());

                    return contextDataResult.flatMap(contextCarrier -> {
                        ICondition.IContext context = contextCarrier.getFirst();

                        boolean conditionsMatch = conditions.stream().allMatch(c -> c.test(context));
                        if (!conditionsMatch)
                            return DataResult.success(Pair.of(Optional.empty(), input));

                        DataResult<Pair<A, T>> innerDecodeResult;

                        T valueDataCarrier = inputMap.get(CONDITIONAL_VALUE_KEY);
                        if (valueDataCarrier != null) {
                            innerDecodeResult = innerCodec.decode(ops, valueDataCarrier);
                        } else {
                            T conditionalsKey = ops.createString(conditionalsPropertyKey);
                            var mapForDecoding = ops.createMap(inputMap
                                    .entries()
                                    .filter(pair -> !pair.getFirst().equals(conditionalsKey)));
                            innerDecodeResult = innerCodec.decode(ops, mapForDecoding);
                        }

                        DataResult<Pair<Optional<PortWithConditions<A>>, T>> ret = innerDecodeResult.map(
                                result -> result.mapFirst(
                                        carrier -> Optional.of(new PortWithConditions<>(conditions, carrier))));
                        return ret;
                    });
                });
            }).result().orElseGet(() -> innerCodec.decode(ops, input).map(result -> result.mapFirst(carrier -> Optional.of(new PortWithConditions<>(carrier)))));
        }
    }
}
