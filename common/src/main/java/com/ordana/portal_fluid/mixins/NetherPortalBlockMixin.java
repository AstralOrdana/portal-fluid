package com.ordana.portal_fluid.mixins;

import com.ordana.portal_fluid.configs.CommonConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.portal.PortalShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NetherPortalBlock.class)
public class NetherPortalBlockMixin {

    @Shadow @Final public static EnumProperty<Direction.Axis> AXIS;

    @Inject(method = "updateShape", at = @At("HEAD"))
    public void bringToTears(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> cir) {
        if (!CommonConfigs.PORTAL_DESTRUCTION_CRYING_OBSIDIAN.get() || level.isClientSide())
            return;

        Direction.Axis axis2 = state.getValue(AXIS);
        RandomSource random = level.getRandom();

        for (Direction cryDir : Direction.values()) {
            BlockPos cryPos = currentPos.relative(cryDir);
            BlockState cryState = level.getBlockState(cryPos);

            if (random.nextInt(3) == 1 && cryState.is(Blocks.OBSIDIAN) && !new PortalShape(level, currentPos, axis2).isComplete())
                level.setBlock(cryPos, Blocks.CRYING_OBSIDIAN.defaultBlockState(), Block.UPDATE_ALL);
        }
    }
}
