package com.ordana.dimensional_tears.mixins.dimensional_tears;

import com.ordana.dimensional_tears.fluids.PortalFluid;
import com.ordana.dimensional_tears.reg.ModSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Boat.class)
public abstract class BoatPaddleSoundMixin extends Entity {

    public BoatPaddleSoundMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "getPaddleSound", at = @At("HEAD"), cancellable = true)
    public void ass(CallbackInfoReturnable<SoundEvent> cir) {
        AABB boundingBox = this.getBoundingBox();

        if (PortalFluid.isTouching(this.level(), boundingBox.setMaxY(boundingBox.minY + 0.001)))
            cir.setReturnValue(ModSoundEvents.BOAT_PADDLE_DIMENSIONAL_TEARS.get());
    }

}
