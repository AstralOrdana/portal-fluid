package com.ordana.dimensional_tears.mixins.fabric;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.ordana.dimensional_tears.fluids.PortalFluidSpriteSet;
import com.ordana.dimensional_tears.reg.ModTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.resources.ResourceLocation;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

    @ModifyExpressionValue(method = "renderWater", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/ScreenEffectRenderer;UNDERWATER_LOCATION:Lnet/minecraft/resources/ResourceLocation;", opcode = Opcodes.GETSTATIC))
    private static ResourceLocation getUnderwaterOverlayTexture(ResourceLocation original, @Local(argsOnly = true) Minecraft minecraft) {
        return minecraft.player != null && minecraft.player.isEyeInFluid(ModTags.DIMENSIONAL_TEARS) ? PortalFluidSpriteSet.SCREEN : original;
    }

}