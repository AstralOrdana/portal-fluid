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

    public static Supplier<Double> DIMENSIONAL_TEARS_SEED;

    static {
        ConfigBuilder builder = ConfigBuilder.create(DimensionalTearsRoot.res("client"), ConfigType.CLIENT);

        builder.push("general");
        DIMENSIONAL_TEARS_SEED = builder.define("dimensional_tears_seed", 1.0, 0.01, 1.0);
        builder.pop();

        CONFIG_SPEC = builder.build();
        CONFIG_SPEC.forceLoad();
    }

}