package com.ordana.portal_fluid.reg;

import com.ordana.portal_fluid.PortalFluidRoot;
import com.ordana.portal_fluid.fluids.PortalFluid;
import net.mehvahdjukaar.moonlight.api.fluids.ModFlowingFluid;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Supplier;

public class ModFluids extends Fluids {

    public static final Supplier<FlowingFluid> FLOWING_PORTAL_FLUID = registerFluid(
        "flowing_portal_fluid",
        () -> new PortalFluid.Flowing(
            ModFlowingFluid.properties()
                .supportsBoating(true)
                .lightLevel(5),
            ModBlocks.PORTAL_FLUID
        )
    );

    public static final Supplier<FlowingFluid> PORTAL_FLUID = registerFluid(
        "portal_fluid",
        () -> new PortalFluid.Source(
            ModFlowingFluid.properties()
                .supportsBoating(true)
                .lightLevel(5),
            ModBlocks.PORTAL_FLUID
        )
    );

    private static <T extends Fluid> Supplier<T> registerFluid(String path, Supplier<T> supplier) {
        return RegHelper.registerFluid(PortalFluidRoot.res(path), supplier);
    }

    public static void init() {}

}
