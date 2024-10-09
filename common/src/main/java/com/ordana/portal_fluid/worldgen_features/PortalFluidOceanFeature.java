package com.ordana.portal_fluid.worldgen_features;

import com.mojang.serialization.Codec;
import com.ordana.portal_fluid.configs.CommonConfigs;
import com.ordana.portal_fluid.reg.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class PortalFluidOceanFeature extends Feature<NoneFeatureConfiguration> {
    public PortalFluidOceanFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        if (!CommonConfigs.PORTAL_FLUID_OCEAN.get()) return false;

        BlockPos originPos = context.origin();
        WorldGenLevel worldGenLevel = context.level();
        ChunkAccess cachedChunk = worldGenLevel.getChunk(originPos);

        var getX = (originPos.getX() & ~15);
        var getZ = (originPos.getZ() & ~15);
        var getY = (originPos.getY() & ~3);

        for (int x = getX; x < getX + 16; x++) {
            for (int z = getZ; z < getZ + 16; z++) {
                for (int y = getY; y < 4; y++) {
                    BlockPos currentPos = new BlockPos(x, y, z);
                    if (cachedChunk.getBlockState(currentPos).isAir()) {
                        worldGenLevel.setBlock(currentPos, ModBlocks.PORTAL_FLUID.get().defaultBlockState(), Block.UPDATE_CLIENTS);
                    }
                }
            }
        }
        return false;
    }
}