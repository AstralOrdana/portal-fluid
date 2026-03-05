package com.ordana.dimensional_tears.mixins.dimensional_tears;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.ordana.dimensional_tears.fluids.DimensionalTearsFluid;
import com.ordana.dimensional_tears.reg.ModSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Boat.class)
public abstract class BoatPaddleSoundMixin extends Entity {

    @Shadow private double waterLevel;
    @Unique private boolean dimensional_tears$rowingInDimTears = false;

    public BoatPaddleSoundMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(method = "getStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/Boat;checkInWater()Z"))
    private boolean shouldRegardAsInWater(boolean original) {
        this.dimensional_tears$rowingInDimTears = DimensionalTearsFluid.isBoatRowingIn(this.level(), this.getBoundingBox(), () -> this.waterLevel, d -> this.waterLevel = d);
        return original || this.dimensional_tears$rowingInDimTears;
    }

    @ModifyExpressionValue(method = "getPaddleSound", at = @At(value = "FIELD", target = "Lnet/minecraft/sounds/SoundEvents;BOAT_PADDLE_WATER:Lnet/minecraft/sounds/SoundEvent;", opcode = Opcodes.GETSTATIC))
    private SoundEvent directDimTearsSoundEvent(SoundEvent original) {
        return this.dimensional_tears$rowingInDimTears ? ModSoundEvents.BOAT_PADDLE_DIMENSIONAL_TEARS.get() : original;
    }

}
