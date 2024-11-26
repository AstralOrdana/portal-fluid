package com.ordana.portal_fluid.fabric;

import com.ordana.portal_fluid.configs.CommonConfigs;
import com.ordana.portal_fluid.reg.LevelHelper;
import com.ordana.portal_fluid.reg.ModSoundEvents;
import com.ordana.portal_fluid.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.Optional;

public class PortalFluidBlock extends LiquidBlock {

    public PortalFluidBlock(FlowingFluid flowingFluid, Properties properties) {
        super(flowingFluid, properties);
    }

    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        setTickCounter(0);
    }

    private int tickCounter = 0;

    public int setTickCounter(int tick) {
        return tickCounter = tick;
    }

    @Override
    public ItemStack pickupBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        Optional<? extends Registry<DimensionType>> registry = level.registryAccess().registry(Registries.DIMENSION_TYPE);

        if (registry.isPresent() && level.dimensionType() == registry.get().get(BuiltinDimensionTypes.END) && level.getMinBuildHeight() + 3 >= pos.getY() && !CommonConfigs.END_OCEAN_BUCKETABLE.get()) {
            return ItemStack.EMPTY;
        }

        return super.pickupBlock(level, pos, state);
    }


    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {

        if (entity.getType().is(ModTags.PORTAL_FLUID_IMMUNE)
                || !entity.isInWater()
                || entity.isPassenger()
                || entity.isVehicle()
                || !entity.canChangeDimensions()
                || entity.isCrouching()
                || pos.equals(level.getSharedSpawnPos())) return;
        if (entity instanceof ServerPlayer player && player.isSecondaryUseActive()) return;

        if (CommonConfigs.INSTANT_TELEPORTATION.get()) {
            if (entity instanceof ServerPlayer player) LevelHelper.teleportToSpawnPosition(player);
            else LevelHelper.teleportToWorldspawn(level, entity);
            entity.playSound(ModSoundEvents.PORTAL_FLUID_TELEPORT.get(), 0.5f, 1.0f);
        }
        else {
            tickCounter++;

            if (this.tickCounter < 1) {
                entity.playSound(ModSoundEvents.PORTAL_FLUID_SUBMERGE.get(), 1.0f, 1.0f);
            }
            level.scheduleTick(pos, this, 120);
            if (this.tickCounter >= 100) {


                if (entity instanceof ServerPlayer player) LevelHelper.teleportToSpawnPosition(player);
                else LevelHelper.teleportToWorldspawn(level, entity);
                entity.playSound(ModSoundEvents.PORTAL_FLUID_TELEPORT.get(), 0.5f, 1.0f);


                setTickCounter(0);

            }
        }
    }

}
