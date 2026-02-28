package com.ordana.portal_fluid.tooltip;

import com.ordana.portal_fluid.mixins.RhymingGaslightTooltipMixin;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * <p>A handler for a tooltip text component that changes every time it's viewed.</p>
 * <p>In order to mark an item as capable of changing this text, you must implement {@link RhymingGaslightTooltipItem} on the class, and onAnimateTick {@link RhymingGaslightTooltipState#getText()} to the list of text components via {@link net.minecraft.world.item.Item#appendHoverText(ItemStack, Item.TooltipContext, List, TooltipFlag)}. Two of these items next to each other in an inventory will not re-shuffle the tooltip text when mouse-hopped between; the cursor must first move to a slot with a different (or empty) item before changing the text on next viewing.</p>
 *
 * @see com.ordana.portal_fluid.items.PortalFluidBottleItem
 * @see com.ordana.portal_fluid.items.PortalFluidBucketItem
 * @see RhymingGaslightTooltipMixin
 * @author axialeaa
 */
public enum RhymingGaslightTooltipState {

    FEARS("tooltip.portal_fluid.rhymes_with_tears_0"),
    SCARES("tooltip.portal_fluid.rhymes_with_tears_1");

    private static RhymingGaslightTooltipState current = RhymingGaslightTooltipState.FEARS;
    private final String translationKey;

    RhymingGaslightTooltipState(String translationKey) {
        this.translationKey = translationKey;
    }

    public static Component getText() {
        return Component.translatable(current.translationKey).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE));
    }

    public static void randomizeCurrent() {
        current = Util.getRandom(values(), RandomSource.create());
    }

}
