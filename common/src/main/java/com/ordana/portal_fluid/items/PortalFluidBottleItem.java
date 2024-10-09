package com.ordana.portal_fluid.items;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.ordana.portal_fluid.configs.CommonConfigs;
import com.ordana.portal_fluid.reg.LevelHelper;
import com.ordana.portal_fluid.reg.TranslationUtils;
import dev.architectury.injectables.annotations.PlatformOnly;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PortalFluidBottleItem extends HoneyBottleItem {
    private static final Logger LOGGER = LogUtils.getLogger();

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
    public boolean allowNbtUpdateAnimation(Player player, InteractionHand hand, ItemStack originalStack, ItemStack updatedStack) {
        return false;
    }

    private int tickCounter = 0;

    public int setTickCounter(int tick) {
        return tickCounter = tick;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level levelIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, levelIn, entityIn, itemSlot, isSelected);
        tickCounter++;
        if (tickCounter >= 200) {
            setBoolean(stack, !getBoolean(stack));
            setTickCounter(0);
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag context) {

        CompoundTag compoundTag = stack.getOrCreateTag();

        if (getBoolean(stack)) tooltip.add(Component.translatable("tooltip.portal_fluid.rhymes_with_tears_0").setStyle(Style.EMPTY.applyFormat(ChatFormatting.DARK_PURPLE)));
        else tooltip.add(Component.translatable("tooltip.portal_fluid.rhymes_with_tears_1", getBoolean(stack)).setStyle(Style.EMPTY.applyFormat(ChatFormatting.DARK_PURPLE)));
        if (compoundTag.contains("anchorPos") && CommonConfigs.PORTAL_FLUID_DRINKING.get()) {
            BlockPos blockPos = NbtUtils.readBlockPos(compoundTag.getCompound("anchorPos"));
            tooltip.add(Component.translatable("tooltip.portal_fluid.portal_fluid_pos", blockPos.getX(), blockPos.getY(), blockPos.getZ()).setStyle(Style.EMPTY.applyFormat(ChatFormatting.LIGHT_PURPLE)));
        }
        if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), Minecraft.getInstance().options.keyShift.key.getValue())) {
            tooltip.add(Component.translatable("tooltip.portal_fluid.portal_fluid_1").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
            tooltip.add(Component.translatable("tooltip.portal_fluid.portal_fluid_2").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
            if (CommonConfigs.CRYING_OBSIDIAN_PORTAL_FLUID.get() && CommonConfigs.RESPAWN_ANCHOR_PORTAL_FLUID.get()) tooltip.add(Component.translatable("tooltip.portal_fluid.portal_fluid_3c").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
            else if (CommonConfigs.CRYING_OBSIDIAN_PORTAL_FLUID.get()) tooltip.add(Component.translatable("tooltip.portal_fluid.portal_fluid_3a").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
            else if (CommonConfigs.RESPAWN_ANCHOR_PORTAL_FLUID.get()) tooltip.add(Component.translatable("tooltip.portal_fluid.portal_fluid_3b").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
        } else {
            tooltip.add(TranslationUtils.CROUCH.component());
        }
    }

    public void setBoolean(@NotNull ItemStack stack, boolean tears) {
        stack.getOrCreateTag().putBoolean("bool", tears);
    }

    public boolean getBoolean(@NotNull ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("bool");
    }

    private static boolean inPortalDimension(@NotNull Level level) {
        return level.dimension() == Level.OVERWORLD || level.dimension() == Level.NETHER;
    }

    @Override
    @NotNull
    public InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        if (inPortalDimension(level)) {
            Optional<PortalShape> optional = PortalShape.findEmptyPortalShape(level, pos.relative(context.getClickedFace()), Direction.Axis.X);
            if (optional.isPresent()) {
                optional.get().createPortalBlocks();
                if (CommonConfigs.PORTAL_CREATION_SOUND.get()) level.playSound(null, pos, SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundSource.BLOCKS, 1.0f, 1.0f);

                ItemStack itemStack2 = ItemUtils.createFilledResult(stack, player, Items.GLASS_BOTTLE.getDefaultInstance());
                player.setItemInHand(context.getHand(), itemStack2);

                //if (!player.getAbilities().instabuild) stack.shrink(1);

                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }

    public static final FoodProperties PORTAL_FLUID = (new FoodProperties.Builder()).nutrition(0).saturationMod(0F).alwaysEat().build();

    @Override
    @NotNull
    public SoundEvent getDrinkingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    @Override
    @NotNull
    public SoundEvent getEatingSound() {
        return SoundEvents.HONEY_DRINK;
    }


    @Override
    @NotNull
    public ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity livingEntity) {
        if (livingEntity instanceof Player player) {
            ItemStack itemStack2 = ItemUtils.createFilledResult(stack, player, Items.GLASS_BOTTLE.getDefaultInstance());
            player.setItemInHand(player.getUsedItemHand(), itemStack2);

        }
        if (livingEntity instanceof ServerPlayer serverPlayer && CommonConfigs.PORTAL_FLUID_DRINKING.get()) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));

            CompoundTag compoundTag = stack.getOrCreateTag();
            boolean bl = compoundTag.contains("anchorPos");
            boolean bl2 = compoundTag.contains("anchorDimension");
            PortalFluidBottleItem.getDimension(compoundTag);

            if (bl && bl2) {
                LevelHelper.teleportToAnchorPosition(serverPlayer, getAnchorPos(compoundTag));
            }
            else LevelHelper.teleportToSpawnPosition(serverPlayer);
        }
        return stack;
    }

    public static void addLocationTags(ResourceKey<Level> anchorDimension, BlockPos pos, CompoundTag compoundTag) {
        if (!compoundTag.contains("anchorPos")) compoundTag.put("anchorPos", NbtUtils.writeBlockPos(pos));
        DataResult<Tag> var10000 = Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, anchorDimension);
        Logger var10001 = LOGGER;
        Objects.requireNonNull(var10001);
        var10000.resultOrPartial(var10001::error).ifPresent(tag -> compoundTag.put("anchorDimension", tag));
    }

    private static Optional<ResourceKey<Level>> getDimension(CompoundTag compoundTag) {
        return Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, compoundTag.get("anchorDimension")).result();
    }

    public static GlobalPos getAnchorPos(CompoundTag compoundTag) {
        boolean bl = compoundTag.contains("anchorPos");
        boolean bl2 = compoundTag.contains("anchorDimension");
        if (bl && bl2) {
            Optional<ResourceKey<Level>> optional = getDimension(compoundTag);
            if (optional.isPresent()) {
                BlockPos blockPos = NbtUtils.readBlockPos(compoundTag.getCompound("anchorPos"));
                return GlobalPos.of(optional.get(), blockPos);
            }
        }

        return null;
    }

}
