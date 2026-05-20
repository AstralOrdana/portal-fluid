package com.ordana.dimensional_tears.util;

import com.ordana.dimensional_tears.DimensionalTearsClient;
import com.ordana.dimensional_tears.configs.ClientConfigs;
import com.ordana.dimensional_tears.reg.ModParticles;
import com.ordana.dimensional_tears.reg.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

public final class DimensionalTearsAmbience {

    public static void tryAnimate(Level level, BlockPos blockPos, double fluidHeight, RandomSource randomSource) {
        if (isAnimatable(level, blockPos)) {
            if (DimensionalTearsClient.CONFIG.particles.AMBIENT_PARTICLE_CHANCE > 0.0)
                particle(level, blockPos, fluidHeight, randomSource);

            if (DimensionalTearsClient.CONFIG.sounds.AMBIENT_SOUND_CHANCE > 0.0)
                sound(level, blockPos, randomSource);
        }
    }

    public static boolean isAnimatable(Level level, BlockPos blockPos) {
        BlockPos above = blockPos.above();
        return level.isEmptyBlock(above) && !level.getBlockState(above).isSolidRender();
    }

    public static void particle(Level level, BlockPos blockPos, double fluidHeight, RandomSource randomSource) {
        if (randomSource.nextDouble() <= DimensionalTearsClient.CONFIG.particles.AMBIENT_PARTICLE_CHANCE) {
            double x = blockPos.getX() + randomSource.nextDouble();
            double y = blockPos.getY() + fluidHeight;
            double z = blockPos.getZ() + randomSource.nextDouble();

            level.addParticle(ModParticles.RIFT_FLAME, x, y, z, 0.0, 0.0, 0.0);
        }
    }

    public static void sound(Level level, BlockPos blockPos, RandomSource randomSource) {
        if (randomSource.nextDouble() <= DimensionalTearsClient.CONFIG.sounds.AMBIENT_SOUND_CHANCE) {
            float volume = (float) getAttenuationDist(randomSource) / 16.0F;
            float pitch = 1.0F + (float) getPitchDeviation(randomSource);

            level.playLocalSound(blockPos, ModSoundEvents.DIMENSIONAL_TEARS_AMBIENT.value(), SoundSource.BLOCKS, volume, pitch, false);
        }
    }

    private static double getPitchDeviation(RandomSource randomSource) {
        double maxDeviation = DimensionalTearsClient.CONFIG.sounds.MAX_SOUND_PITCH_DEVIATION;
        return randomSource.nextDouble() * maxDeviation * 2 - maxDeviation;
    }

    private static double getAttenuationDist(RandomSource randomSource) {
        int multiplier = DimensionalTearsClient.CONFIG.sounds.MAX_SOUND_ATTENUATION_DIST_MULTIPLIER;
        return DimensionalTearsClient.CONFIG.sounds.MIN_SOUND_ATTENUATION_DIST + randomSource.nextDouble() * multiplier;
    }

}
