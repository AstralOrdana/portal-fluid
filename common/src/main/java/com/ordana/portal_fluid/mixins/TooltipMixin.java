package com.ordana.portal_fluid.mixins;


import com.ordana.portal_fluid.configs.CommonConfigs;
import com.ordana.portal_fluid.util.TranslationUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

    @Environment(EnvType.CLIENT)
    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void vanillaItemTooltips(ItemStack stack, Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag tooltipFlag, CallbackInfo ci) {
        if (stack.is(Items.FLINT_AND_STEEL) && !CommonConfigs.FlINT_AND_STEEL_PORTAL_LIGHTING.get())
            tooltip.add(TranslationUtils.FLINT_AND_STEEL.component());

        if (CommonConfigs.CRYING_OBSIDIAN_PORTAL_FLUID.get() && stack.is(Items.CRYING_OBSIDIAN))
            tooltip.add(TranslationUtils.CRYING_OBSIDIAN.component());

        if (CommonConfigs.RESPAWN_ANCHOR_PORTAL_FLUID.get() && stack.is(Items.RESPAWN_ANCHOR))
            tooltip.add(TranslationUtils.CRYING_OBSIDIAN.component());
    }
}
