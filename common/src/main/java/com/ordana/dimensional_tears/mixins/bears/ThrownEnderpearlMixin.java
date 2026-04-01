package com.ordana.dimensional_tears.mixins.bears;

import com.ordana.dimensional_tears.entity.DimensionalBear;
import com.ordana.dimensional_tears.reg.ModSoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.ordana.dimensional_tears.entity.DimensionalBear.spawnBear;

@Mixin(ThrownEnderpearl.class)
public abstract class ThrownEnderpearlMixin {

    @Inject(method = "onHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;changeDimension(Lnet/minecraft/world/level/portal/DimensionTransition;)Lnet/minecraft/world/entity/Entity;", ordinal = 0))
    private void directDimTearsSoundEvent(HitResult hitResult, CallbackInfo ci) {
        var thrownEnderpearl = (ThrownEnderpearl) (Object) this;
        spawnBear(thrownEnderpearl);
    }

}
