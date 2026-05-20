package com.ordana.dimensional_tears.mixins.dimensional_tears;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.ordana.dimensional_tears.DimensionalTearsPlatform;
import com.ordana.dimensional_tears.fluids.DimensionalTearsFluidRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

    @Inject(method = "renderScreenEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"))
    private static void renderDimTearsScreenEffect(boolean isFirstPerson, boolean isSleeping, float partialTicks, SubmitNodeCollector submitNodeCollector, boolean hideGui, CallbackInfo ci, @Local(name = "poseStack") PoseStack poseStack) {
        LocalPlayer player = Minecraft.getInstance().player;
        assert player != null;

        if (DimensionalTearsPlatform.isEyeInDimTears(player))
            DimensionalTearsFluidRenderer.renderScreenEffect(player.level(), player, poseStack);
    }

}