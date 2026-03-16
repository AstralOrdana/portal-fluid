package com.ordana.dimensional_tears.neoforge.reg;

import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.neoforge.fluid.DimensionalTearsFluidType;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public interface ModFluidTypes {

    Supplier<FluidType> DIMENSIONAL_TEARS_TYPE = registerFluidType("dimensional_tears", DimensionalTearsFluidType::new);

    private static Supplier<FluidType> registerFluidType(String path, Supplier<FluidType> fluidTypeSupplier) {
        return RegHelper.register(DimensionalTearsRoot.res(path), fluidTypeSupplier, NeoForgeRegistries.Keys.FLUID_TYPES);
    }

    static void init() {}

}
