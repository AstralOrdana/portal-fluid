package com.ordana.dimensional_tears.mixins.rifting;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.ordana.dimensional_tears.reg.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.stream.Stream;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @ModifyExpressionValue(method = "updateSynchronizedMobEffectParticles", at = @At(value = "INVOKE", target = "Ljava/util/Collection;stream()Ljava/util/stream/Stream;", ordinal = 0))
    private Stream<MobEffectInstance> skipAddRiftingToParticleSet(Stream<MobEffectInstance> original) {
        return original.filter(mobEffectInstance -> !mobEffectInstance.is(ModEffects.RIFTING.getHolder()));
    }

}
