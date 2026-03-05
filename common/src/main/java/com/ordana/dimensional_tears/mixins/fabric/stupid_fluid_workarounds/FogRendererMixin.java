package com.ordana.dimensional_tears.mixins.fabric.stupid_fluid_workarounds;

import com.mojang.blaze3d.systems.RenderSystem;
import com.ordana.dimensional_tears.fluids.DimensionalTearsFluidRenderer;
import com.ordana.dimensional_tears.reg.ModTags;
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
    private static void setupPortalFluidColor(Camera camera, float f, ClientLevel clientLevel, int i, float g, CallbackInfo ci) {
        if (camera.getEntity().isEyeInFluid(ModTags.DIMENSIONAL_TEARS)) {
            fogRed = DimensionalTearsFluidRenderer.FOG_COLOR.x;
            fogGreen = DimensionalTearsFluidRenderer.FOG_COLOR.y;
            fogBlue = DimensionalTearsFluidRenderer.FOG_COLOR.z;
            biomeChangedTime = -1L;

            RenderSystem.clearColor(fogRed, fogGreen, fogBlue, 0.0F);

            ci.cancel();
        }
    }

    @Inject(method = "setupFog", at = @At("HEAD"), cancellable = true)
    private static void setupPortalFluidFogParameters(Camera camera, FogRenderer.FogMode fogMode, float viewDistance, boolean thickenFog, float partialTicks, CallbackInfo ci) {
        Entity entity = camera.getEntity();

        if (entity.isEyeInFluid(ModTags.DIMENSIONAL_TEARS)) {
            if (entity.isSpectator()) {
                RenderSystem.setShaderFogStart(-8.0F);
                RenderSystem.setShaderFogEnd(viewDistance * 0.5F);
            }
            else {
                RenderSystem.setShaderFogStart(DimensionalTearsFluidRenderer.FOG_START);
                RenderSystem.setShaderFogEnd(DimensionalTearsFluidRenderer.FOG_END);
            }

            ci.cancel();
        }
    }

}