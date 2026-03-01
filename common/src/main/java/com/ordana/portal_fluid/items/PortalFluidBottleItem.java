package com.ordana.portal_fluid.items;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import com.ordana.portal_fluid.configs.CommonConfigs;
import com.ordana.portal_fluid.util.TeleportHelper;
import com.ordana.portal_fluid.reg.ModComponents;
import com.ordana.portal_fluid.util.TranslationUtils;
import com.ordana.portal_fluid.tooltip.RhymingGaslightTooltipItem;
import com.ordana.portal_fluid.tooltip.RhymingGaslightTooltipState;
import dev.architectury.injectables.annotations.PlatformOnly;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

public class PortalFluidBottleItem extends HoneyBottleItem implements RhymingGaslightTooltipItem {
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
    public boolean allowComponentsUpdateAnimation(Player player, InteractionHand hand, ItemStack originalStack, ItemStack updatedStack) {
        return false;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable TooltipContext level, @NotNull List<Component> tooltip, @NotNull TooltipFlag context) {
        tooltip.add(RhymingGaslightTooltipState.getText());
        GlobalPos anchorPos = stack.get(ModComponents.ANCHOR_POS.get());

        if (anchorPos != null && CommonConfigs.PORTAL_FLUID_DRINKING.get()) {
            BlockPos blockPos = anchorPos.pos();
            tooltip.add(Component.translatable("tooltip.portal_fluid.portal_fluid_pos", blockPos.getX(), blockPos.getY(), blockPos.getZ()).setStyle(Style.EMPTY.applyFormat(ChatFormatting.LIGHT_PURPLE)));
        }

        if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), Minecraft.getInstance().options.keyShift.key.getValue())) {
            tooltip.add(TranslationUtils.PORTAL_FLUID_1.component());
            tooltip.add(TranslationUtils.PORTAL_FLUID_2.component());

            if (CommonConfigs.CRYING_OBSIDIAN_PORTAL_FLUID.get() && CommonConfigs.RESPAWN_ANCHOR_PORTAL_FLUID.get())
                tooltip.add(TranslationUtils.PORTAL_FLUID_3C.component());
            else if (CommonConfigs.CRYING_OBSIDIAN_PORTAL_FLUID.get())
                tooltip.add(TranslationUtils.PORTAL_FLUID_3A.component());
            else if (CommonConfigs.RESPAWN_ANCHOR_PORTAL_FLUID.get())
                tooltip.add(TranslationUtils.PORTAL_FLUID_3B.component());
        }
        else tooltip.add(TranslationUtils.CROUCH.component());
    }

    private static boolean inPortalDimension(@NotNull Level level) {
        return level.dimension() == Level.OVERWORLD || level.dimension() == Level.NETHER;
    }

    @Override
    @NotNull
    public InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (!inPortalDimension(level))
            return InteractionResult.PASS;

        Optional<PortalShape> optional = PortalShape.findEmptyPortalShape(level, pos.relative(context.getClickedFace()), Direction.Axis.X);

        if (optional.isPresent()) {
            optional.get().createPortalBlocks();

            if (CommonConfigs.PORTAL_CREATION_SOUND.get())
                level.playSound(null, pos, SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundSource.BLOCKS);

            Player player = context.getPlayer();

            if (player == null)
                return InteractionResult.PASS;

            ItemStack itemStack2 = ItemUtils.createFilledResult(context.getItemInHand(), player, Items.GLASS_BOTTLE.getDefaultInstance());
            player.setItemInHand(context.getHand(), itemStack2);

            //if (!player.getAbilities().instabuild) stack.shrink(1);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }

    public static final FoodProperties PORTAL_FLUID = (new FoodProperties.Builder()).nutrition(0).saturationModifier(0F).alwaysEdible().build();

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
    public ItemStack finishUsingItem(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity livingEntity) {
        if (!CommonConfigs.PORTAL_FLUID_DRINKING.get())
            return itemStack;

        if (level instanceof ServerLevel serverLevel && livingEntity instanceof ServerPlayer serverPlayer) {
            ItemStack itemStack2 = ItemUtils.createFilledResult(itemStack, serverPlayer, Items.GLASS_BOTTLE.getDefaultInstance());
            serverPlayer.setItemInHand(serverPlayer.getUsedItemHand(), itemStack2);

            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, itemStack);
            serverPlayer.awardStat(Stats.ITEM_USED.get(this));

            TeleportHelper.teleportPlayerToSpawnPosition(serverLevel, serverPlayer, itemStack);
        }

        return itemStack;
    }

    @Nullable
    public static Vec3 getAnchorRespawnPosition(MinecraftServer minecraftServer, @Nullable ItemStack itemStack) {
        if (itemStack == null)
            return null;

        GlobalPos globalPos = itemStack.get(ModComponents.ANCHOR_POS.get());

        if (globalPos == null)
            return null;

        ServerLevel dimensionLevel = minecraftServer.getLevel(globalPos.dimension());
        Optional<Vec3> optional = RespawnAnchorBlock.findStandUpPosition(EntityType.PLAYER, dimensionLevel, globalPos.pos());

        return optional.orElse(null);
    }

}
