package com.ordana.dimensional_tears.blocks;

import com.ordana.dimensional_tears.DimensionalTearsPlatform;
import com.ordana.dimensional_tears.configs.CommonConfigs;
import com.ordana.dimensional_tears.util.TeleportHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class DimensionalTearsBlock extends LiquidBlock {

    public static final BooleanProperty IS_OCEAN = BooleanProperty.create("is_ocean");

    public DimensionalTearsBlock(Supplier<FlowingFluid> flowingFluid, Properties properties) {
        super(flowingFluid.get(), properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(IS_OCEAN, false));
    }

    @Override
    @NotNull
    public ItemStack pickupBlock(Player player, LevelAccessor level, BlockPos pos, BlockState state) {
        return state.getValue(IS_OCEAN) && !CommonConfigs.END_OCEAN_BUCKETABLE.get() ? ItemStack.EMPTY : super.pickupBlock(player, level, pos, state);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos blockPos, Entity entity) {
        if (level instanceof ServerLevel serverLevel && DimensionalTearsPlatform.isInDimTears(entity) && TeleportHelper.canTeleportTo(entity) && !entity.isCrouching()) {
            TeleportHelper.tryDelegateTeleportationToRiftingEffect(serverLevel, entity, isFullySubmerged(level, blockPos, entity), null);
        }
    }

    /**
     * Fixes teleporting multiple times in the same tick and spamming the client with sounds.
     */
    private static boolean isFullySubmerged(Level level, BlockPos blockPos, Entity entity) {
        return level.getFluidState(blockPos).getShape(level, blockPos).bounds().move(blockPos).contains(entity.getEyePosition());
    }

    @Override
    @NotNull
    protected VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (blockState.getValue(IS_OCEAN) && blockGetter.getBlockState(blockPos.below()).isAir())
            return Shapes.block().move(0.0, -1.0, 0.0);

        return super.getCollisionShape(blockState, blockGetter, blockPos, collisionContext);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(IS_OCEAN));
    }

}