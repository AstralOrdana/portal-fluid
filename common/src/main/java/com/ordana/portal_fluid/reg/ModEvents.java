package com.ordana.portal_fluid.reg;

import com.ordana.portal_fluid.blocks.PortalFluidCauldronBlock;
import com.ordana.portal_fluid.configs.CommonConfigs;
import com.ordana.portal_fluid.items.PortalFluidBottleItem;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CryingObsidianBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;

public class ModEvents {

    @FunctionalInterface
    public interface InteractionEvent {
        InteractionResult run(Item i, ItemStack stack,
                              BlockPos pos,
                              BlockState state,
                              Player player, Level level,
                              InteractionHand hand,
                              BlockHitResult hit);
    }

    private static final List<InteractionEvent> EVENTS = new ArrayList<>();

    static {
        EVENTS.add(ModEvents::obsidianDraining);
        EVENTS.add(ModEvents::portalCauldronLogic);
    }

    public static InteractionResult onBlockCLicked(ItemStack stack, Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.isEmpty()) return InteractionResult.PASS;
        Item i = stack.getItem();
        BlockPos pos = hitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);
        for (var event : EVENTS) {
            var result = event.run(i, stack, pos, state, player, level, hand, hitResult);
            if (result != InteractionResult.PASS) return result;
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult portalCauldronLogic(Item item, ItemStack stack, BlockPos pos, BlockState state,
                                                         Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (item == Items.GLASS_BOTTLE) {
            if (state.getBlock() instanceof PortalFluidCauldronBlock) {
                level.playSound(player, pos, ModSoundEvents.PORTAL_FLUID_BOTTLE_FILL.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                if (player instanceof ServerPlayer serverPlayer) {
                    ItemStack itemStack2 = ItemUtils.createFilledResult(stack, player, ModItems.PORTAL_FLUID_BOTTLE.get().getDefaultInstance());
                    player.setItemInHand(hand, itemStack2);
                    if (state.getValue(LayeredCauldronBlock.LEVEL) > 1) level.setBlockAndUpdate(pos, ModBlocks.PORTAL_CAULDRON.get().defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, state.getValue(LayeredCauldronBlock.LEVEL) - 1));
                    else level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());

                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);

            }
        }
        else if (item == Items.BUCKET) {
            if (state.getBlock() instanceof PortalFluidCauldronBlock && state.getValue(LayeredCauldronBlock.LEVEL) == 3) {
                level.playSound(player, pos, ModSoundEvents.PORTAL_FLUID_BUCKET_FILL.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                if (player instanceof ServerPlayer serverPlayer) {
                    ItemStack itemStack2 = ItemUtils.createFilledResult(stack, player, ModItems.PORTAL_FLUID_BUCKET.get().getDefaultInstance());
                    player.setItemInHand(hand, itemStack2);
                    level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());

                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }

    private static InteractionResult obsidianDraining(Item item, ItemStack stack, BlockPos pos, BlockState state,
                                                      Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (item == ModItems.PORTAL_FLUID_BOTTLE.get()) {
            if (state.is(Blocks.CAULDRON) || (state.getBlock() instanceof PortalFluidCauldronBlock && state.getValue(LayeredCauldronBlock.LEVEL) < 3)) {
                level.playSound(player, pos, ModSoundEvents.PORTAL_FLUID_BOTTLE_EMPTY.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                if (player instanceof ServerPlayer serverPlayer) {
                    ItemStack itemStack2 = ItemUtils.createFilledResult(stack, player, Items.GLASS_BOTTLE.getDefaultInstance());
                    player.setItemInHand(hand, itemStack2);
                    if (state.is(Blocks.CAULDRON)) level.setBlockAndUpdate(pos, ModBlocks.PORTAL_CAULDRON.get().defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 1));
                    else level.setBlockAndUpdate(pos, ModBlocks.PORTAL_CAULDRON.get().defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, state.getValue(LayeredCauldronBlock.LEVEL) + 1));

                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);

            }
            return InteractionResult.PASS;
        }
        if (item == Items.GLASS_BOTTLE) {
            if (state.getBlock() instanceof CryingObsidianBlock && CommonConfigs.CRYING_OBSIDIAN_PORTAL_FLUID.get()) {
                level.playSound(player, pos, ModSoundEvents.PORTAL_FLUID_BOTTLE_FILL.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
                ParticleUtils.spawnParticlesOnBlockFaces(level, pos, ParticleTypes.FALLING_OBSIDIAN_TEAR, UniformInt.of(3, 5));
                if (player instanceof ServerPlayer serverPlayer) {
                    ItemStack itemStack2 = ItemUtils.createFilledResult(stack, player, ModItems.PORTAL_FLUID_BOTTLE.get().getDefaultInstance());
                    player.setItemInHand(hand, itemStack2);
                    //if (!player.getAbilities().instabuild) stack.shrink(1);
                    level.setBlockAndUpdate(pos, Blocks.OBSIDIAN.defaultBlockState());
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);

            }
            if (state.getBlock() instanceof RespawnAnchorBlock && state.getValue(RespawnAnchorBlock.CHARGE) > 0 && CommonConfigs.RESPAWN_ANCHOR_PORTAL_FLUID.get()) {
                level.playSound(player, pos, ModSoundEvents.PORTAL_FLUID_BOTTLE_FILL.get(),SoundSource.BLOCKS, 1.0f, 1.0f);
                ParticleUtils.spawnParticlesOnBlockFaces(level, pos, ParticleTypes.FALLING_OBSIDIAN_TEAR, UniformInt.of(3, 5));
                if (player instanceof ServerPlayer serverPlayer) {

                    ItemStack itemStack2 = new ItemStack(ModItems.PORTAL_FLUID_BOTTLE.get());
                    PortalFluidBottleItem.addLocationTags(level.dimension(), pos, itemStack2.getOrCreateTag());

                    if (!player.getInventory().add(itemStack2)) {
                        player.drop(itemStack2, false);
                    }

                    stack.shrink(1);

                    level.setBlockAndUpdate(pos, state.setValue(RespawnAnchorBlock.CHARGE, state.getValue(RespawnAnchorBlock.CHARGE) - 1));
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);

            }
        }
        return InteractionResult.PASS;
    }

}