package com.ordana.dimensional_tears.util;

import com.ordana.dimensional_tears.reg.ModParticles;
import com.ordana.dimensional_tears.reg.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

public final class DimensionalTearsVisuals {

    private static final int DEFAULT_SOUND_RARITY = 200;
    private static final int DEFAULT_PARTICLE_RARITY = 20;
    private static final float MIN_SOUND_VOLUME = 0.2F;
    private static final float MAX_SOUND_VOLUME = 0.4F;
    private static final float PITCH_DEVIANCE = 0.1F;
    private static final double PARTICLE_VERTICAL_OFFSET = 0.2;

    public static void onAnimateTick(Level level, BlockPos blockPos, RandomSource randomSource) {
        BlockPos above = blockPos.above();

        if (level.isEmptyBlock(above) && !level.getBlockState(above).isSolidRender(level, above)) {
            particle(level, blockPos, randomSource, DEFAULT_PARTICLE_RARITY);
            sound(level, blockPos, randomSource, DEFAULT_SOUND_RARITY);
        }
    }

    public static void particle(Level level, BlockPos blockPos, RandomSource randomSource, int rarity) {
        if (randomSource.nextInt(rarity) == 0) {
            double x = blockPos.getX() + randomSource.nextDouble();
            double y = blockPos.getY() + PARTICLE_VERTICAL_OFFSET;
            double z = blockPos.getZ() + randomSource.nextDouble();

            level.addParticle(ModParticles.RIFT_FLAME.get(), x, y, z, 0.0, 0.0, 0.0);
        }
    }

    public static void sound(Level level, BlockPos blockPos, RandomSource randomSource, int rarity) {
        if (randomSource.nextInt(rarity) == 0) {
            float volume = Mth.randomBetween(randomSource, MIN_SOUND_VOLUME, MAX_SOUND_VOLUME);
            float pitch = 1.0F + Mth.randomBetween(randomSource, -PITCH_DEVIANCE, PITCH_DEVIANCE);

            level.playLocalSound(blockPos, ModSoundEvents.DIMENSIONAL_TEARS_AMBIENT.get(), SoundSource.BLOCKS, volume, pitch, false);
        }
    }

}
