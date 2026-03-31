package com.ordana.dimensional_tears.blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ordana.dimensional_tears.items.DimensionalTearsBottleItem;
import com.ordana.dimensional_tears.items.DimensionalTearsBucketItem;
import com.ordana.dimensional_tears.reg.ModBlocks;
import com.ordana.dimensional_tears.reg.ModItems;
import com.ordana.dimensional_tears.util.DimensionalTearsAmbience;
import com.ordana.dimensional_tears.util.TeleportHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

public class DimensionalTearsCauldronBlock extends AbstractCauldronBlock {

    public static final MapCodec<DimensionalTearsCauldronBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
        .group(
            propertiesCodec(),
            CauldronInteraction.CODEC.fieldOf("interactions").forGetter(portalFluidCauldronBlock -> portalFluidCauldronBlock.interactions)
        )
        .apply(instance, DimensionalTearsCauldronBlock::new)
    );

    public static final int MIN_FILL_LEVEL = 1;
    public static final int MAX_FILL_LEVEL = 3;
    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_CAULDRON;
    private static final int BASE_CONTENT_HEIGHT = 6;
    private static final double HEIGHT_PER_LEVEL = 3.0;

    public DimensionalTearsCauldronBlock(Properties properties, CauldronInteraction.InteractionMap map) {
        super(properties, map);
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, MIN_FILL_LEVEL));
    }

    @Override
    @NotNull
    protected MapCodec<? extends AbstractCauldronBlock> codec() {
        return CODEC;
    }

    @Override
    protected double getContentHeight(BlockState blockState) {
        return (BASE_CONTENT_HEIGHT + blockState.getValue(LEVEL) * HEIGHT_PER_LEVEL) / 16.0;
    }

    @Override
    public boolean isFull(BlockState state) {
        return state.getValue(LEVEL) == MAX_FILL_LEVEL;
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        DimensionalTearsAmbience.tryAnimate(level, blockPos.above(), this.getContentHeight(blockState), randomSource);
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (level instanceof ServerLevel serverLevel && this.isEntityInsideContent(blockState, blockPos, entity) && TeleportHelper.canTeleportTo(entity))
            TeleportHelper.tryDelegateTeleportationToRiftingEffect(serverLevel, entity, false, null);
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

    public static boolean canEmptyInto(BlockState blockState) {
        Block block = blockState.getBlock();
        return block instanceof CauldronBlock || block instanceof DimensionalTearsCauldronBlock && blockState.getValue(LEVEL) < MAX_FILL_LEVEL;
    }

    public static BlockState getNextCauldronState(BlockState blockState) {
        Block block = ModBlocks.DIMENSIONAL_TEARS_CAULDRON.get();
        return blockState.is(block) ? blockState.cycle(LEVEL) : block.defaultBlockState();
    }

    public static BlockState getPreviousCauldronState(BlockState blockState) {
        if (blockState.getValue(LEVEL) == MIN_FILL_LEVEL)
            return Blocks.CAULDRON.defaultBlockState();

        return blockState.setValue(LEVEL, blockState.getValue(LEVEL) - 1);
    }

    public static InteractionResult tryCollectWithBottle(ItemStack itemStack, BlockPos blockPos, BlockState blockState, Player player, Level level, InteractionHand interactionHand) {
        DimensionalTearsBottleItem.playSound(level, blockPos, player, true);
        ItemStack filledResult = ItemUtils.createFilledResult(itemStack, player, ModItems.DIMENSIONAL_TEARS_BOTTLE.get().getDefaultInstance());

        player.setItemInHand(interactionHand, filledResult);
        level.setBlockAndUpdate(blockPos, getPreviousCauldronState(blockState));

        if (player instanceof ServerPlayer serverPlayer)
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, itemStack);

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    public static InteractionResult tryCollectWithBucket(ItemStack itemStack, BlockPos blockPos, BlockState blockState, Player player, Level level, InteractionHand interactionHand) {
        if (blockState.getValue(LEVEL) < MAX_FILL_LEVEL)
            return InteractionResult.PASS;

        DimensionalTearsBucketItem.playSound(level, blockPos, player, true);
        ItemStack filledResult = ItemUtils.createFilledResult(itemStack, player, ModItems.DIMENSIONAL_TEARS_BUCKET.get().getDefaultInstance());

        player.setItemInHand(interactionHand, filledResult);
        level.setBlockAndUpdate(blockPos, Blocks.CAULDRON.defaultBlockState());

        if (player instanceof ServerPlayer serverPlayer)
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, itemStack);

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    @NotNull
    public ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return Items.CAULDRON.getDefaultInstance();
    }

}