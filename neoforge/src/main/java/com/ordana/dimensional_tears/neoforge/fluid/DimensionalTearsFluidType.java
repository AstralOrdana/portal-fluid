package com.ordana.dimensional_tears.neoforge.fluid;

import com.ordana.dimensional_tears.fluids.DimensionalTearsFluid;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

public class DimensionalTearsFluidType extends FluidType {

    private static final FluidType.Properties DIMENSIONAL_TEARS_PROPERTIES = FluidType.Properties.create()
        .descriptionId("block.dimensional_tears.dimensional_tears")
        .supportsBoating(true)
        .canDrown(false)
        .canSwim(false)
        .pathType(PathType.LAVA)
        .adjacentPathType(null)
        .fallDistanceModifier(0.0F)
        .lightLevel(DimensionalTearsFluid.LUMINANCE);

    public DimensionalTearsFluidType() {
        super(DIMENSIONAL_TEARS_PROPERTIES);
    }

    @Override
    public double motionScale(@NotNull Entity entity) {
        return DimensionalTearsFluid.motionScale();
    }

    @Override
    public boolean move(@NotNull FluidState state, @NotNull LivingEntity entity, @NotNull Vec3 movementVector, double gravity) {
        DimensionalTearsFluid.move(entity, gravity, entity.getDeltaMovement().y <= 0.0, movementVector);
        return true;
    }

}
