package com.ordana.dimensional_tears.mixins.dimensional_tears;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.ordana.dimensional_tears.blocks.FluidInteractionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LiquidBlock.class)
public class FluidInteractionMixin {

    @Shadow @Final protected FlowingFluid fluid;

    @WrapMethod(method = "shouldSpreadLiquid")
    private boolean attemptFluidInteraction(Level level, BlockPos blockPos, BlockState blockState, Operation<Boolean> original) {
        return original.call(level, blockPos, blockState) && !FluidInteractionHelper.tryGenerate(level, blockPos, this.fluid.defaultFluidState());
    }

}
