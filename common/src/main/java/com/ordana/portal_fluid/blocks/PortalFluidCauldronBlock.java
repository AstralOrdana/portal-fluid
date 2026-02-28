package com.ordana.portal_fluid.blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ordana.portal_fluid.particles.PortalFluidFlameParticle;
import com.ordana.portal_fluid.reg.TeleportHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 1));
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
        if (!this.isEntityInsideContent(state, pos, entity) || entity.isPassenger() || entity.isVehicle())
            return;

        if (entity instanceof ServerPlayer player) {
            TeleportHelper.teleportPlayerToSpawnPosition(player);
            this.handleEntityTeleport(state, level, pos);
        }
        else {
            TeleportHelper.teleportToWorldspawn(level, entity);
            level.playSound(null, entity.blockPosition(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    protected boolean canReceiveStalactiteDrip(Fluid fluid) {
        return fluid == Fluids.WATER;
    }

    protected void handleEntityTeleport(BlockState state, Level level, BlockPos pos) {
        lowerFillLevel(state, level, pos);
    }

    public static void lowerFillLevel(BlockState state, Level level, BlockPos pos) {
        int i = state.getValue(LEVEL) - 1;
        BlockState blockState = i == 0 ? Blocks.CAULDRON.defaultBlockState() : state.setValue(LEVEL, i);

        level.setBlockAndUpdate(pos, blockState);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(blockState));
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