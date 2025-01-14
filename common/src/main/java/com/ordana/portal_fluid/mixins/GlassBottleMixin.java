package com.ordana.portal_fluid.mixins;

import com.ordana.portal_fluid.reg.ModItems;
import com.ordana.portal_fluid.reg.ModSoundEvents;
import com.ordana.portal_fluid.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BottleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BottleItem.class)
public class GlassBottleMixin extends Item {

    public GlassBottleMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void useInject(Level level, Player player, InteractionHand usedHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {

        ItemStack itemStack = player.getItemInHand(usedHand);
        {
            BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
            BlockPos blockPos = blockHitResult.getBlockPos();
            if (level.getFluidState(blockPos).is(ModTags.PORTAL_FLUID)) {
                level.playSound(player, player.getX(), player.getY(), player.getZ(), ModSoundEvents.PORTAL_FLUID_BOTTLE_FILL.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
                level.gameEvent(player, GameEvent.FLUID_PICKUP, blockPos);
                cir.setReturnValue(InteractionResultHolder.sidedSuccess(this.turnBottleIntoItem(itemStack, player, ModItems.PORTAL_FLUID_BOTTLE.get().getDefaultInstance()), level.isClientSide()));
            }
        }
    }

    @Unique
    protected ItemStack turnBottleIntoItem(ItemStack bottleStack, Player player, ItemStack filledBottleStack) {
        player.awardStat(Stats.ITEM_USED.get(this));
        return ItemUtils.createFilledResult(bottleStack, player, filledBottleStack);
    }

}
