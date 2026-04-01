package com.ordana.dimensional_tears.mixins.bears;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.ordana.dimensional_tears.entity.DimensionalBear.spawnBear;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "randomTeleport", at = @At(value = "RETURN"))
    private void directDimTearsSoundEvent(double d, double e, double f, boolean bl, CallbackInfoReturnable<Boolean> cir) {
        spawnBear((LivingEntity) (Object) this);
    }

}
