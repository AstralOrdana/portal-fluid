package com.ordana.portal_fluid.blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ordana.portal_fluid.particles.PortalFluidFlameParticle;
import com.ordana.portal_fluid.util.TeleportHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

public class PortalFluidCauldronBlock extends AbstractCauldronBlock {

    public static final MapCodec<PortalFluidCauldronBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
        .group(
            propertiesCodec(),
            CauldronInteraction.CODEC.fieldOf("interactions").forGetter(portalFluidCauldronBlock -> portalFluidCauldronBlock.interactions)
        )
        .apply(instance, PortalFluidCauldronBlock::new)
    );

    public static final int MIN_FILL_LEVEL = 1;
    public static final int MAX_FILL_LEVEL = 3;
    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_CAULDRON;
    private static final int BASE_CONTENT_HEIGHT = 6;
    private static final double HEIGHT_PER_LEVEL = 3.0D;

    public PortalFluidCauldronBlock(Properties properties, CauldronInteraction.InteractionMap map) {
        super(properties, map);
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, MIN_FILL_LEVEL));
    }

    @Override
    @NotNull
    protected MapCodec<? extends AbstractCauldronBlock> codec() {
        return CODEC;
    }

    @Override
    protected double getContentHeight(BlockState state) {
        return (BASE_CONTENT_HEIGHT + state.getValue(LEVEL) * HEIGHT_PER_LEVEL) / 16.0D;
    }

    @Override
    public boolean isFull(BlockState state) {
        return state.getValue(LEVEL) == MAX_FILL_LEVEL;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        PortalFluidFlameParticle.onAnimateTick(level, pos.above(), random);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level instanceof ServerLevel serverLevel && this.isEntityInsideContent(state, pos, entity) && TeleportHelper.canTeleportTo(serverLevel, entity, pos))
            TeleportHelper.tryDelegateTeleportationToRiftingEffect(serverLevel, entity);
    }

    @Override
    protected boolean canReceiveStalactiteDrip(Fluid fluid) {
        return fluid == Fluids.WATER;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return state.getValue(LEVEL);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    @Override
    protected void receiveStalactiteDrip(BlockState state, Level level, BlockPos pos, Fluid fluid) {
        if (!this.isFull(state)) {
            BlockState blockState = state.setValue(LEVEL, state.getValue(LEVEL) + 1);
            level.setBlockAndUpdate(pos, blockState);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(blockState));
            level.levelEvent(LevelEvent.SOUND_DRIP_WATER_INTO_CAULDRON, pos, 0);
        }
    }

}