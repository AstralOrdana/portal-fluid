package com.ordana.dimensional_tears.mixins;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.ordana.dimensional_tears.reg.ModItems;
import com.ordana.dimensional_tears.reg.ModSoundEvents;
import com.ordana.dimensional_tears.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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

@Mixin(BottleItem.class)
public class GlassBottleMixin extends Item {

    public GlassBottleMixin(Properties properties) {
        super(properties);
    }

    @WrapMethod(method = "use")
    public InteractionResult useInject(Level level, Player player, InteractionHand interactionHand, Operation<InteractionResult> original) {
        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        BlockPos blockPos = blockHitResult.getBlockPos();

        if (!level.getFluidState(blockPos).is(ModTags.DIMENSIONAL_TEARS))
            return original.call(level, player, interactionHand);

        level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSoundEvents.BOTTLE_FILL_DIMENSIONAL_TEARS.value(), SoundSource.NEUTRAL);
        level.gameEvent(player, GameEvent.FLUID_PICKUP, blockPos);

        player.awardStat(Stats.ITEM_USED.get(this));

        ItemStack itemStack = player.getItemInHand(interactionHand);
        ItemStack filledResult = ItemUtils.createFilledResult(itemStack, player, ModItems.DIMENSIONAL_TEARS_BOTTLE.get().getDefaultInstance());

        return InteractionResult.SUCCESS_SERVER;
    }

}
