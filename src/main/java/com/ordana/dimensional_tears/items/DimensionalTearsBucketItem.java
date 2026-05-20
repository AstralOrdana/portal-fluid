package com.ordana.dimensional_tears.items;

import com.ordana.dimensional_tears.blocks.DimensionalTearsCauldronBlock;
import com.ordana.dimensional_tears.reg.ModBlocks;
import com.ordana.dimensional_tears.reg.ModSoundEvents;
import com.ordana.dimensional_tears.tooltip.RhymingGaslightTooltipItem;
import com.ordana.dimensional_tears.tooltip.RhymingGaslightTooltipState;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
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
import java.util.function.Consumer;

import static net.minecraft.world.level.block.LayeredCauldronBlock.LEVEL;
import static net.minecraft.world.level.block.LayeredCauldronBlock.MAX_FILL_LEVEL;

@SuppressWarnings("unused")
public class DimensionalTearsBucketItem extends BucketItem implements RhymingGaslightTooltipItem {


    public DimensionalTearsBucketItem(Fluid fluid, Properties properties) {
        super(fluid, properties);
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
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.accept(RhymingGaslightTooltipState.getText());

//        if (Translation.isShiftDown()) {
//            tooltip.add(Translation.DIMENSIONAL_TEARS_BUCKET_1.component());
//            tooltip.add(Translation.DIMENSIONAL_TEARS_BUCKET_2.component());
//            tooltip.add(Translation.DIMENSIONAL_TEARS_BUCKET_3.component());
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

        if (!DimensionalTearsCauldronBlock.canEmptyInto(blockState) || player == null)
            return InteractionResult.PASS;

        this.playEmptySound(player, level, blockPos);

        ItemStack itemStack = context.getItemInHand();
        ItemStack itemStack2 = ItemUtils.createFilledResult(itemStack, player, Items.BUCKET.getDefaultInstance());

        player.setItemInHand(context.getHand(), itemStack2);
        level.setBlockAndUpdate(blockPos, ModBlocks.DIMENSIONAL_TEARS_CAULDRON.get().defaultBlockState().setValue(LEVEL, MAX_FILL_LEVEL));

        if (player instanceof ServerPlayer serverPlayer)
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, itemStack);

        return InteractionResult.SUCCESS_SERVER;
    }

    @Override
    protected void playEmptySound(@org.jspecify.annotations.Nullable LivingEntity user, LevelAccessor level, BlockPos pos) {
        playSound(level, pos, user, false);
        level.gameEvent(user, GameEvent.FLUID_PLACE, pos);
    }

    public static void playSound(LevelAccessor levelAccessor, BlockPos blockPos, LivingEntity player, boolean fill) {
        SoundEvent soundEvent = fill ? ModSoundEvents.BUCKET_FILL_DIMENSIONAL_TEARS.value() : ModSoundEvents.BUCKET_EMPTY_DIMENSIONAL_TEARS.value();
        levelAccessor.playSound(player, blockPos, soundEvent, SoundSource.BLOCKS);
    }

}
