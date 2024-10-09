package com.ordana.portal_fluid.reg;

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
    GRAY        (ChatFormatting.GRAY),
    ITALIC_GRAY (ChatFormatting.GRAY, ChatFormatting.ITALIC),
    ;

    final Style val;

    Styles(ChatFormatting... formats) {
        this.val = Style.EMPTY.applyFormats(formats);
    }
}

public enum TranslationUtils {
    CROUCH              ("tooltip.portal_fluid.hold_crouch",
            Style.EMPTY.withColor(ChatFormatting.GOLD), Components.SNEAK.val),
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
