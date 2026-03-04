package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.PortalFluidRoot;
import com.ordana.dimensional_tears.fluids.PortalFluid;
import net.mehvahdjukaar.moonlight.api.fluids.ModFlowingFluid;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public class ModFluids {

    public static final Supplier<PortalFluid.Flowing> FLOWING_DIMENSIONAL_TEARS = registerFluid(
        "flowing_dimensional_tears",
        () -> new PortalFluid.Flowing(
            ModFlowingFluid.properties()
                .supportsBoating(true)
                .lightLevel(5),
            ModBlocks.DIMENSIONAL_TEARS
        )
    );

    public static final Supplier<PortalFluid.Source> DIMENSIONAL_TEARS = registerFluid(
        "dimensional_tears",
        () -> new PortalFluid.Source(
            ModFlowingFluid.properties()
                .supportsBoating(true)
                .lightLevel(5),
            ModBlocks.DIMENSIONAL_TEARS
        )
    );

    private static <T extends Fluid> Supplier<T> registerFluid(String path, Supplier<T> supplier) {
        return RegHelper.registerFluid(PortalFluidRoot.res(path), supplier);
    }

    public static void init() {}

}
