package com.ordana.dimensional_tears.fabric.mixins.stupid_fluid_workarounds;

import com.mojang.blaze3d.systems.RenderSystem;
import com.ordana.dimensional_tears.fabric.DimensionalTearsPlatformImpl;
import com.ordana.dimensional_tears.fluids.DimensionalTearsFluidSpriteSet;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @Shadow private static float fogRed;
    @Shadow private static float fogGreen;
    @Shadow private static float fogBlue;

    @Shadow private static long biomeChangedTime;

    @Inject(method = "setupColor", at = @At("HEAD"), cancellable = true)
    private static void setupDimTearsFogColor(Camera camera, float f, ClientLevel clientLevel, int i, float g, CallbackInfo ci) {
        if (DimensionalTearsPlatformImpl.isEyeInDimTears(camera.getEntity())) {
            fogRed = DimensionalTearsFluidSpriteSet.FOG_COLOR.x;
            fogGreen = DimensionalTearsFluidSpriteSet.FOG_COLOR.y;
            fogBlue = DimensionalTearsFluidSpriteSet.FOG_COLOR.z;
            biomeChangedTime = -1L;

            RenderSystem.clearColor(fogRed, fogGreen, fogBlue, 0.0F);

            ci.cancel();
        }
    }

    @Inject(method = "setupFog", at = @At("HEAD"), cancellable = true)
    private static void renderDimTearsFog(Camera camera, FogRenderer.FogMode fogMode, float viewDistance, boolean thickenFog, float partialTicks, CallbackInfo ci) {
        Entity entity = camera.getEntity();

        if (DimensionalTearsPlatformImpl.isEyeInDimTears(entity)) {
            DimensionalTearsFluidSpriteSet.renderFog(entity, viewDistance);
            ci.cancel();
        }
    }

}