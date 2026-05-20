package com.ordana.dimensional_tears.mixins.rifting.water_extinguish;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.ordana.dimensional_tears.reg.ModEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LayeredCauldronBlock.class)
public class LayeredCauldronBlockMixin {

    @WrapOperation(method = "lambda$entityInside$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isOnFire()Z"))
    private boolean isOnFireOrHasRifting(Entity instance, Operation<Boolean> original) {
        return original.call(instance) || instance instanceof LivingEntity livingEntity && livingEntity.hasEffect(ModEffects.RIFTING);
    }

}
