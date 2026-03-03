package com.ordana.portal_fluid.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

@FunctionalInterface
public interface InteractionEvent {

    InteractionResult run(ItemStack itemStack, BlockPos blockPos, BlockState blockState, Player player, Level level, InteractionHand interactionHand, BlockHitResult blockHitResult);

}