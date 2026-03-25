package com.ordana.dimensional_tears.blocks;

import com.google.common.collect.Lists;
import com.ordana.dimensional_tears.configs.CommonConfigs;
import com.ordana.dimensional_tears.reg.ModSoundEvents;
import com.ordana.dimensional_tears.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Consumer;

public final class FluidInteractionHelper {

    public static boolean tryGenerate(Level level, BlockPos blockPos, FluidState fluidState) {
        if (fluidState.is(FluidTags.WATER))
            return createDimensionalTearsInteraction(level, blockPos, Lists.newArrayList(Direction.values()), Blocks.ICE, serverLevel -> serverLevel.playSound(null, blockPos, ModSoundEvents.WATER_FREEZE.get(), SoundSource.BLOCKS, 0.5F, 1.0F));

        if (fluidState.is(FluidTags.LAVA))
            return createDimensionalTearsInteraction(level, blockPos, LiquidBlock.POSSIBLE_FLOW_DIRECTIONS, getLavaInteractionResult(level, level.getFluidState(blockPos), Blocks.END_STONE), serverLevel -> serverLevel.levelEvent(LevelEvent.LAVA_FIZZ, blockPos, 0));

        if (fluidState.is(ModTags.DIMENSIONAL_TEARS))
            return createInteraction(level, blockPos, Lists.newArrayList(Direction.DOWN), getLavaInteractionResult(level, level.getFluidState(blockPos), null), FluidTags.LAVA, serverLevel -> serverLevel.levelEvent(LevelEvent.LAVA_FIZZ, blockPos, 0));

        return false;
    }

    @Nullable
    private static Block getLavaInteractionResult(Level level, FluidState fluidState, @Nullable Block ifNotSource) {
        if (fluidState.isSource())
            return level.getRandom().nextDouble() <= CommonConfigs.LAVA_INTERACTION_CRYING_OBSIDIAN_CHANCE.get() ? Blocks.CRYING_OBSIDIAN : Blocks.OBSIDIAN;

        return ifNotSource;
    }

    private static boolean createDimensionalTearsInteraction(Level level, BlockPos blockPos, Collection<Direction> directions, @Nullable Block block, Consumer<ServerLevel> onServerGenerated) {
        return createInteraction(level, blockPos, directions, block, ModTags.DIMENSIONAL_TEARS, onServerGenerated);
    }

    private static boolean createInteraction(Level level, BlockPos blockPos, Collection<Direction> directions, Block block, TagKey<Fluid> fluidTag, Consumer<ServerLevel> onServerGenerated) {
        boolean generated = false;

        for (Direction direction : directions) {
            BlockPos relativePos = blockPos.relative(direction.getOpposite());
            FluidState relativeFluid = level.getFluidState(relativePos);

            if (relativeFluid.is(fluidTag) && block != null) {
                level.setBlockAndUpdate(blockPos, block.defaultBlockState());

                if (level instanceof ServerLevel serverLevel)
                    onServerGenerated.accept(serverLevel);

                generated = true;
            }
        }

        return generated;
    }

}
