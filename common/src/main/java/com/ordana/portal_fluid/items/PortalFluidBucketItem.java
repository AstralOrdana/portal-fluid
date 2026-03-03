package com.ordana.portal_fluid.items;

import com.ordana.portal_fluid.blocks.PortalFluidCauldronBlock;
import com.ordana.portal_fluid.reg.ModBlocks;
import com.ordana.portal_fluid.reg.ModSoundEvents;
// import com.ordana.portal_fluid.util.Translation;
import com.ordana.portal_fluid.tooltip.RhymingGaslightTooltipItem;
import com.ordana.portal_fluid.tooltip.RhymingGaslightTooltipState;
import dev.architectury.injectables.annotations.PlatformOnly;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.advancements.CriteriaTriggers;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;

import static net.minecraft.world.level.block.LayeredCauldronBlock.LEVEL;
import static net.minecraft.world.level.block.LayeredCauldronBlock.MAX_FILL_LEVEL;

@SuppressWarnings("unused")
public class PortalFluidBucketItem extends BucketItem implements RhymingGaslightTooltipItem {

    private static final Field CONTENT = PlatHelper.findField(BucketItem.class, "content");

    public PortalFluidBucketItem(Fluid fluid, Properties properties) {
        super(fluid, properties);
    }

    //Override
    @PlatformOnly("neoforge")
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

//        if (Translation.isShiftDown()) {
//            tooltip.add(Translation.PORTAL_FLUID_BUCKET_1.component());
//            tooltip.add(Translation.PORTAL_FLUID_BUCKET_2.component());
//            tooltip.add(Translation.PORTAL_FLUID_BUCKET_3.component());
//        }
//        else tooltip.add(Translation.CROUCH.component());
    }

    @Override
    @NotNull
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);

        Player player = context.getPlayer();

        if (!PortalFluidCauldronBlock.canEmptyInto(blockState) || player == null)
            return InteractionResult.PASS;

        this.playEmptySound(player, level, blockPos);

        ItemStack itemStack = context.getItemInHand();
        ItemStack itemStack2 = ItemUtils.createFilledResult(itemStack, player, Items.BUCKET.getDefaultInstance());

        player.setItemInHand(context.getHand(), itemStack2);
        level.setBlockAndUpdate(blockPos, ModBlocks.PORTAL_CAULDRON.get().defaultBlockState().setValue(LEVEL, MAX_FILL_LEVEL));

        if (player instanceof ServerPlayer serverPlayer)
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, itemStack);

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    protected void playEmptySound(@Nullable Player player, LevelAccessor level, BlockPos pos) {
        playSound(level, pos, player, false);
        level.gameEvent(player, GameEvent.FLUID_PLACE, pos);
    }

    public static void playSound(LevelAccessor levelAccessor, BlockPos blockPos, Player player, boolean fill) {
        SoundEvent soundEvent = fill ? ModSoundEvents.BUCKET_FILL_PORTAL_FLUID.get() : ModSoundEvents.BUCKET_EMPTY_PORTAL_FLUID.get();
        levelAccessor.playSound(player, blockPos, soundEvent, SoundSource.BLOCKS);
    }

}
