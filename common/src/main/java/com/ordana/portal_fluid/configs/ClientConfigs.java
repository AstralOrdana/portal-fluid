package com.ordana.portal_fluid.configs;

import com.ordana.portal_fluid.PortalFluidRoot;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;
import net.mehvahdjukaar.moonlight.api.platform.configs.ModConfigHolder;

import java.util.function.Supplier;

public class ClientConfigs {

    public static void init() {

    }

    public static ModConfigHolder CONFIG_SPEC;

    public static Supplier<Double> PORTAL_FLUID_SEED;

    static {
        ConfigBuilder builder = ConfigBuilder.create(PortalFluidRoot.res("client"), ConfigType.CLIENT);

        builder.push("general");
        PORTAL_FLUID_SEED = builder.define("portal_fluid_seed", 1.0, 0.01, 1.0);
        builder.pop();

        CONFIG_SPEC = builder.build();
        CONFIG_SPEC.forceLoad();
    }

}