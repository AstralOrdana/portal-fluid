package com.ordana.dimensional_tears.mixins.rifting.water_extinguish;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.ordana.dimensional_tears.effects.RiftingEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.AbstractThrownPotion;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownSplashPotion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractThrownPotion.class)
public class ThrownPotionMixin {

    @ModifyReturnValue(method = "lambda$static$0", at = @At("RETURN"))
    private static boolean appendRiftingCheckToMainLambda(boolean original, LivingEntity livingEntity) {
        return original || RiftingEffect.has(livingEntity);
    }

    @WrapOperation(method = "onHitAsWater", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isOnFire()Z"))
    private boolean isOnFireOrHasRifting(LivingEntity instance, Operation<Boolean> original) {
        return original.call(instance) || RiftingEffect.has(instance);
    }

}
