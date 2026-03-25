package com.ordana.dimensional_tears.mixins.dimensional_tears.fluid_interaction;

import com.ordana.dimensional_tears.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FlowingFluid.class)
public class FlowingFluidMixin {

    @Inject(method = "spreadTo", at = @At("HEAD"))
    private void handleDelayedDownwardFlowing(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, Direction direction, FluidState fluidState, CallbackInfo ci) {
        if (direction != Direction.DOWN || !(blockState.getBlock() instanceof LiquidBlock))
            return;

        FluidState relativeState = levelAccessor.getFluidState(blockPos);

        if (relativeState.is(ModTags.DIMENSIONAL_TEARS) && fluidState.is(FluidTags.LAVA)) {
            levelAccessor.setBlock(blockPos, Blocks.END_STONE.defaultBlockState(), Block.UPDATE_ALL);
            levelAccessor.levelEvent(LevelEvent.LAVA_FIZZ, blockPos, 0);
        }
    }

}
