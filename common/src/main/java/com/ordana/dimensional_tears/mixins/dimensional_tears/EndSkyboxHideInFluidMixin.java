package com.ordana.dimensional_tears.mixins.dimensional_tears;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ordana.dimensional_tears.DimensionalTearsPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LevelRenderer.class, priority = 1500)
public class EndSkyboxHideInFluidMixin {

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "renderEndSky", at = @At("HEAD"), cancellable = true)
    private void test(PoseStack poseStack, CallbackInfo ci) {
        assert this.minecraft.player != null;
        if (DimensionalTearsPlatform.isEyeInDimTears(this.minecraft.player))
            ci.cancel();
    }

}
