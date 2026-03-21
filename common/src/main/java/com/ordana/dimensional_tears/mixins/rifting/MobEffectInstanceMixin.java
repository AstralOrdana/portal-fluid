package com.ordana.dimensional_tears.mixins.rifting;

import com.ordana.dimensional_tears.effects.RiftingEffect;
import com.ordana.dimensional_tears.reg.ModEffects;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffectInstance.class)
public abstract class MobEffectInstanceMixin {

    @Shadow public abstract boolean is(Holder<MobEffect> holder);

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;tickDownDuration()I", shift = At.Shift.AFTER))
    private void addParticles(LivingEntity livingEntity, Runnable runnable, CallbackInfoReturnable<Boolean> cir) {
        if (this.is(ModEffects.RIFTING.getHolder()) && livingEntity.level() instanceof ServerLevel serverLevel)
            RiftingEffect.tryAddInstanceParticles(serverLevel, livingEntity, (MobEffectInstance) (Object) this);
    }

}
