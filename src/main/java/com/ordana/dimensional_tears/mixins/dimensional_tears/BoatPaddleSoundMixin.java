package com.ordana.dimensional_tears.mixins.dimensional_tears;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.ordana.dimensional_tears.fluids.DimensionalTearsFluid;
import com.ordana.dimensional_tears.reg.ModSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractBoat.class)
public abstract class BoatPaddleSoundMixin extends Entity {

    @Shadow private double waterLevel;

    public BoatPaddleSoundMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @WrapMethod(method = "getPaddleSound")
    private SoundEvent directDimTearsSoundEvent(Operation<SoundEvent> original) {
        if (DimensionalTearsFluid.isBoatRowingIn(this.level(), this.getBoundingBox(), () -> this.waterLevel, d -> this.waterLevel = d))
            return ModSoundEvents.BOAT_PADDLE_DIMENSIONAL_TEARS.value();

        return original.call();
    }

}
