package com.ordana.portal_fluid.items;

import com.mojang.blaze3d.platform.InputConstants;
import com.ordana.portal_fluid.blocks.PortalFluidCauldronBlock;
import com.ordana.portal_fluid.reg.ModBlocks;
import com.ordana.portal_fluid.reg.ModSoundEvents;
import com.ordana.portal_fluid.util.TranslationUtils;
import com.ordana.portal_fluid.tooltip.RhymingGaslightTooltipItem;
import com.ordana.portal_fluid.tooltip.RhymingGaslightTooltipState;
import dev.architectury.injectables.annotations.PlatformOnly;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;

import static net.minecraft.world.level.block.LayeredCauldronBlock.LEVEL;
import static net.minecraft.world.level.block.LayeredCauldronBlock.MAX_FILL_LEVEL;

public class PortalFluidBucketItem extends BucketItem implements RhymingGaslightTooltipItem {

    private static final Field CONTENT = PlatHelper.findField(BucketItem.class, "content");

    public PortalFluidBucketItem(Fluid fluid, Properties properties) {
        super(fluid, properties);
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

        if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), Minecraft.getInstance().options.keyShift.key.getValue())) {
            tooltip.add(TranslationUtils.PORTAL_FLUID_BUCKET_1.component());
            tooltip.add(TranslationUtils.PORTAL_FLUID_BUCKET_2.component());
            tooltip.add(TranslationUtils.PORTAL_FLUID_BUCKET_3.component());
        }
        else tooltip.add(TranslationUtils.CROUCH.component());
    }

    @Override
    @NotNull
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        Player player = context.getPlayer();
        ItemStack itemStack = context.getItemInHand();

        if (state.getBlock() instanceof CauldronBlock || state.getBlock() instanceof PortalFluidCauldronBlock && state.getValue(LEVEL) < MAX_FILL_LEVEL) {
            this.playEmptySound(player, level, pos);

            if (player instanceof ServerPlayer serverPlayer) {
                ItemStack itemStack2 = ItemUtils.createFilledResult(itemStack, player, Items.BUCKET.getDefaultInstance());
                player.setItemInHand(context.getHand(), itemStack2);
                level.setBlockAndUpdate(pos, ModBlocks.PORTAL_CAULDRON.get().defaultBlockState().setValue(LEVEL, MAX_FILL_LEVEL));

                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, itemStack);
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    @Override
    protected void playEmptySound(@Nullable Player player, LevelAccessor level, BlockPos pos) {
        SoundEvent soundEvent = ModSoundEvents.PORTAL_FLUID_BUCKET_EMPTY.get();
        level.playSound(player, pos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(player, GameEvent.FLUID_PLACE, pos);
    }
}
