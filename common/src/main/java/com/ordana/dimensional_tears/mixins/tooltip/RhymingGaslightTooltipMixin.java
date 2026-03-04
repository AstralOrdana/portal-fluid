package com.ordana.dimensional_tears.mixins.tooltip;

import com.ordana.dimensional_tears.tooltip.RhymingGaslightTooltipItem;
import com.ordana.dimensional_tears.tooltip.RhymingGaslightTooltipState;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class RhymingGaslightTooltipMixin extends Screen {

    @Shadow @Nullable protected Slot hoveredSlot;
    @Unique private boolean dimensional_tears$lastStackHoveredWasPortalFluid = false;

    protected RhymingGaslightTooltipMixin(Component component) {
        super(component);
    }

    @Inject(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;hoveredSlot:Lnet/minecraft/world/inventory/Slot;", opcode = Opcodes.PUTFIELD, ordinal = 1, shift = At.Shift.AFTER))
    private void gaslightGatekeepGirlboss(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        boolean portalFluidHovered = this.hoveredSlot != null && this.hoveredSlot.hasItem() && this.hoveredSlot.getItem().getItem() instanceof RhymingGaslightTooltipItem;

        if (portalFluidHovered) {
            if (!this.dimensional_tears$lastStackHoveredWasPortalFluid) {
                RhymingGaslightTooltipState.randomizeCurrent();
                this.dimensional_tears$lastStackHoveredWasPortalFluid = true;
            }
        }
        else {
            this.dimensional_tears$lastStackHoveredWasPortalFluid = false;
        }
    }

}
