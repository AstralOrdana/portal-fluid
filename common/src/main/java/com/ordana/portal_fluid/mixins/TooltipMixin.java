package com.ordana.portal_fluid.mixins;


import com.ordana.portal_fluid.configs.CommonConfigs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public class TooltipMixin {

    @Environment(EnvType.CLIENT)
    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void vanillaItemTooltips(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag isAdvanced, CallbackInfo ci) {

        if (stack.is(Items.FLINT_AND_STEEL) && !CommonConfigs.FlINT_AND_STEEL_PORTAL_LIGHTING.get()) {
            tooltip.add(Component.translatable("tooltip.portal_fluid.flint_and_steel").setStyle(Style.EMPTY.applyFormat(ChatFormatting.DARK_PURPLE)));
        }
        if (CommonConfigs.CRYING_OBSIDIAN_PORTAL_FLUID.get() && stack.is(Items.CRYING_OBSIDIAN)) {
            tooltip.add(Component.translatable("tooltip.portal_fluid.crying_obsidian").setStyle(Style.EMPTY.applyFormat(ChatFormatting.DARK_PURPLE)));
        }
        if (CommonConfigs.RESPAWN_ANCHOR_PORTAL_FLUID.get() && stack.is(Items.RESPAWN_ANCHOR)) {
            tooltip.add(Component.translatable("tooltip.portal_fluid.crying_obsidian").setStyle(Style.EMPTY.applyFormat(ChatFormatting.DARK_PURPLE)));
        }
    }
}
