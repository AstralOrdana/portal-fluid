package com.ordana.dimensional_tears.util;

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
            if (ClientConfigs.AMBIENT_PARTICLE_CHANCE.get() > 0.0)
                particle(level, blockPos, fluidHeight, randomSource);

            if (ClientConfigs.AMBIENT_SOUND_CHANCE.get() > 0.0)
                sound(level, blockPos, randomSource);
        }
    }

    public static boolean isAnimatable(Level level, BlockPos blockPos) {
        BlockPos above = blockPos.above();
        return level.isEmptyBlock(above) && !level.getBlockState(above).isSolidRender(level, above);
    }

    public static void particle(Level level, BlockPos blockPos, double fluidHeight, RandomSource randomSource) {
        if (randomSource.nextDouble() <= ClientConfigs.AMBIENT_PARTICLE_CHANCE.get()) {
            double x = blockPos.getX() + randomSource.nextDouble();
            double y = blockPos.getY() + fluidHeight;
            double z = blockPos.getZ() + randomSource.nextDouble();

            level.addParticle(ModParticles.RIFT_FLAME.get(), x, y, z, 0.0, 0.0, 0.0);
        }
    }

    public static void sound(Level level, BlockPos blockPos, RandomSource randomSource) {
        if (randomSource.nextDouble() <= ClientConfigs.AMBIENT_SOUND_CHANCE.get()) {
            float volume = (float) getAttenuationDist(randomSource) / 16.0F;
            float pitch = 1.0F + (float) getPitchDeviation(randomSource);

            level.playLocalSound(blockPos, ModSoundEvents.DIMENSIONAL_TEARS_AMBIENT.get(), SoundSource.BLOCKS, volume, pitch, false);
        }
    }

    private static double getPitchDeviation(RandomSource randomSource) {
        double maxDeviation = ClientConfigs.MAX_SOUND_PITCH_DEVIATION.get();
        return randomSource.nextDouble() * maxDeviation * 2 - maxDeviation;
    }

    private static double getAttenuationDist(RandomSource randomSource) {
        int multiplier = ClientConfigs.MAX_SOUND_ATTENUATION_DIST_MULTIPLIER.get();
        return ClientConfigs.MIN_SOUND_ATTENUATION_DIST.get() + randomSource.nextDouble() * multiplier;
    }

}
