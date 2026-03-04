package com.ordana.dimensional_tears.mixins.nether_portal;

import com.ordana.dimensional_tears.configs.CommonConfigs;
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
    public void bringToTears(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2, CallbackInfoReturnable<BlockState> cir) {
        if (levelAccessor.isClientSide())
            return;

        Direction.Axis axis = blockState.getValue(AXIS);
        PortalShape portalShape = new PortalShape(levelAccessor, blockPos, axis);

        float chance = CommonConfigs.PORTAL_DESTRUCTION_CRYING_OBSIDIAN_CHANCE.get();

        if (!portalShape.isComplete() && chance > 0) {
            RandomSource random = levelAccessor.getRandom();

            for (Direction potentialCryingDirection : Direction.values()) {
                Direction.Axis axis2 = potentialCryingDirection.getAxis();
                if (axis2.isHorizontal() && potentialCryingDirection.getClockWise().getAxis() == axis)
                    continue;

                BlockPos potentialCryingPos = blockPos.relative(potentialCryingDirection);
                BlockState potentialCryingState = levelAccessor.getBlockState(potentialCryingPos);

                if (random.nextDouble() <= chance && potentialCryingState.is(Blocks.OBSIDIAN))
                    levelAccessor.setBlock(potentialCryingPos, Blocks.CRYING_OBSIDIAN.defaultBlockState(), Block.UPDATE_ALL);
            }
        }
    }

}
