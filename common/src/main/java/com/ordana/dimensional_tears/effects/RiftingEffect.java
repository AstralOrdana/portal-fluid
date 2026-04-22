package com.ordana.dimensional_tears.effects;

import com.ordana.dimensional_tears.configs.ClientConfigs;
import com.ordana.dimensional_tears.configs.CommonConfigs;
import com.ordana.dimensional_tears.networking.RiftingParticleS2CMessage;
import com.ordana.dimensional_tears.reg.ModEffects;
import com.ordana.dimensional_tears.reg.ModParticles;
import com.ordana.dimensional_tears.util.TeleportHelper;
import net.minecraft.SharedConstants;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class RiftingEffect extends CountdownEffect {

    @Nullable private ItemStack causingStack = null;

    public RiftingEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color, ModParticles.RIFT_FLAME);
    }

    public void setCausingStack(@Nullable ItemStack causingStack) {
        this.causingStack = causingStack;
    }

    @Override
    public void onFinalTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.level() instanceof ServerLevel serverLevel)
            TeleportHelper.teleportEntity(serverLevel, livingEntity, this.causingStack);
    }

    public static int teleportDelayTicks() {
        return CommonConfigs.RIFTING_DELAY_SECONDS.get() * SharedConstants.TICKS_PER_SECOND;
    }

    public static void tryAddInstanceParticles(ServerLevel serverLevel, LivingEntity livingEntity, MobEffectInstance mobEffectInstance) {
        int duration = mobEffectInstance.getDuration();

        if (duration > 1) {
            float f = 1.0F - Math.clamp((float) duration / teleportDelayTicks(), 0.0F, 1.0F);
            RiftingParticleS2CMessage.send(serverLevel, livingEntity, (byte) Mth.lerpDiscrete(f * f * f, 0, ClientConfigs.EFFECT_PARTICLE_DENSITY.get()), f, false);
            // addParticles(serverLevel, livingEntity, Mth.lerpDiscrete(f * f * f, 0, ClientConfigs.EFFECT_PARTICLE_DENSITY.get()), f, false);
        }
    }

    public static boolean has(LivingEntity livingEntity) {
        return livingEntity.hasEffect(ModEffects.RIFTING.getHolder());
    }

}
