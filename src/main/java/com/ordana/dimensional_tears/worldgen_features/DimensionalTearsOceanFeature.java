package com.ordana.dimensional_tears.worldgen_features;

import com.mojang.serialization.Codec;
import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.blocks.DimensionalTearsBlock;
import com.ordana.dimensional_tears.configs.CommonConfigs;
import com.ordana.dimensional_tears.reg.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class DimensionalTearsOceanFeature extends Feature<DimensionalTearsOceanConfig> {

    public DimensionalTearsOceanFeature(Codec<DimensionalTearsOceanConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<DimensionalTearsOceanConfig> context) {
        if (!DimensionalTearsRoot.CONFIG.fluids.DIMENSIONAL_TEARS_OCEAN)
            return false;

        DimensionalTearsOceanConfig config = context.config();

        BlockPos originPos = context.origin();
        WorldGenLevel worldGenLevel = context.level();
        ChunkAccess cachedChunk = worldGenLevel.getChunk(originPos);

        int getX = originPos.getX() & ~15;
        int getZ = originPos.getZ() & ~15;
        int getY = config.floorElevation();

        for (BlockPos blockPos : BlockPos.betweenClosed(getX, getY, getZ, getX + 16, config.surfaceElevation(), getZ + 16)) {
            if (!cachedChunk.getBlockState(blockPos).isAir())
                continue;

            BlockState blockState = ModBlocks.DIMENSIONAL_TEARS.get()
                .defaultBlockState()
                .setValue(DimensionalTearsBlock.IS_OCEAN, true);

            worldGenLevel.setBlock(blockPos, blockState, Block.UPDATE_CLIENTS);
        }

        return false;
    }

}