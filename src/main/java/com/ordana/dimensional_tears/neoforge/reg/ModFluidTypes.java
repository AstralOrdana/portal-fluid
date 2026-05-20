//? neoforge {
/*package com.ordana.dimensional_tears.neoforge.reg;

import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.neoforge.fluid.DimensionalTearsFluidType;
import com.ordana.dimensional_tears.reg.RegSupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public interface ModFluidTypes {

    RegSupplier<FluidType> DIMENSIONAL_TEARS_TYPE = registerFluidType("dimensional_tears", DimensionalTearsFluidType::new);

    private static RegSupplier<FluidType> registerFluidType(String path, Supplier<FluidType> fluidTypeSupplier) {
        Identifier res = DimensionalTearsRoot.res(path);
        return new RegSupplier<>(res, Registry.register(NeoForgeRegistries.FLUID_TYPES, res, fluidTypeSupplier.get()));
    }

    static void init() {}

}
*///?}