package com.ordana.dimensional_tears.effects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public abstract class CountdownEffect extends MobEffect {

    private final SimpleParticleType mobEffect;

    public CountdownEffect(MobEffectCategory mobEffectCategory, int color, SimpleParticleType mobEffect) {
        super(mobEffectCategory, color);
        this.mobEffect = mobEffect;
    }

    public abstract void onFinalTick(LivingEntity livingEntity, int amplifier);

    @Override
    @NotNull
    public ParticleOptions createParticleOptions(MobEffectInstance mobEffectInstance) {
        return this.mobEffect;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration == 1;
    }

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity mob, int amplification) {
        this.onFinalTick(mob, amplification);
        return true;
    }
}
