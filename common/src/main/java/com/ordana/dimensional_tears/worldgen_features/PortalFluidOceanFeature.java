package com.ordana.dimensional_tears.worldgen_features;

import com.mojang.serialization.Codec;
import com.ordana.dimensional_tears.configs.CommonConfigs;
import com.ordana.dimensional_tears.reg.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class PortalFluidOceanFeature extends Feature<PortalFluidOceanConfig> {
    public PortalFluidOceanFeature(Codec<PortalFluidOceanConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<PortalFluidOceanConfig> context) {
        if (!CommonConfigs.DIMENSIONAL_TEARS_OCEAN.get())
            return false;

        PortalFluidOceanConfig config = context.config();

        BlockPos originPos = context.origin();
        WorldGenLevel worldGenLevel = context.level();
        ChunkAccess cachedChunk = worldGenLevel.getChunk(originPos);

        var getX = (originPos.getX() & ~15);
        var getZ = (originPos.getZ() & ~15);
        var getY = (config.floorElevation());

        for (int x = getX; x < getX + 16; x++) {
            for (int z = getZ; z < getZ + 16; z++) {
                for (int y = getY; y <= config.surfaceElevation(); y++) {
                    BlockPos currentPos = new BlockPos(x, y, z);
                    if (cachedChunk.getBlockState(currentPos).isAir()) {
                        worldGenLevel.setBlock(currentPos, ModBlocks.DIMENSIONAL_TEARS.get().defaultBlockState(), Block.UPDATE_CLIENTS);
                    }
                }
            }
        }
        return false;
    }
}