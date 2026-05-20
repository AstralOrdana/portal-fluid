package com.ordana.dimensional_tears.fabric.mixins.stupid_fluid_workarounds;

import com.mojang.blaze3d.systems.RenderSystem;
import com.ordana.dimensional_tears.DimensionalTearsPlatform;
import com.ordana.dimensional_tears.fluids.DimensionalTearsFluidSpriteSet;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    /*FIXME
    @Shadow private static float fogRed;
    @Shadow private static float fogGreen;
    @Shadow private static float fogBlue;

    @Shadow private static long biomeChangedTime;

    @Inject(method = "setupColor", at = @At("HEAD"), cancellable = true)
    private static void setupDimTearsFogColor(Camera camera, float f, ClientLevel clientLevel, int i, float g, CallbackInfo ci) {
        if (DimensionalTearsPlatform.isEyeInDimTears(camera.entity())) {
            fogRed = DimensionalTearsFluidSpriteSet.FOG_COLOR.x;
            fogGreen = DimensionalTearsFluidSpriteSet.FOG_COLOR.y;
            fogBlue = DimensionalTearsFluidSpriteSet.FOG_COLOR.z;
            biomeChangedTime = -1L;
//fixme
//            RenderSystem.clearColor(fogRed, fogGreen, fogBlue, 0.0F);

            ci.cancel();
        }
    }

     */

    @Inject(method = "setupFog", at = @At("HEAD"), cancellable = true)
    private static void renderDimTearsFog(Camera camera, int renderDistanceInChunks, DeltaTracker deltaTracker, float darkenWorldAmount, ClientLevel level, CallbackInfoReturnable<FogData> cir) {
        Entity entity = camera.entity();
        if (entity == null) return;

        if (DimensionalTearsPlatform.isEyeInDimTears(entity)) {
            cir.setReturnValue(DimensionalTearsFluidSpriteSet.renderFog(entity, renderDistanceInChunks));
        }
    }

}