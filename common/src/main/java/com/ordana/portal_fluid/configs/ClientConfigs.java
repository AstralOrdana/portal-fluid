package com.ordana.portal_fluid.configs;

import com.ordana.portal_fluid.PortalFluidRoot;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigSpec;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;

import java.util.function.Supplier;

public class ClientConfigs {

    public static void init() {

    }

    public static ConfigSpec CONFIG_SPEC;

    public static Supplier<Double> PORTAL_FLUID_SEED;

    static {
        ConfigBuilder builder = ConfigBuilder.create(PortalFluidRoot.res("client"), ConfigType.CLIENT);

        builder.push("general");
        PORTAL_FLUID_SEED = builder.define("portal_fluid_seed", 1D, 0.01D, 1D);
        builder.pop();

        CONFIG_SPEC = builder.buildAndRegister();
        CONFIG_SPEC.loadFromFile();
    }

}