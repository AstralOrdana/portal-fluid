package com.ordana.dimensional_tears.util;

import com.ordana.dimensional_tears.configs.ClientConfigs;
import com.ordana.dimensional_tears.configs.CommonConfigs;
import com.ordana.dimensional_tears.effects.RiftingEffect;
import com.ordana.dimensional_tears.items.DimensionalTearsBottleItem;
import com.ordana.dimensional_tears.networking.RiftingParticleS2CMessage;
import com.ordana.dimensional_tears.reg.ModEffects;
import com.ordana.dimensional_tears.reg.ModItems;
import com.ordana.dimensional_tears.reg.ModSoundEvents;
import com.ordana.dimensional_tears.reg.ModTags;
import net.minecraft.SharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
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

    private static final DimensionTransition.PostDimensionTransition ON_TRANSITION = DimensionTransition.PLACE_PORTAL_TICKET.then(entity -> tryRemoveRiftingEffect(entity, true));

    public static void tryDelegateTeleportationToRiftingEffect(ServerLevel serverLevel, Entity entity, boolean fullySubmerged, @Nullable ItemStack causingStack) {
        if (!(entity instanceof LivingEntity livingEntity) || fullySubmerged && CommonConfigs.FULLY_SUBMERGED_INSTANT_TELEPORT.get()) {
            teleportEntity(serverLevel, entity, causingStack);
            return;
        }

        Holder<MobEffect> mobEffectHolder = ModEffects.RIFTING.getHolder();
        MobEffectInstance mobEffectInstance = new MobEffectInstance(mobEffectHolder, RiftingEffect.teleportDelayTicks());

        if (!livingEntity.isSpectator() && livingEntity.addEffect(mobEffectInstance) && causingStack != null) {
            ((RiftingEffect) mobEffectHolder.value()).setCausingStack(causingStack);

            if (entity instanceof ServerPlayer serverPlayer)
                serverPlayer.getCooldowns().addCooldown(ModItems.DIMENSIONAL_TEARS_BOTTLE.get(), SharedConstants.TICKS_PER_SECOND * 10);
        }
    }

    public static void tryRemoveRiftingEffect(@Nullable Entity entity, boolean silent) {
        if (entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(ModEffects.RIFTING.getHolder())) {
            livingEntity.removeEffect(ModEffects.RIFTING.getHolder());

            if (!silent && entity.level() instanceof ServerLevel serverLevel) {
                serverLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ModSoundEvents.GENERIC_EXTINGUISH_RIFTING.get(), SoundSource.NEUTRAL, 0.7F, Mth.randomBetween(entity.getRandom(), 0.9F, 1.1F));
                RiftingParticleS2CMessage.send(serverLevel, entity, ClientConfigs.EFFECT_PARTICLE_DENSITY.get().byteValue(), 0.5F, true);
            }
        }
    }

    public static void teleportEntity(ServerLevel serverLevel, Entity entity, @Nullable ItemStack causingStack) {
        playTeleportSound(serverLevel, entity.position());
        entity.changeDimension(getDimensionTransition(serverLevel, entity, causingStack));
    }

    private static DimensionTransition getDimensionTransition(ServerLevel serverLevel, Entity entity, @Nullable ItemStack itemStack) {
        MinecraftServer server = serverLevel.getServer();

        if (entity instanceof ServerPlayer serverPlayer) {
            return Objects.requireNonNullElse(
                DimensionalTearsBottleItem.getAnchorDimensionTransition(server, serverPlayer, itemStack),
                serverPlayer.findRespawnPositionAndUseSpawnBlock(false, ON_TRANSITION)
            );
        }

        return createDimensionTransition(server.overworld(), entity, getSpawnPosition(serverLevel, entity));
    }

    private static Vec3 getSpawnPosition(ServerLevel serverLevel, Entity entity) {
        return entity.adjustSpawnLocation(serverLevel, serverLevel.getSharedSpawnPos()).getBottomCenter();
    }

    public static DimensionTransition createDimensionTransition(ServerLevel serverLevel, Entity entity, Vec3 spawnPosition) {
        return new DimensionTransition(serverLevel, spawnPosition, entity.getDeltaMovement(), entity.getYRot(), entity.getXRot(), ON_TRANSITION);
    }

    public static void playTeleportSound(ServerLevel serverLevel, Vec3 vec3) {
        serverLevel.playSound(null, vec3.x, vec3.y, vec3.z, ModSoundEvents.DIMENSIONAL_TEARS_TELEPORT.get(), SoundSource.BLOCKS);
    }

    public static boolean canTeleportTo(Entity entity) {
        if (entity.getType().is(ModTags.DIMENSIONAL_TEARS_IMMUNE))
            return false;

        return entity.canUsePortal(true) && !entity.isPassenger() && !entity.isVehicle();
    }

}
