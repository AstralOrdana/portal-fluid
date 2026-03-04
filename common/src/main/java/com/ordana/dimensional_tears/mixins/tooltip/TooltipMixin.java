package com.ordana.dimensional_tears.mixins.tooltip;

import com.ordana.dimensional_tears.configs.CommonConfigs;
import com.ordana.dimensional_tears.util.Translation;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public class TooltipMixin {

    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void vanillaItemTooltips(ItemStack stack, Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag tooltipFlag, CallbackInfo ci) {
//        if (stack.is(Items.FLINT_AND_STEEL) && !CommonConfigs.FlINT_AND_STEEL_PORTAL_LIGHTING.get())
//            tooltip.add(Translation.FLINT_AND_STEEL.component());

        if (stack.is(Items.CRYING_OBSIDIAN) && CommonConfigs.CRYING_OBSIDIAN_DIMENSIONAL_TEARS.get())
            tooltip.add(Translation.CRYING_OBSIDIAN.component());

        if (stack.is(Items.RESPAWN_ANCHOR) && CommonConfigs.RESPAWN_ANCHOR_DIMENSIONAL_TEARS.get())
            tooltip.add(Translation.CRYING_OBSIDIAN.component());
    }

}
