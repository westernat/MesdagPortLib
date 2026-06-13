package PortLib.extensions.net.minecraft.advancements.AdvancementProgress;

import PortLib.extensions.com.mojang.serialization.Codec.PortCodecExtension;
import com.mojang.serialization.Codec;
import net.minecraft.advancements.CriterionProgress;
import org.mesdag.portlib.diff.mixin.CriterionProgressAccessor;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PortAdvancementProgressExtension {
    private static final DateTimeFormatter OBTAINED_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z", Locale.ROOT);
    private static final Codec<Instant> OBTAINED_TIME_CODEC = PortCodecExtension.temporalCodec(OBTAINED_TIME_FORMAT)
            .xmap(Instant::from, instant -> instant.atZone(ZoneId.systemDefault()));
    private static final Codec<Map<String, CriterionProgress>> CRITERIA_CODEC = Codec.unboundedMap(Codec.STRING, OBTAINED_TIME_CODEC).xmap(
            map -> map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                CriterionProgress progress = new CriterionProgress();
                ((CriterionProgressAccessor) progress).setObtained(Date.from(entry.getValue()));
                return progress;
            })),
            map -> map.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().isDone())
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> Objects.requireNonNull(entry.getValue().getObtained()).toInstant()))
    );

    public static DateTimeFormatter obtainedTimeFormat() {
        return OBTAINED_TIME_FORMAT;
    }

    public static Codec<Map<String, CriterionProgress>> criteriaCodec() {
        return CRITERIA_CODEC;
    }
}
