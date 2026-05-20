package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.DimensionalTearsPlatform;
import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.fluids.DimensionalTearsFluid;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public interface ModFluids {

    RegSupplier<FlowingFluid> FLOWING_DIMENSIONAL_TEARS = registerFluid("flowing_dimensional_tears", DimensionalTearsFluid.Flowing::new);
    RegSupplier<FlowingFluid> DIMENSIONAL_TEARS = registerFluid("dimensional_tears", DimensionalTearsFluid.Source::new);

    private static <T extends Fluid> RegSupplier<T> registerFluid(String path, Supplier<T> fluidSupplier) {
        DimensionalTearsPlatform.addAlias(BuiltInRegistries.FLUID, path);
        Identifier res = DimensionalTearsRoot.res(path);
        return new RegSupplier<>(res, Registry.register(BuiltInRegistries.FLUID, res, fluidSupplier.get()));
    }

    static void init() {}

}
