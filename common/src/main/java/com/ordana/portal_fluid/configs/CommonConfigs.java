package com.ordana.portal_fluid.configs;

import com.ordana.portal_fluid.PortalFluidRoot;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;
import net.mehvahdjukaar.moonlight.api.platform.configs.ModConfigHolder;

import java.util.function.Supplier;

public class CommonConfigs {

    public static ModConfigHolder SERVER_SPEC;

    public static Supplier<Boolean> PORTAL_FLUID_DRINKING;
    public static Supplier<Boolean> INSTANT_TELEPORTATION;
    public static Supplier<Boolean> RESPAWN_ANCHOR_PORTAL_FLUID;
    public static Supplier<Boolean> CRYING_OBSIDIAN_PORTAL_FLUID;
    public static Supplier<Boolean> FlINT_AND_STEEL_PORTAL_LIGHTING;
    public static Supplier<Boolean> PIGLINS_GIVE_CRYING_OBSIDIAN;
    public static Supplier<Boolean> PORTAL_CREATION_SOUND;
    public static Supplier<Boolean> PORTAL_DESTRUCTION_SOUND;
    public static Supplier<Boolean> PORTAL_FLUID_OCEAN;
    public static Supplier<Boolean> END_OCEAN_BUCKETABLE;
    public static Supplier<Float> PORTAL_DESTRUCTION_CRYING_OBSIDIAN_CHANCE;

    public static void init() {
        // bump class load init
    }

    static {
        ConfigBuilder builder = ConfigBuilder.create(PortalFluidRoot.res("common"), ConfigType.COMMON);

//        builder.setSynced();

        builder.push("config");
        FlINT_AND_STEEL_PORTAL_LIGHTING = builder.define("flint_and_steel_portal_lighting", true);
        PORTAL_DESTRUCTION_CRYING_OBSIDIAN_CHANCE = builder.define("portal_destruction_crying_obsidian_chance", 0.25F, 0.0F, 1.0F);

        PORTAL_CREATION_SOUND = builder.define("portal_creation_sound", true);
        PORTAL_DESTRUCTION_SOUND = builder.define("portal_destruction_sound", true);
        PORTAL_FLUID_DRINKING = builder.define("portal_fluid_drinking", true);
        INSTANT_TELEPORTATION = builder.define("instant_teleportation", true);
        PIGLINS_GIVE_CRYING_OBSIDIAN = builder.define("piglins_give_crying_obsidian", true);

        CRYING_OBSIDIAN_PORTAL_FLUID = builder.define("dimensional_tears_from_crying_obsidian", false);
        RESPAWN_ANCHOR_PORTAL_FLUID = builder.define("dimensional_tears_from_respawn_anchor", true);

        PORTAL_FLUID_OCEAN = builder.define("portal_fluid_ocean", true);
        END_OCEAN_BUCKETABLE = builder.define("end_ocean_bucketable", false);
        builder.pop();

        SERVER_SPEC = builder.build();
        SERVER_SPEC.forceLoad();
    }
}
