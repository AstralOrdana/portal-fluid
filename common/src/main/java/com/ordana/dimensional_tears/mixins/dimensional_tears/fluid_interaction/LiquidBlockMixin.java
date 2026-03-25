package com.ordana.dimensional_tears.mixins.dimensional_tears.fluid_interaction;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.ordana.dimensional_tears.blocks.FluidInteractionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LiquidBlock.class)
public class LiquidBlockMixin {

    @WrapWithCondition(method = { "onPlace", "neighborChanged" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;scheduleTick(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/material/Fluid;I)V"))
    private boolean attemptFluidInteraction(Level instance, BlockPos blockPos, Fluid fluid, int i) {
        return !FluidInteractionHelper.tryInteract(instance, blockPos, fluid.defaultFluidState());
    }

}
