package com.ordana.dimensional_tears.mixins.dimensional_tears;

import com.ordana.dimensional_tears.DimensionalTearsPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SkyRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(SkyRenderer.class)
public class LevelRendererMixin {

    @Inject(method = "renderEndSky", at = @At("HEAD"), cancellable = true)
    private void cancelEndSkyboxRenderingInDimTears(CallbackInfo ci) {
        if (DimensionalTearsPlatform.isEyeInDimTears(Objects.requireNonNull(Minecraft.getInstance().player)))
            ci.cancel();
    }

}
