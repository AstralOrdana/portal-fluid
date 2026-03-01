package com.ordana.portal_fluid.util;

import net.minecraft.ChatFormatting;
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

public enum TranslationUtils {
    CROUCH                ("tooltip.portal_fluid.hold_crouch",           Styles.GOLD.val, Components.SNEAK.val),
    FLINT_AND_STEEL       ("tooltip.portal_fluid.flint_and_steel",       Styles.DARK_PURPLE.val),
    CRYING_OBSIDIAN       ("tooltip.portal_fluid.crying_obsidian",       Styles.DARK_PURPLE.val),
    PORTAL_FLUID_1        ("tooltip.portal_fluid.portal_fluid_1",        Styles.GRAY.val),
    PORTAL_FLUID_2        ("tooltip.portal_fluid.portal_fluid_2",        Styles.GRAY.val),
    PORTAL_FLUID_3A       ("tooltip.portal_fluid.portal_fluid_3a",       Styles.GRAY.val),
    PORTAL_FLUID_3B       ("tooltip.portal_fluid.portal_fluid_3b",       Styles.GRAY.val),
    PORTAL_FLUID_3C       ("tooltip.portal_fluid.portal_fluid_3c",       Styles.GRAY.val),
    PORTAL_FLUID_BUCKET_1 ("tooltip.portal_fluid.portal_fluid_bucket_1", Styles.GRAY.val),
    PORTAL_FLUID_BUCKET_2 ("tooltip.portal_fluid.portal_fluid_bucket_2", Styles.GRAY.val),
    PORTAL_FLUID_BUCKET_3 ("tooltip.portal_fluid.portal_fluid_bucket_3", Styles.GRAY.val),
    ;

    private final MutableComponent component;

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

    TranslationUtils(String s, Style st, Object... o) {
        component = Component.translatable(s, o);
        if (st != null) {
            component.setStyle(st);
        }
    }
}
