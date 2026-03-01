package com.ordana.portal_fluid.effects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public abstract class CountdownEffect extends MobEffect {

    private final Supplier<SimpleParticleType> mobEffect;

    public CountdownEffect(MobEffectCategory mobEffectCategory, int i, Supplier<SimpleParticleType> mobEffect) {
        super(mobEffectCategory, i);
        this.mobEffect = mobEffect;
    }

    public abstract void onEndCountdown(LivingEntity livingEntity, int amplifier);

    @Override
    @NotNull
    public ParticleOptions createParticleOptions(MobEffectInstance mobEffectInstance) {
        return this.mobEffect.get();
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration == 1;
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        this.onEndCountdown(livingEntity, amplifier);
        return true;
    }

}
