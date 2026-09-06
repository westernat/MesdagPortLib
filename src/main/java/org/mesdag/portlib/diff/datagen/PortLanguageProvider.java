package org.mesdag.portlib.diff.datagen;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.data.LanguageProvider;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.registries.PortRegistryEntry;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class PortLanguageProvider extends LanguageProvider {
    private final Map<String, String> enData = new TreeMap<>();
    private final Map<String, String> zhData = new TreeMap<>();
    private final PackOutput output;
    private final String locale;

    public PortLanguageProvider(PackOutput output, String locale) {
        super(output, PortLib.MODID, locale);
        this.output = output;
        this.locale = locale;
    }

    @Override
    protected void addTranslations() {
        addAttribute(PortLib.BLOCK_BREAK_SPEED, "Block Break Speed", "方块破坏速度");
        addAttribute(PortLib.BURNING_TIME, "Burning Time", "燃烧时间");
        addAttribute(PortLib.EXPLOSION_KNOCKBACK_RESISTANCE, "Explosion Knockback Resistance", "爆炸击退抗性");
        addAttribute(PortLib.FALL_DAMAGE_MULTIPLIER, "Fall Damage Multiplier", "摔落伤害倍率");
        addAttribute(PortLib.FLYING_SPEED, "Flying Speed", "飞行速度");
        addAttribute(PortLib.JUMP_STRENGTH, "Jump Strength", "跳跃力度");
        addAttribute(PortLib.MAX_ABSORPTION, "Max Absorption", "最大伤害吸收");
        addAttribute(PortLib.MINING_EFFICIENCY, "Mining Efficiency", "挖掘效率");
        addAttribute(PortLib.MOVEMENT_EFFICIENCY, "Movement Efficiency", "移动效率");
        addAttribute(PortLib.OXYGEN_BONUS, "Oxygen Bonus", "氧气加成");
        addAttribute(PortLib.SAFE_FALL_DISTANCE, "Safe Fall Distance", "安全摔落距离");
        addAttribute(PortLib.SCALE, "Scale", "体型缩放");
        addAttribute(PortLib.SNEAKING_SPEED, "Sneaking Speed", "潜行速度");
        addAttribute(PortLib.SUBMERGED_MINING_SPEED, "Submerged Mining Speed", "水下挖掘速度");
        addAttribute(PortLib.SWEEPING_DAMAGE_RATIO, "Sweeping Damage Ratio", "横扫伤害比例");
        addAttribute(PortLib.WATER_MOVEMENT_EFFICIENCY, "Water Movement Efficiency", "水中移动效率");
        addAttribute(PortLib.CREATIVE_FLIGHT, "Creative Flight", "创造飞行");

        add("portlib.value.flat", "%s");
        add("portlib.value.percent", "%s%%");
        add("portlib.value.boolean.enabled", "Enabled");
        add("portlib.value.boolean.disabled", "Disabled");
        add("portlib.value.boolean.enable", "Enables");
        add("portlib.value.boolean.disable", "Disables");
        add("portlib.value.boolean.invalid", "Invalid");
        add("portlib.modifier.plus", "+%s %s");
        add("portlib.modifier.take", "%s %s");
        add("portlib.modifier.bool", "%s %s");

        add("portlib.configuration.title", "%s Configuration", "%s配置");
        add("portlib.configuration.type.client", "%s Client Configuration", "%s客户端配置");
        add("portlib.configuration.type.common", "%s Common Configuration", "%s通用配置");
        add("portlib.configuration.type.server", "%s Server Configuration", "%s服务端配置");
        add("portlib.configuration.not_loaded", "This configuration is not loaded in the current context.", "此配置在当前环境中尚未加载。");
        add("portlib.configuration.requires_world_restart", "Changes to this option require re-entering the world.", "修改此项后需要重新进入世界。");
        add("portlib.configuration.undo", "Undo", "撤销");
        add("portlib.configuration.reset", "Reset", "重置");

        add("portlib.network.advanced_add_entity.failed", "Failed to process advanced entity spawn data: %s", "处理高级实体生成数据失败：%s");
        add("portlib.network.data_maps.failed", "Failed to handle registry data map sync for registry %s: %s", "处理注册表数据映射同步失败 %s: %s");
        add("portlib.network.data_maps.missing_our", "Cannot connect to server as it is missing mandatory registry data maps present on the client: %s", "无法连接至服务器，因为客户端缺少必要的注册表数据映射：%s");
        add("portlib.network.data_maps.missing_their", "Cannot connect to server as it has mandatory registry data maps not present on the client: %s", "无法连接至服务器，因为客户端上不存在必要的注册表数据映射：%s");
    }

    @Override
    public void add(String key, String value) {
        add(key, value, value);
    }

    private void addAttribute(PortRegistryEntry<Attribute, ?> entry, String en, String zh) {
        add(entry.get().getDescriptionId(), en, zh);
    }

    private static String toTitleCase(String raw) {
        return Arrays.stream(raw.split("_"))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        addTranslations();
        Path path = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(PortLib.MODID).resolve("lang");
        if (locale.equals("en_us") && !enData.isEmpty()) {
            return save(enData, cache, path.resolve("en_us.json"));
        }
        if (locale.equals("zh_cn") && !zhData.isEmpty()) {
            return save(zhData, cache, path.resolve("zh_cn.json"));
        }
        return CompletableFuture.allOf();
    }

    private CompletableFuture<?> save(Map<String, String> data, CachedOutput cache, Path target) {
        JsonObject json = new JsonObject();
        data.forEach(json::addProperty);
        return DataProvider.saveStable(cache, json, target);
    }

    private void add(String key, String en, String zh) {
        if (locale.equals("en_us")) {
            enData.put(key, en);
        } else if (locale.equals("zh_cn")) {
            zhData.put(key, zh);
        }
    }
}
