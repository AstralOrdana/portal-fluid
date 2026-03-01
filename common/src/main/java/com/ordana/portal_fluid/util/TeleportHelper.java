/*
Methods in this class from MajruszLibrary under the MIT license

Full repo can be found here: https://github.com/Majrusz/MajruszLibrary

        Permission is hereby granted, free of charge, to any person obtaining
        a copy of this software and associated documentation files (the
        'Software'), to deal in the Software without restriction, including
        without limitation the rights to use, copy, modify, merge, publish,
        distribute, sublicense, and/or sell copies of the Software, and to
        permit persons to whom the Software is furnished to do so, subject to
        the following conditions:

        The above copyright notice and this permission notice shall be
        included in all copies or substantial portions of the Software.

        THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
        EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
        MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
        IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
        CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
        TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
        SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.ordana.portal_fluid.util;

import com.ordana.portal_fluid.configs.CommonConfigs;
import com.ordana.portal_fluid.reg.ModEffects;
import com.ordana.portal_fluid.reg.ModSoundEvents;
import com.ordana.portal_fluid.reg.ModTags;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public final class TeleportHelper {

    private static final int DEFAULT_RIFTING_TICKS = SharedConstants.TICKS_PER_SECOND * 10;

    public static void tryDelegateTeleportationToRiftingEffect(ServerLevel serverLevel, Entity entity) {
        if (entity instanceof LivingEntity livingEntity && !CommonConfigs.INSTANT_TELEPORTATION.get()) {
            Holder<MobEffect> mobEffectHolder = ModEffects.RIFTING.getHolder();

            if (!livingEntity.hasEffect(mobEffectHolder) && !livingEntity.isSpectator())
                livingEntity.addEffect(new MobEffectInstance(mobEffectHolder, DEFAULT_RIFTING_TICKS));
        }
        else teleportEntity(serverLevel, entity, null);
    }

    public static void teleportEntity(ServerLevel serverLevel, Entity entity, @Nullable ItemStack itemStack) {
        if (entity instanceof ServerPlayer serverPlayer && serverPlayer.getRespawnPosition() != null)
            TeleportHelper.teleportPlayerToSpawnPosition(serverLevel, serverPlayer, itemStack);
        else TeleportHelper.teleportToWorldspawn(serverLevel, entity);
    }

    public static void teleportPlayerToSpawnPosition(ServerLevel serverLevel, ServerPlayer serverPlayer, @Nullable ItemStack itemStack) {
        RespawnData.create(serverPlayer, itemStack).teleport(serverPlayer);
        playTeleportSound(serverLevel, serverPlayer);
    }

    private static void teleportToWorldspawn(ServerLevel serverLevel, Entity entity) {
        Vec3 spawnPosition = serverLevel.getSharedSpawnPos().getBottomCenter();
        entity.teleportTo(spawnPosition.x, spawnPosition.y, spawnPosition.z);

        playTeleportSound(serverLevel, entity);
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
