package com.ordana.dimensional_tears.configs;

import com.ordana.dimensional_tears.DimensionalTearsRoot;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;
import net.mehvahdjukaar.moonlight.api.platform.configs.ModConfigHolder;
import net.minecraft.SharedConstants;

import java.util.function.Supplier;

public class CommonConfigs {

    public static ModConfigHolder SERVER_SPEC;

    public static Supplier<Boolean> FlINT_AND_STEEL_PORTAL_LIGHTING;
    public static Supplier<Boolean> DIMENSIONAL_TEARS_DRINKING;
    public static Supplier<Boolean> END_OCEAN_BUCKETABLE;

    public static Supplier<Boolean> PORTAL_CREATION_SOUND;
    public static Supplier<Boolean> PORTAL_DESTRUCTION_SOUND;

    public static Supplier<Boolean> DIMENSIONAL_TEARS_OCEAN;
    public static Supplier<Boolean> CRYING_OBSIDIAN_DIMENSIONAL_TEARS;
    public static Supplier<Boolean> RESPAWN_ANCHOR_DIMENSIONAL_TEARS;
    public static Supplier<Boolean> DIMENSIONAL_TEARS_SOURCE_CONVERSION;
    public static Supplier<Double> PORTAL_DESTRUCTION_CRYING_OBSIDIAN_CHANCE;
    public static Supplier<Double> LAVA_INTERACTION_CRYING_OBSIDIAN_CHANCE;
    public static Supplier<Boolean> PIGLINS_GIVE_CRYING_OBSIDIAN;

    public static Supplier<Integer> RIFTING_DELAY_SECONDS;
    public static Supplier<Integer> FLUID_FLOWING_TICK_RATE;
    public static Supplier<Boolean> FULLY_SUBMERGED_INSTANT_TELEPORT;
    public static Supplier<Integer> SPAWN_BYPASS_INSTANT_TELEPORTATION_RANGE;

    public static void init() {
        // bump class load bootstrap
    }

    static {
        ConfigBuilder builder = ConfigBuilder.create(DimensionalTearsRoot.res("common"), ConfigType.COMMON);

//        builder.setSynced();

        builder.push("capabilities");
        FlINT_AND_STEEL_PORTAL_LIGHTING = builder.define("flint_and_steel_portal_lighting", true);
        DIMENSIONAL_TEARS_DRINKING = builder.define("dimensional_tears_drinking", true);
        END_OCEAN_BUCKETABLE = builder.define("end_ocean_bucketable", false);
        builder.pop();

        builder.push("sounds");
        PORTAL_CREATION_SOUND = builder.define("portal_creation_sound", true);
        PORTAL_DESTRUCTION_SOUND = builder.define("portal_destruction_sound", true);
        builder.pop();

        builder.push("obtaining");
        CRYING_OBSIDIAN_DIMENSIONAL_TEARS = builder.define("dimensional_tears_from_crying_obsidian", false);
        RESPAWN_ANCHOR_DIMENSIONAL_TEARS = builder.define("dimensional_tears_from_respawn_anchor", true);
        PORTAL_DESTRUCTION_CRYING_OBSIDIAN_CHANCE = builder.define("portal_destruction_crying_obsidian_chance", 0.25, 0.0, 1.0);
        LAVA_INTERACTION_CRYING_OBSIDIAN_CHANCE = builder.define("lava_interaction_crying_obsidian_chance", 0.1, 0.0, 1.0);
        PIGLINS_GIVE_CRYING_OBSIDIAN = builder.define("piglins_give_crying_obsidian", true);
        builder.pop();

        builder.push("fluids");
        DIMENSIONAL_TEARS_OCEAN = builder.define("dimensional_tears_ocean", true);
        DIMENSIONAL_TEARS_SOURCE_CONVERSION = builder.define("dimensional_tears_source_conversion", false);
        FLUID_FLOWING_TICK_RATE = builder.define("fluid_flowing_tick_rate", 10, 5, SharedConstants.TICKS_PER_SECOND * 10);
        builder.pop();

        builder.push("teleportation");
        RIFTING_DELAY_SECONDS = builder.define("rifting_delay_seconds", 10, 1, 300);
        FULLY_SUBMERGED_INSTANT_TELEPORT = builder.define("fully_submerged_instant_teleport", true);
        SPAWN_BYPASS_INSTANT_TELEPORTATION_RANGE = builder.define("spawn_bypass_instant_teleportation_range", 4, 0, 32);
        builder.pop();

        SERVER_SPEC = builder.build();
        SERVER_SPEC.forceLoad();
    }

}
