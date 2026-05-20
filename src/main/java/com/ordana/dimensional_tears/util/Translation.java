package com.ordana.dimensional_tears.util;

import com.mojang.blaze3d.platform.InputConstants;
import com.ordana.dimensional_tears.mixins.KeyMappingAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Early predefines later-used constants to
 * avoid heavy constant replicates.
 */
enum Components {
    SNEAK   (Component.keybind("key.sneak")),
    ;

    final Component val;

    Components(Component c) {
        val = c;
    }
}

enum Styles {
    GRAY         (ChatFormatting.GRAY),
    ITALIC_GRAY  (ChatFormatting.GRAY, ChatFormatting.ITALIC),
    DARK_PURPLE  (ChatFormatting.DARK_PURPLE),
    LIGHT_PURPLE (ChatFormatting.LIGHT_PURPLE),
    GOLD         (ChatFormatting.GOLD),
    ;

    final Style val;

    Styles(ChatFormatting... formats) {
        this.val = Style.EMPTY.applyFormats(formats);
    }
}

public enum Translation {
    CROUCH                ("tooltip.dimensional_tears.hold_crouch",           Styles.GOLD.val, Components.SNEAK.val),
    FLINT_AND_STEEL       ("tooltip.dimensional_tears.flint_and_steel",       Styles.DARK_PURPLE.val),
    CRYING_OBSIDIAN       ("tooltip.dimensional_tears.crying_obsidian",       Styles.DARK_PURPLE.val),
    DIMENSIONAL_TEARS_1        ("tooltip.dimensional_tears.dimensional_tears_1",        Styles.GRAY.val),
    DIMENSIONAL_TEARS_2        ("tooltip.dimensional_tears.dimensional_tears_2",        Styles.GRAY.val),
    FROM_CRYING_OBSIDIAN  ("tooltip.dimensional_tears.from_crying_obsidian",  Styles.GRAY.val),
    FROM_RESPAWN_ANCHOR   ("tooltip.dimensional_tears.from_respawn_anchor",   Styles.GRAY.val),
    FROM_EITHER           ("tooltip.dimensional_tears.from_either",           Styles.GRAY.val),
    DIMENSIONAL_TEARS_BUCKET_1 ("tooltip.dimensional_tears.dimensional_tears_bucket_1", Styles.GRAY.val),
    DIMENSIONAL_TEARS_BUCKET_2 ("tooltip.dimensional_tears.dimensional_tears_bucket_2", Styles.GRAY.val),
    DIMENSIONAL_TEARS_BUCKET_3 ("tooltip.dimensional_tears.dimensional_tears_bucket_3", Styles.GRAY.val),
    ;

    private final MutableComponent component;

    Translation(String s, Style st, Object... o) {
        component = Component.translatable(s, o);
        if (st != null) {
            component.setStyle(st);
        }
    }

    /**
     * Returns a copy of the inner component
     * @return A copy of the inner component
     * @apiNote Creates a copy to avoid exposing inner value
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public MutableComponent component() {
        return component.copy();
    }

    @NotNull
    public static MutableComponent text(String s, ChatFormatting... formats) {
        if (formats != null && formats.length != 0) {
            return Component.translatable(s).withStyle(formats);
        }
        return Component.translatable(s);
    }

    public static boolean isShiftDown() {
        Minecraft minecraft = Minecraft.getInstance();
        return InputConstants.isKeyDown(minecraft.getWindow(), ((KeyMappingAccessor) minecraft.options.keyShift).getKey().getValue());
    }

}
