package com.ordana.dimensional_tears.items;

import com.mojang.logging.LogUtils;
import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.blocks.DimensionalTearsCauldronBlock;
import com.ordana.dimensional_tears.configs.CommonConfigs;
import com.ordana.dimensional_tears.reg.ModComponents;
import com.ordana.dimensional_tears.reg.ModItems;
import com.ordana.dimensional_tears.reg.ModSoundEvents;
import com.ordana.dimensional_tears.tooltip.RhymingGaslightTooltipItem;
import com.ordana.dimensional_tears.tooltip.RhymingGaslightTooltipState;
import com.ordana.dimensional_tears.util.TeleportHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class DimensionalTearsBottleItem extends Item implements RhymingGaslightTooltipItem {

    private static final Logger LOGGER = LogUtils.getLogger();
    public static final FoodProperties FOOD_PROPERTIES = new FoodProperties.Builder()
        .nutrition(0)
        .saturationModifier(0F)
        .alwaysEdible()
        .build();

    public DimensionalTearsBottleItem(Properties properties) {
        super(properties);
    }

    //Override
    //? neoforge {
    /*public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }
    *///?} else {
    @Override
    public boolean allowComponentsUpdateAnimation(Player player, InteractionHand hand, ItemStack originalStack, ItemStack updatedStack) {
        return false;
    }
    //?}


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.accept(RhymingGaslightTooltipState.getText());
        GlobalPos anchorPos = stack.get(ModComponents.ANCHOR_POS.get());

        if (anchorPos != null && DimensionalTearsRoot.CONFIG.capabilities.DIMENSIONAL_TEARS_DRINKING) {
            BlockPos blockPos = anchorPos.pos();
            tooltip.accept(Component.translatable("tooltip.dimensional_tears.dimensional_tears_pos", blockPos.getX(), blockPos.getY(), blockPos.getZ()).setStyle(Style.EMPTY.applyFormat(ChatFormatting.LIGHT_PURPLE)));
        }

//        if (!Translation.isShiftDown()) {
//            tooltip.add(Translation.CROUCH.component());
//            return;
//        }

//        tooltip.add(Translation.DIMENSIONAL_TEARS_1.component());
//        tooltip.add(Translation.DIMENSIONAL_TEARS_2.component());
//
//        boolean respawnAnchorFluid = CommonConfigs.RESPAWN_ANCHOR_DIMENSIONAL_TEARS.get();
//
//        if (CommonConfigs.CRYING_OBSIDIAN_DIMENSIONAL_TEARS.get())
//            tooltip.add(respawnAnchorFluid ? Translation.FROM_EITHER.component() : Translation.FROM_CRYING_OBSIDIAN.component());
//        else if (respawnAnchorFluid)
//            tooltip.add(Translation.FROM_RESPAWN_ANCHOR.component());
    }

    @Override
    @NotNull
    public InteractionResult useOn(@NotNull UseOnContext context) {
        Player player = context.getPlayer();

        if (player == null)
            return InteractionResult.PASS;

        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        ItemStack itemStack = context.getItemInHand();
        InteractionHand interactionHand = context.getHand();

        InteractionResult createPortalResult = tryCreatePortal(level, blockPos, itemStack, player, interactionHand, context.getClickedFace());
        InteractionResult emptyIntoCauldronResult = tryEmptyIntoCauldron(level, blockPos, itemStack, player, interactionHand);

        return createPortalResult == InteractionResult.PASS ? emptyIntoCauldronResult : createPortalResult;
    }

/*    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }*/

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @org.jspecify.annotations.Nullable EquipmentSlot slot) {
        if (level instanceof ServerLevel serverLevel) {
            GlobalPos globalPos = itemStack.get(ModComponents.ANCHOR_POS.get());

            if (globalPos == null)
                return;

            ResourceKey<Level> dimensionKey = globalPos.dimension();
            ServerLevel dimension = serverLevel.getServer().getLevel(dimensionKey);

            if (dimension == null)
                return;

            BlockState blockState = dimension.getBlockState(globalPos.pos());

            if (!(blockState.getBlock() instanceof RespawnAnchorBlock))
                itemStack.remove(ModComponents.ANCHOR_POS.get());
        }
    }

    @Override
    @NotNull
    public ItemStack finishUsingItem(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity livingEntity) {
        if (!DimensionalTearsRoot.CONFIG.capabilities.DIMENSIONAL_TEARS_DRINKING)
            return itemStack;

        if (level instanceof ServerLevel serverLevel && livingEntity instanceof ServerPlayer serverPlayer) {
            if (!serverPlayer.hasInfiniteMaterials()) {
                ItemStack filledResult = ItemUtils.createFilledResult(itemStack, serverPlayer, Items.GLASS_BOTTLE.getDefaultInstance());
                serverPlayer.setItemInHand(serverPlayer.getUsedItemHand(), filledResult);
            }

            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, itemStack);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));

            TeleportHelper.tryDelegateTeleportationToRiftingEffect(serverLevel, serverPlayer, false, itemStack);
        }

        return itemStack;
    }

    private static boolean inPortalDimension(@NotNull Level level) {
        ResourceKey<Level> dimensionKey = level.dimension();
        return dimensionKey == Level.OVERWORLD || dimensionKey == Level.NETHER;
    }

    private static InteractionResult tryCreatePortal(Level level, BlockPos blockPos, ItemStack itemStack, Player player, InteractionHand interactionHand, Direction clickedFace) {
        if (!inPortalDimension(level))
            return InteractionResult.PASS;

        Optional<PortalShape> optional = PortalShape.findEmptyPortalShape(level, blockPos.relative(clickedFace), Direction.Axis.X);

        if (optional.isEmpty())
            return InteractionResult.PASS;

        optional.get().createPortalBlocks(level);

        if (DimensionalTearsRoot.CONFIG.sounds.PORTAL_CREATION_SOUND)
            level.playSound(null, blockPos, ModSoundEvents.PORTAL_SPAWN.value(), SoundSource.BLOCKS);

        if (!player.hasInfiniteMaterials()) {
            ItemStack filledResult = ItemUtils.createFilledResult(itemStack, player, Items.GLASS_BOTTLE.getDefaultInstance());
            player.setItemInHand(interactionHand, filledResult);
        }

        return InteractionResult.SUCCESS;
    }

    private static InteractionResult tryEmptyIntoCauldron(Level level, BlockPos blockPos, ItemStack itemStack, Player player, InteractionHand interactionHand) {
        BlockState blockState = level.getBlockState(blockPos);

        if (!DimensionalTearsCauldronBlock.canEmptyInto(blockState))
            return InteractionResult.PASS;

        playSound(level, blockPos, player, false);

        if (!player.hasInfiniteMaterials()) {
            ItemStack filledResult = ItemUtils.createFilledResult(itemStack, player, Items.GLASS_BOTTLE.getDefaultInstance());
            player.setItemInHand(interactionHand, filledResult);
        }
 
        level.setBlockAndUpdate(blockPos, DimensionalTearsCauldronBlock.getNextCauldronState(blockState));

        if (player instanceof ServerPlayer serverPlayer)
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, itemStack);

        return InteractionResult.SUCCESS_SERVER;
    }

    public static InteractionResult tryCollectObsidianTears(Level level, BlockPos blockPos, ItemStack itemStack, Player player, InteractionHand interactionHand) {
        if (!DimensionalTearsRoot.CONFIG.obtaining.CRYING_OBSIDIAN_DIMENSIONAL_TEARS)
            return InteractionResult.PASS;

        ParticleUtils.spawnParticlesOnBlockFaces(level, blockPos, ParticleTypes.FALLING_OBSIDIAN_TEAR, UniformInt.of(3, 5));
        playSound(level, blockPos, player, true);

        ItemStack filledResult = ItemUtils.createFilledResult(itemStack, player, ModItems.DIMENSIONAL_TEARS_BOTTLE.get().getDefaultInstance());

        player.setItemInHand(interactionHand, filledResult);
        level.setBlockAndUpdate(blockPos, Blocks.OBSIDIAN.defaultBlockState());

        if (player instanceof ServerPlayer serverPlayer)
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, itemStack);

        return InteractionResult.SUCCESS_SERVER;
    }

    public static InteractionResult tryCollectRespawnAnchorTears(Level level, BlockPos blockPos, BlockState blockState, ItemStack itemStack, Player player, InteractionHand interactionHand) {
        if (blockState.getValue(RespawnAnchorBlock.CHARGE) == 0 || !DimensionalTearsRoot.CONFIG.obtaining.RESPAWN_ANCHOR_DIMENSIONAL_TEARS)
            return InteractionResult.PASS;

        ParticleUtils.spawnParticlesOnBlockFaces(level, blockPos, ParticleTypes.FALLING_OBSIDIAN_TEAR, UniformInt.of(3, 5));
        playSound(level, blockPos, player, true);

        ItemStack portalFluidBottle = ModItems.DIMENSIONAL_TEARS_BOTTLE.get().getDefaultInstance();
        portalFluidBottle.set(ModComponents.ANCHOR_POS.get(), new GlobalPos(level.dimension(), blockPos));

        ItemStack filledResult = ItemUtils.createFilledResult(itemStack, player, portalFluidBottle);

        player.setItemInHand(interactionHand, filledResult);
        level.setBlockAndUpdate(blockPos, blockState.setValue(RespawnAnchorBlock.CHARGE, blockState.getValue(RespawnAnchorBlock.CHARGE) - 1));

        if (player instanceof ServerPlayer serverPlayer)
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, itemStack);

        return InteractionResult.SUCCESS_SERVER;
    }

    public static void playSound(Level level, BlockPos blockPos, Player player, boolean fill) {
        SoundEvent soundEvent = fill ? ModSoundEvents.BOTTLE_FILL_DIMENSIONAL_TEARS.value() : ModSoundEvents.BOTTLE_EMPTY_DIMENSIONAL_TEARS.value();
        level.playSound(player, blockPos, soundEvent, SoundSource.BLOCKS);
    }

    @Nullable
    public static TeleportTransition getAnchorDimensionTransition(MinecraftServer minecraftServer, ServerPlayer serverPlayer, @Nullable ItemStack itemStack) {
        if (itemStack != null) {
            GlobalPos globalPos = itemStack.get(ModComponents.ANCHOR_POS.get());

            if (globalPos == null)
                return null;

            ServerLevel dimensionLevel = minecraftServer.getLevel(globalPos.dimension());
            Optional<Vec3> optional = RespawnAnchorBlock.findStandUpPosition(EntityType.PLAYER, dimensionLevel, globalPos.pos());

            if (optional.isPresent())
                return TeleportHelper.createDimensionTransition(dimensionLevel, serverPlayer, optional.get());
        }

        return null;
    }

}
