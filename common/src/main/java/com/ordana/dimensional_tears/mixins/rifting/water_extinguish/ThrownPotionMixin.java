package com.ordana.dimensional_tears.mixins.rifting.water_extinguish;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.ordana.dimensional_tears.reg.ModEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownPotion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ThrownPotion.class)
public class ThrownPotionMixin {

    @WrapOperation(method = "applyWater", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isOnFire()Z"))
    private boolean isOnFireOrHasRifting(LivingEntity instance, Operation<Boolean> original) {
        return original.call(instance) || instance.hasEffect(ModEffects.RIFTING.getHolder());
    }

}
