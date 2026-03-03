package com.ordana.portal_fluid.reg;

import com.ordana.portal_fluid.blocks.PortalFluidCauldronBlock;
import com.ordana.portal_fluid.items.PortalFluidBottleItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;

public class ModEvents {

    private static final List<InteractionEvent> EVENTS = new ArrayList<>(List.of(
        ModEvents::glassBottle,
        ModEvents::bucket
    ));

    public static InteractionResult onBlockClicked(ItemStack itemStack, Player player, Level level, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (itemStack.isEmpty())
            return InteractionResult.PASS;

        BlockPos blockPos = blockHitResult.getBlockPos();
        BlockState blockState = level.getBlockState(blockPos);

        for (InteractionEvent event : EVENTS) {
            InteractionResult result = event.run(itemStack, blockPos, blockState, player, level, interactionHand, blockHitResult);

            if (result != InteractionResult.PASS)
                return result;
        }

        return InteractionResult.PASS;
    }

    private static InteractionResult bucket(ItemStack itemStack, BlockPos blockPos, BlockState blockState, Player player, Level level, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (itemStack.is(Items.BUCKET) && blockState.getBlock() instanceof PortalFluidCauldronBlock)
            return PortalFluidCauldronBlock.tryCollectWithBucket(itemStack, blockPos, blockState, player, level, interactionHand);

        return InteractionResult.PASS;
    }

    private static InteractionResult glassBottle(ItemStack itemStack, BlockPos blockPos, BlockState blockState, Player player, Level level, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!itemStack.is(Items.GLASS_BOTTLE))
            return InteractionResult.PASS;

        return switch (blockState.getBlock()) {
            case PortalFluidCauldronBlock ignored ->
                PortalFluidCauldronBlock.tryCollectWithBottle(itemStack, blockPos, blockState, player, level, interactionHand);
            case CryingObsidianBlock ignored ->
                PortalFluidBottleItem.tryCollectObsidianTears(level, blockPos, itemStack, player, interactionHand);
            case RespawnAnchorBlock ignored ->
                PortalFluidBottleItem.tryCollectRespawnAnchorTears(level, blockPos, blockState, itemStack, player, interactionHand);
            default -> InteractionResult.PASS;
        };
    }

    @FunctionalInterface
    public interface InteractionEvent {

        InteractionResult run(ItemStack itemStack, BlockPos blockPos, BlockState blockState, Player player, Level level, InteractionHand interactionHand, BlockHitResult blockHitResult);

    }

}