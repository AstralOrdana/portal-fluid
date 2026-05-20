package com.ordana.dimensional_tears.fabric;

import com.ordana.dimensional_tears.fluids.DimensionalTearsFluid;
import net.fabricmc.fabric.api.registry.fluid.FluidBehavior;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityFluidInteraction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("all")
public class DimensionalTearsBehavior implements FluidBehavior {
    public static final FluidBehavior DIMENSIONAL_TEARS = new DimensionalTearsBehavior();

    @Override
    public void handleFluidInteractionUpdate(TagKey<Fluid> fluid, Entity entity, EntityFluidInteraction interaction, boolean canPushEntity) {
        WATER_LIKE.handleFluidInteractionUpdate(fluid, entity, interaction, canPushEntity);
    }

    @Override
    public void travelInFluid(TagKey<Fluid> fluid, LivingEntity entity, Vec3 input, double baseGravity, boolean isFalling, double oldY) {
        double gravity = entity.getGravity();
        boolean falling = entity.getDeltaMovement().y <= 0.0;

        if (falling && entity.hasEffect(MobEffects.SLOW_FALLING))
            gravity = Math.min(gravity, 0.01);

        DimensionalTearsFluid.move(entity, gravity, falling, input);
    }

    @Override
    public boolean canSwimInFluid(TagKey<Fluid> fluid, Entity entity) {
        return FluidBehavior.super.canSwimInFluid(fluid, entity);
    }

    @Override
    public boolean shouldTryFloatingInFluid(TagKey<Fluid> fluid, Entity entity) {
        return FluidBehavior.super.shouldTryFloatingInFluid(fluid, entity);
    }

    @Override
    public boolean canMoveDownInFluid(TagKey<Fluid> fluid, Entity entity) {
        return FluidBehavior.super.canMoveDownInFluid(fluid, entity);
    }

    @Override
    public boolean canDrownInFluid(TagKey<Fluid> fluid, LivingEntity entity) {
        return FluidBehavior.super.canDrownInFluid(fluid, entity);
    }

    @Override
    public boolean canSupportBoat(TagKey<Fluid> fluid, Entity entity) {
        return true;
    }

    @Override
    public boolean canSprintInFluid(TagKey<Fluid> fluid, LivingEntity entity) {
        return FluidBehavior.super.canSprintInFluid(fluid, entity);
    }
}
