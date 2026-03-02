package com.ordana.portal_fluid.util;

import com.ordana.portal_fluid.configs.CommonConfigs;
import com.ordana.portal_fluid.items.PortalFluidBottleItem;
import com.ordana.portal_fluid.reg.ModEffects;
import com.ordana.portal_fluid.reg.ModSoundEvents;
import com.ordana.portal_fluid.reg.ModTags;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class TeleportHelper {

    public static void tryDelegateTeleportationToRiftingEffect(ServerLevel serverLevel, Entity entity) {
        int delaySeconds = CommonConfigs.TELEPORTATION_DELAY_SECONDS.get();

        if (entity instanceof LivingEntity livingEntity && delaySeconds > 0) {
            Holder<MobEffect> mobEffectHolder = ModEffects.RIFTING.getHolder();

            if (!livingEntity.hasEffect(mobEffectHolder) && !livingEntity.isSpectator())
                livingEntity.addEffect(new MobEffectInstance(mobEffectHolder, SharedConstants.TICKS_PER_SECOND * delaySeconds));
        }
        else teleportEntity(serverLevel, entity, null);
    }

    public static void teleportEntity(ServerLevel serverLevel, Entity entity, @Nullable ItemStack itemStack) {
        entity.changeDimension(getDimensionTransition(serverLevel, entity, itemStack));
        playTeleportSound(serverLevel, entity);
    }

    private static DimensionTransition getDimensionTransition(ServerLevel serverLevel, Entity entity, @Nullable ItemStack itemStack) {
        MinecraftServer server = serverLevel.getServer();

        if (entity instanceof ServerPlayer serverPlayer) {
            return Objects.requireNonNullElse(
                PortalFluidBottleItem.getAnchorDimensionTransition(server, serverPlayer, itemStack),
                serverPlayer.findRespawnPositionAndUseSpawnBlock(false, DimensionTransition.DO_NOTHING)
            );
        }

        return createDimensionTransition(server.overworld(), entity, getSpawnPosition(serverLevel, entity));
    }

    private static Vec3 getSpawnPosition(ServerLevel serverLevel, Entity entity) {
        return entity.adjustSpawnLocation(serverLevel, serverLevel.getSharedSpawnPos()).getBottomCenter();
    }

    public static DimensionTransition createDimensionTransition(ServerLevel serverLevel, Entity entity, Vec3 spawnPosition) {
        return new DimensionTransition(serverLevel, spawnPosition, entity.getDeltaMovement(), entity.getYRot(), entity.getXRot(), DimensionTransition.DO_NOTHING);
    }

    private static void playTeleportSound(ServerLevel serverLevel, Entity entity) {
        playTeleportSound(serverLevel, entity, 1.0F);
    }

    private static void playTeleportSound(ServerLevel serverLevel, Entity entity, float volume) {
        serverLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSoundEvents.PORTAL_FLUID_TELEPORT.get(), SoundSource.NEUTRAL, volume, 1.0F);
    }

    public static boolean canTeleportTo(ServerLevel serverLevel, Entity entity, BlockPos blockPos) {
        if (entity.getType().is(ModTags.PORTAL_FLUID_IMMUNE))
            return false;

        if (entity.isPassenger() || entity.isVehicle())
            return false;

        return !entity.isCrouching() && !blockPos.equals(serverLevel.getSharedSpawnPos());
    }

}
