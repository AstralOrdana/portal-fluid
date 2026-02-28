package com.ordana.portal_fluid.blocks;

import com.ordana.portal_fluid.configs.CommonConfigs;
import com.ordana.portal_fluid.reg.ModSoundEvents;
import com.ordana.portal_fluid.reg.TeleportHelper;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.FlowingFluid;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class PortalFluidBlock extends LiquidBlock {

    private final Predicate<Entity> inFluidPredicate;
    private int tickCounter = 0;

    public PortalFluidBlock(Supplier<FlowingFluid> flowingFluid, Properties properties, Predicate<Entity> inFluidPredicate) {
        super(flowingFluid.get(), properties);
        this.inFluidPredicate = inFluidPredicate;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.tickCounter = 0;
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
        if (!TeleportHelper.canTeleport(level, entity, pos) || !this.inFluidPredicate.test(entity))
            return;

        if (CommonConfigs.INSTANT_TELEPORTATION.get()) {
            TeleportHelper.teleportEntity(level, entity);
            return;
        }

        this.tickCounter++;

        if (this.tickCounter < 1)
            entity.playSound(ModSoundEvents.PORTAL_FLUID_SUBMERGE.get(), 1.0F, 1.0F);

        level.scheduleTick(pos, this, SharedConstants.TICKS_PER_SECOND * 6);

        if (this.tickCounter >= SharedConstants.TICKS_PER_SECOND * 5) {
            TeleportHelper.teleportEntity(level, entity);
            this.tickCounter = 0;
        }
    }

}