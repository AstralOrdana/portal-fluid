package com.ordana.portal_fluid.effects;

import com.ordana.portal_fluid.reg.ModParticles;
import com.ordana.portal_fluid.util.TeleportHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class RiftingEffect extends CountdownEffect {

    public RiftingEffect(MobEffectCategory arg, int i) {
        super(arg, i, ModParticles.PORTAL_FLAME);
    }

    @Override
    public void onEndCountdown(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.level() instanceof ServerLevel serverLevel && TeleportHelper.canTeleportTo(serverLevel, livingEntity, livingEntity.blockPosition()))
            TeleportHelper.teleportEntity(serverLevel, livingEntity, null);
    }

}
