package com.ordana.dimensional_tears.configs;

import com.ordana.dimensional_tears.DimensionalTearsRoot;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;
import net.mehvahdjukaar.moonlight.api.platform.configs.ModConfigHolder;

import java.util.function.Supplier;

public class ClientConfigs {

    public static void init() {

    }

    public static ModConfigHolder CONFIG_SPEC;

    public static Supplier<Double> AMBIENT_PARTICLE_CHANCE;
    public static Supplier<Double> AMBIENT_PARTICLE_VERTICAL_OFFSET;

    public static Supplier<Double> AMBIENT_SOUND_CHANCE;
    public static Supplier<Double> MAX_SOUND_PITCH_DEVIATION;
    public static Supplier<Double> MIN_SOUND_ATTENUATION_DIST;
    public static Supplier<Integer> MAX_SOUND_ATTENUATION_DIST_MULTIPLIER;

    static {
        ConfigBuilder builder = ConfigBuilder.create(DimensionalTearsRoot.res("client"), ConfigType.CLIENT);

        builder.push("particles");
        AMBIENT_PARTICLE_CHANCE = builder.define("ambient_particle_rarity", 0.05, 0.0, 1.0);
        AMBIENT_PARTICLE_VERTICAL_OFFSET = builder.define("particle_vertical_offset", 0.3, 0.0, 1.0);
        builder.pop();

        builder.push("sounds");
        AMBIENT_SOUND_CHANCE = builder.define("ambient_sound_chance", 0.005, 0.0, 1.0);
        MAX_SOUND_PITCH_DEVIATION = builder.define("max_sound_pitch_deviation", 0.1, 0.0, 1.0);
        MIN_SOUND_ATTENUATION_DIST = builder.define("min_sound_attenuation_dist", 4.0, 0.0, 32.0);
        MAX_SOUND_ATTENUATION_DIST_MULTIPLIER = builder.define("max_sound_attenuation_dist_multiplier", 4, 1, 10);
        builder.pop();

        CONFIG_SPEC = builder.build();
        CONFIG_SPEC.forceLoad();
    }

}