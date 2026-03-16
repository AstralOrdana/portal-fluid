package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.fluids.DimensionalTearsFluid;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public interface ModFluids {

    Supplier<FlowingFluid> FLOWING_DIMENSIONAL_TEARS = registerFluid("flowing_dimensional_tears", DimensionalTearsFluid.Flowing::new);
    Supplier<FlowingFluid> DIMENSIONAL_TEARS = registerFluid("dimensional_tears", DimensionalTearsFluid.Source::new);

    private static <T extends Fluid> Supplier<T> registerFluid(String path, Supplier<T> fluidSupplier) {
        return RegHelper.registerFluid(DimensionalTearsRoot.res(path), fluidSupplier);
    }

    static void init() {}

}
