package com.ordana.dimensional_tears.blocks;

import com.google.common.collect.Lists;
import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.configs.CommonConfigs;
import com.ordana.dimensional_tears.reg.ModSoundEvents;
import com.ordana.dimensional_tears.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FluidState;

import java.util.List;

public final class FluidInteractionHelper {

    private static Block getLavaInteractionResult(Level level, FluidState fluidState) {
        if (fluidState.isSource())
            return level.getRandom().nextDouble() <= DimensionalTearsRoot.CONFIG.obtaining.LAVA_INTERACTION_CRYING_OBSIDIAN_CHANCE ? Blocks.CRYING_OBSIDIAN : Blocks.OBSIDIAN;

        return Blocks.END_STONE;
    }

    public static void playWaterFreezeSound(LevelAccessor levelAccessor, BlockPos blockPos) {
        levelAccessor.playSound(null, blockPos, ModSoundEvents.WATER_FREEZE.value(), SoundSource.BLOCKS, 0.5F, 1.0F);
    }

    public static boolean tryInteract(Level level, BlockPos currentPos, FluidState currentState) {
        boolean isWater = currentState.is(FluidTags.WATER);
        List<Direction> directions = isWater ? Lists.newArrayList(Direction.values()) : LiquidBlock.POSSIBLE_FLOW_DIRECTIONS;

        for (Direction direction : directions) {
            BlockPos relativePos = currentPos.relative(direction.getOpposite());

            if (level.getFluidState(relativePos).is(ModTags.DIMENSIONAL_TEARS)) {
                if (currentState.is(FluidTags.WATER)) {
                    level.setBlockAndUpdate(currentPos, Blocks.ICE.defaultBlockState());
                    playWaterFreezeSound(level, currentPos);

                    return true;
                }

                if (currentState.is(FluidTags.LAVA)) {
                    level.setBlockAndUpdate(currentPos, getLavaInteractionResult(level, currentState).defaultBlockState());
                    level.levelEvent(LevelEvent.LAVA_FIZZ, currentPos, 0);

                    return true;
                }
            }
        }

        return false;
    }

}
