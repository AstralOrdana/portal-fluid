package com.ordana.dimensional_tears.mixins.nether_portal;

import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.configs.CommonConfigs;
import com.ordana.dimensional_tears.reg.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
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
    public void bringToTears(BlockState blockState, LevelReader levelAccessor, ScheduledTickAccess ticks, BlockPos blockPos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random, CallbackInfoReturnable<BlockState> cir) {
        if (levelAccessor instanceof ServerLevel serverLevel) {

            Direction.Axis axis = blockState.getValue(AXIS);
            PortalShape portalShape = PortalShape.findAnyShape(levelAccessor, blockPos, axis);

            double chance = DimensionalTearsRoot.CONFIG.obtaining.PORTAL_DESTRUCTION_CRYING_OBSIDIAN_CHANCE;

            if (portalShape.isComplete() || chance <= 0.0)
                return;

            for (Direction potentialCryingDirection : Direction.values()) {
                Direction.Axis axis2 = potentialCryingDirection.getAxis();
                if (axis2.isHorizontal() && potentialCryingDirection.getClockWise().getAxis() == axis)
                    continue;

                BlockPos potentialCryingPos = blockPos.relative(potentialCryingDirection);
                BlockState potentialCryingState = levelAccessor.getBlockState(potentialCryingPos);

                if (random.nextDouble() <= chance && potentialCryingState.is(Blocks.OBSIDIAN)) {
                    serverLevel.setBlock(potentialCryingPos, Blocks.CRYING_OBSIDIAN.defaultBlockState(), Block.UPDATE_ALL);
                    // TODO next release: improve this impl so that it doesn't depend on the config option being set above 0
                    serverLevel.playSound(null, blockPos, ModSoundEvents.PORTAL_DESTROY.value(), SoundSource.BLOCKS);
                }
            }
        }
    }

}
