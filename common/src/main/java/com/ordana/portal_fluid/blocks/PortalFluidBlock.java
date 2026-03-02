package com.ordana.portal_fluid.blocks;

import com.ordana.portal_fluid.configs.CommonConfigs;
import com.ordana.portal_fluid.util.TeleportHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class PortalFluidBlock extends LiquidBlock {

    private final Predicate<Entity> inFluidPredicate;

    public PortalFluidBlock(Supplier<FlowingFluid> flowingFluid, Properties properties, Predicate<Entity> inFluidPredicate) {
        super(flowingFluid.get(), properties);
        this.inFluidPredicate = inFluidPredicate;
    }

    @Override
    @NotNull
    public ItemStack pickupBlock(Player player, LevelAccessor level, BlockPos pos, BlockState state) {
        Optional<? extends Registry<DimensionType>> registry = level.registryAccess().registry(Registries.DIMENSION_TYPE);

        if (registry.isPresent() && level.dimensionType() == registry.get().get(BuiltinDimensionTypes.END) && level.getMinBuildHeight() + 3 >= pos.getY() && !CommonConfigs.END_OCEAN_BUCKETABLE.get())
            return ItemStack.EMPTY;

        return super.pickupBlock(player, level, pos, state);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level instanceof ServerLevel serverLevel && this.inFluidPredicate.test(entity) && TeleportHelper.canTeleportTo(serverLevel, entity, pos))
            TeleportHelper.tryDelegateTeleportationToRiftingEffect(serverLevel, entity);
    }

    @Override
    @NotNull
    protected VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (blockGetter instanceof Level level && level.dimension() == Level.END && blockPos.getY() == 0)
            return Shapes.block().move(0.0, -1.0, 0.0);

        return super.getCollisionShape(blockState, blockGetter, blockPos, collisionContext);
    }

}