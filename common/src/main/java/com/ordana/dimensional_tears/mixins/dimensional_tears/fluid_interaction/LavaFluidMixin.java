package com.ordana.dimensional_tears.mixins.dimensional_tears.fluid_interaction;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.ordana.dimensional_tears.reg.ModTags;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.LavaFluid;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LavaFluid.class)
public class LavaFluidMixin {

    @ModifyReturnValue(method = "canBeReplacedWith", at = @At("RETURN"))
    private boolean canReplaceDimensionalTears(boolean original, @Local(argsOnly = true) FluidState fluidState, @Local(argsOnly = true) Direction direction) {
        return original || direction == Direction.DOWN && fluidState.is(ModTags.DIMENSIONAL_TEARS);
    }

    @WrapOperation(method = "spreadTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean replaceDimensionalTears(FluidState instance, TagKey<Fluid> tagKey, Operation<Boolean> original, @Share("replacingTears") LocalBooleanRef replacingTearsRef) {
        if (original.call(instance, tagKey))
            return true;

        boolean replacingTears = original.call(instance, ModTags.DIMENSIONAL_TEARS);
        replacingTearsRef.set(replacingTears);

        return replacingTears;
    }

    @ModifyExpressionValue(method = "spreadTo", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/block/Blocks;STONE:Lnet/minecraft/world/level/block/Block;", opcode = Opcodes.GETSTATIC))
    private Block placeEndStone(Block original, @Share("replacingTears") LocalBooleanRef replacingTearsRef) {
        return replacingTearsRef.get() ? Blocks.END_STONE : original;
    }

}
