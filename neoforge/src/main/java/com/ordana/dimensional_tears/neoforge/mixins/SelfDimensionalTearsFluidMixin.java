package com.ordana.dimensional_tears.neoforge.mixins;

import com.ordana.dimensional_tears.fluids.DimensionalTearsFluid;
import com.ordana.dimensional_tears.neoforge.reg.ModFluidTypes;
import net.minecraft.world.level.material.FlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DimensionalTearsFluid.class)
public abstract class SelfDimensionalTearsFluidMixin extends FlowingFluid {

    @Override
    @NotNull
    public FluidType getFluidType() {
        return ModFluidTypes.DIMENSIONAL_TEARS_TYPE.get();
    }

}
