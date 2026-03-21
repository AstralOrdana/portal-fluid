package com.ordana.dimensional_tears.effects;

import com.ordana.dimensional_tears.configs.CommonConfigs;
import com.ordana.dimensional_tears.reg.ModParticles;
import com.ordana.dimensional_tears.util.TeleportHelper;
import net.minecraft.SharedConstants;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class RiftingEffect extends CountdownEffect {

    public static final int MAX_PARTICLE_ITERATIONS = 25;
    private static final double PARTICLE_HITBOX_RADIUS = 1.65;
    private static final float MIN_PARTICLE_HEIGHT_MUL = 0.6F;
    private static final float MAX_PARTICLE_HEIGHT_MUL = 1.1F;

    @Nullable private ItemStack causingStack = null;

    public RiftingEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color, ModParticles.RIFT_FLAME);
    }

    public void setCausingStack(@Nullable ItemStack causingStack) {
        this.causingStack = causingStack;
    }

    @Override
    public void onFinalTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.level() instanceof ServerLevel serverLevel && TeleportHelper.canTeleportTo(livingEntity))
            TeleportHelper.teleportEntity(serverLevel, livingEntity, this.causingStack);
    }

    public static int teleportDelayTicks() {
        return CommonConfigs.RIFTING_DELAY_SECONDS.get() * SharedConstants.TICKS_PER_SECOND;
    }

    public static void tryAddInstanceParticles(ServerLevel serverLevel, LivingEntity livingEntity, MobEffectInstance mobEffectInstance) {
        int duration = mobEffectInstance.getDuration();

        if (duration > 1) {
            float f = 1.0F - Math.clamp((float) duration / teleportDelayTicks(), 0.0F, 1.0F);
            addParticles(serverLevel, livingEntity, Mth.lerpDiscrete(f * f * f, 0, MAX_PARTICLE_ITERATIONS), f);
        }
    }

    public static void addParticles(ServerLevel serverLevel, Entity entity, int iterations, float delta) {
        for (int i = 0; i < iterations; i++) {
            double x = entity.getRandomX(PARTICLE_HITBOX_RADIUS);
            double y = entity.getY(delta * Mth.randomBetween(serverLevel.getRandom(), MIN_PARTICLE_HEIGHT_MUL, MAX_PARTICLE_HEIGHT_MUL));
            double z = entity.getRandomZ(PARTICLE_HITBOX_RADIUS);

            serverLevel.sendParticles(ModParticles.RIFT_FLAME.get(), x, y, z, 1, 0, 0, 0, 0);
        }
    }
}
