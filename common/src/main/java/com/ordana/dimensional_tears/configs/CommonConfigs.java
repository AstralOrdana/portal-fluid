package com.ordana.dimensional_tears.configs;

import com.ordana.dimensional_tears.DimensionalTearsRoot;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;
import net.mehvahdjukaar.moonlight.api.platform.configs.ModConfigHolder;

import java.util.function.Supplier;

public class CommonConfigs {

    public static ModConfigHolder SERVER_SPEC;

    public static Supplier<Boolean> DIMENSIONAL_TEARS_DRINKING;
    public static Supplier<Boolean> RESPAWN_ANCHOR_DIMENSIONAL_TEARS;
    public static Supplier<Boolean> CRYING_OBSIDIAN_DIMENSIONAL_TEARS;
    public static Supplier<Boolean> FlINT_AND_STEEL_PORTAL_LIGHTING;
    public static Supplier<Boolean> PIGLINS_GIVE_CRYING_OBSIDIAN;
    public static Supplier<Boolean> PORTAL_CREATION_SOUND;
    public static Supplier<Boolean> PORTAL_DESTRUCTION_SOUND;
    public static Supplier<Boolean> DIMENSIONAL_TEARS_OCEAN;
    public static Supplier<Boolean> END_OCEAN_BUCKETABLE;
    public static Supplier<Integer> TELEPORTATION_DELAY_SECONDS;
    public static Supplier<Float> PORTAL_DESTRUCTION_CRYING_OBSIDIAN_CHANCE;
    public static Supplier<Boolean> DIMENSIONAL_TEARS_SOURCE_CONVERSION;

    public static void init() {
        // bump class load bootstrap
    }

    static {
        ConfigBuilder builder = ConfigBuilder.create(DimensionalTearsRoot.res("common"), ConfigType.COMMON);

//        builder.setSynced();

        builder.push("config");
        FlINT_AND_STEEL_PORTAL_LIGHTING = builder.define("flint_and_steel_portal_lighting", true);
        PORTAL_DESTRUCTION_CRYING_OBSIDIAN_CHANCE = builder.define("portal_destruction_crying_obsidian_chance", 0.25F, 0.0F, 1.0F);

        PORTAL_CREATION_SOUND = builder.define("portal_creation_sound", true);
        PORTAL_DESTRUCTION_SOUND = builder.define("portal_destruction_sound", true);
        DIMENSIONAL_TEARS_DRINKING = builder.define("dimensional_tears_drinking", true);
        TELEPORTATION_DELAY_SECONDS = builder.define("teleportation_delay_seconds", 10, 0, 300);
        PIGLINS_GIVE_CRYING_OBSIDIAN = builder.define("piglins_give_crying_obsidian", true);

        CRYING_OBSIDIAN_DIMENSIONAL_TEARS = builder.define("dimensional_tears_from_crying_obsidian", false);
        RESPAWN_ANCHOR_DIMENSIONAL_TEARS = builder.define("dimensional_tears_from_respawn_anchor", true);

        DIMENSIONAL_TEARS_OCEAN = builder.define("dimensional_tears_ocean", true);
        END_OCEAN_BUCKETABLE = builder.define("end_ocean_bucketable", false);
        DIMENSIONAL_TEARS_SOURCE_CONVERSION = builder.define("dimensional_tears_source_conversion", false);
        builder.pop();

        SERVER_SPEC = builder.build();
        SERVER_SPEC.forceLoad();
    }
}
