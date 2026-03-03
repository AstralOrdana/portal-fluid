package com.ordana.portal_fluid.mixins.portal_fluid;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ordana.portal_fluid.reg.ModTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class EndSkyboxHideInFluidMixin {

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "renderEndSky", at = @At("HEAD"), cancellable = true)
    private void test(PoseStack poseStack, CallbackInfo ci) {
        if (this.minecraft.player != null && !this.minecraft.player.isEyeInFluid(ModTags.PORTAL_FLUID))
            ci.cancel();
    }

}
