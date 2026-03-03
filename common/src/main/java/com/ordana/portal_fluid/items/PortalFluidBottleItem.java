package com.ordana.portal_fluid.items;

import com.mojang.logging.LogUtils;
import com.ordana.portal_fluid.blocks.PortalFluidCauldronBlock;
import com.ordana.portal_fluid.configs.CommonConfigs;
import com.ordana.portal_fluid.reg.ModItems;
import com.ordana.portal_fluid.reg.ModSoundEvents;
import com.ordana.portal_fluid.util.TeleportHelper;
import com.ordana.portal_fluid.reg.ModComponents;
import com.ordana.portal_fluid.util.Translation;
import com.ordana.portal_fluid.tooltip.RhymingGaslightTooltipItem;
import com.ordana.portal_fluid.tooltip.RhymingGaslightTooltipState;
import dev.architectury.injectables.annotations.PlatformOnly;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("unused")
public class PortalFluidBottleItem extends HoneyBottleItem implements RhymingGaslightTooltipItem {

    private static final Logger LOGGER = LogUtils.getLogger();
    public static final FoodProperties FOOD_PROPERTIES = new FoodProperties.Builder()
        .nutrition(0)
        .saturationModifier(0F)
        .alwaysEdible()
        .build();

    public PortalFluidBottleItem(Properties properties) {
        super(properties);
    }

    //Override
    @PlatformOnly(PlatformOnly.FORGE)
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    //Override
    @PlatformOnly(PlatformOnly.FABRIC)
    public boolean allowComponentsUpdateAnimation(Player player, InteractionHand hand, ItemStack originalStack, ItemStack updatedStack) {
        return false;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable TooltipContext level, @NotNull List<Component> tooltip, @NotNull TooltipFlag context) {
        tooltip.add(RhymingGaslightTooltipState.getText());
        GlobalPos anchorPos = stack.get(ModComponents.ANCHOR_POS.get());

        if (anchorPos != null && CommonConfigs.PORTAL_FLUID_DRINKING.get()) {
            BlockPos blockPos = anchorPos.pos();
            tooltip.add(Component.translatable("tooltip.portal_fluid.portal_fluid_pos", blockPos.getX(), blockPos.getY(), blockPos.getZ()).setStyle(Style.EMPTY.applyFormat(ChatFormatting.LIGHT_PURPLE)));
        }

        if (!Translation.isShiftDown()) {
            tooltip.add(Translation.CROUCH.component());
            return;
        }

        tooltip.add(Translation.PORTAL_FLUID_1.component());
        tooltip.add(Translation.PORTAL_FLUID_2.component());

        boolean respawnAnchorFluid = CommonConfigs.RESPAWN_ANCHOR_PORTAL_FLUID.get();

        if (CommonConfigs.CRYING_OBSIDIAN_PORTAL_FLUID.get())
            tooltip.add(respawnAnchorFluid ? Translation.FROM_EITHER.component() : Translation.FROM_CRYING_OBSIDIAN.component());
        else if (respawnAnchorFluid)
            tooltip.add(Translation.FROM_RESPAWN_ANCHOR.component());
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

        InteractionResult emptyIntoCauldronResult = tryEmptyIntoCauldron(level, blockPos, itemStack, player, interactionHand);
        InteractionResult createPortalResult = tryCreatePortal(level, blockPos, itemStack, player, interactionHand, context.getClickedFace());

        return Objects.requireNonNullElse(emptyIntoCauldronResult, createPortalResult);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    @NotNull
    public SoundEvent getDrinkingSound() {
        return ModSoundEvents.PORTAL_FLUID_BOTTLE_DRINK.get();
    }

    @Override
    @NotNull
    public SoundEvent getEatingSound() {
        return ModSoundEvents.PORTAL_FLUID_BOTTLE_DRINK.get();
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
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
        if (CommonConfigs.PORTAL_FLUID_DRINKING.get() && level instanceof ServerLevel serverLevel && livingEntity instanceof ServerPlayer serverPlayer) {
            ItemStack filledResult = ItemUtils.createFilledResult(itemStack, serverPlayer, Items.GLASS_BOTTLE.getDefaultInstance());
            serverPlayer.setItemInHand(serverPlayer.getUsedItemHand(), filledResult);

            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, itemStack);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));

            TeleportHelper.tryDelegateTeleportationToRiftingEffect(serverLevel, serverPlayer, itemStack);
        }

        return itemStack;
    }

    private static boolean inPortalDimension(@NotNull Level level) {
        ResourceKey<Level> dimensionKey = level.dimension();
        return dimensionKey == Level.OVERWORLD || dimensionKey == Level.NETHER;
    }

    private static InteractionResult tryCreatePortal(Level level, BlockPos blockPos, ItemStack itemStack, Player player, InteractionHand interactionHand, Direction clickedFace) {
        if (inPortalDimension(level)) {
            Optional<PortalShape> optional = PortalShape.findEmptyPortalShape(level, blockPos.relative(clickedFace), Direction.Axis.X);

            if (optional.isEmpty())
                return InteractionResult.PASS;

            if (CommonConfigs.PORTAL_CREATION_SOUND.get())
                level.playSound(player, blockPos, ModSoundEvents.PORTAL_SPAWN.get(), SoundSource.BLOCKS);

            ItemStack filledResult = ItemUtils.createFilledResult(itemStack, player, Items.GLASS_BOTTLE.getDefaultInstance());
            player.setItemInHand(interactionHand, filledResult);

            optional.get().createPortalBlocks();

            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
    }

    private static InteractionResult tryEmptyIntoCauldron(Level level, BlockPos blockPos, ItemStack itemStack, Player player, InteractionHand interactionHand) {
        BlockState blockState = level.getBlockState(blockPos);

        if (!PortalFluidCauldronBlock.canEmptyInto(blockState))
            return InteractionResult.PASS;

        playSound(level, blockPos, player, false);
        ItemStack filledResult = ItemUtils.createFilledResult(itemStack, player, Items.GLASS_BOTTLE.getDefaultInstance());

        player.setItemInHand(interactionHand, filledResult);
        level.setBlockAndUpdate(blockPos, PortalFluidCauldronBlock.getNextCauldronState(blockState));

        if (player instanceof ServerPlayer serverPlayer)
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, itemStack);

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    public static InteractionResult tryCollectObsidianTears(Level level, BlockPos blockPos, ItemStack itemStack, Player player, InteractionHand interactionHand) {
        if (!CommonConfigs.CRYING_OBSIDIAN_PORTAL_FLUID.get())
            return InteractionResult.PASS;

        ParticleUtils.spawnParticlesOnBlockFaces(level, blockPos, ParticleTypes.FALLING_OBSIDIAN_TEAR, UniformInt.of(3, 5));
        playSound(level, blockPos, player, true);

        ItemStack filledResult = ItemUtils.createFilledResult(itemStack, player, ModItems.PORTAL_FLUID_BOTTLE.get().getDefaultInstance());

        player.setItemInHand(interactionHand, filledResult);
        level.setBlockAndUpdate(blockPos, Blocks.OBSIDIAN.defaultBlockState());

        if (player instanceof ServerPlayer serverPlayer)
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, itemStack);

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public static InteractionResult tryCollectRespawnAnchorTears(Level level, BlockPos blockPos, BlockState blockState, ItemStack itemStack, Player player, InteractionHand interactionHand) {
        if (blockState.getValue(RespawnAnchorBlock.CHARGE) == 0 || !CommonConfigs.RESPAWN_ANCHOR_PORTAL_FLUID.get())
            return InteractionResult.PASS;

        ParticleUtils.spawnParticlesOnBlockFaces(level, blockPos, ParticleTypes.FALLING_OBSIDIAN_TEAR, UniformInt.of(3, 5));
        playSound(level, blockPos, player, true);

        ItemStack filledResult = ItemUtils.createFilledResult(itemStack, player, ModItems.PORTAL_FLUID_BOTTLE.get().getDefaultInstance());
        filledResult.set(ModComponents.ANCHOR_POS.get(), new GlobalPos(level.dimension(), blockPos));

        player.setItemInHand(interactionHand, filledResult);
        level.setBlockAndUpdate(blockPos, blockState.setValue(RespawnAnchorBlock.CHARGE, blockState.getValue(RespawnAnchorBlock.CHARGE) - 1));

        if (player instanceof ServerPlayer serverPlayer)
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, itemStack);

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public static void playSound(Level level, BlockPos blockPos, Player player, boolean fill) {
        SoundEvent soundEvent = fill ? ModSoundEvents.BOTTLE_FILL_PORTAL_FLUID.get() : ModSoundEvents.BOTTLE_EMPTY_PORTAL_FLUID.get();
        level.playSound(player, blockPos, soundEvent, SoundSource.BLOCKS);
    }

    @Nullable
    public static DimensionTransition getAnchorDimensionTransition(MinecraftServer minecraftServer, ServerPlayer serverPlayer, @Nullable ItemStack itemStack) {
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
