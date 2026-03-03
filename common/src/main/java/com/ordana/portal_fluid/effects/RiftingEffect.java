package com.ordana.portal_fluid.effects;

import com.ordana.portal_fluid.reg.ModParticles;
import com.ordana.portal_fluid.util.TeleportHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class RiftingEffect extends CountdownEffect {

    @Nullable private ItemStack causingStack = null;

    public RiftingEffect(MobEffectCategory arg, int i) {
        super(arg, i, ModParticles.PORTAL_FLAME);
    }

    public void setCausingStack(@Nullable ItemStack causingStack) {
        this.causingStack = causingStack;
    }

    @Override
    public void onFinalTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.level() instanceof ServerLevel serverLevel && TeleportHelper.canTeleportTo(livingEntity))
            TeleportHelper.teleportEntity(serverLevel, livingEntity, this.causingStack);
    }

}
