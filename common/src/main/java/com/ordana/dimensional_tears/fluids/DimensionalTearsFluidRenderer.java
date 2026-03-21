package com.ordana.dimensional_tears.fluids;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.mehvahdjukaar.moonlight.api.client.ModFluidRenderProperties;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class DimensionalTearsFluidRenderer extends ModFluidRenderProperties {

    public static final float FOG_START = 0.1F;
    public static final float FOG_END = 2.0F;
    public static final Vector3f FOG_COLOR = Vec3.fromRGB24(0x100C1C).toVector3f();

    public DimensionalTearsFluidRenderer() {
        super(DimensionalTearsFluidSpriteSet.DISCONNECTED.location, DimensionalTearsFluidSpriteSet.FLOWING);
    }

    @Nullable
    @Override
    public ResourceLocation getOverlayTexture() {
        return DimensionalTearsFluidSpriteSet.OVERLAY;
    }

    @Nullable
    @Override
    public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
        return DimensionalTearsFluidSpriteSet.UNDER;
    }

    @NotNull
    @Override
    public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
        return FOG_COLOR;
    }

    @Override
    public ResourceLocation getStillTexture(FluidState fluidState, BlockAndTintGetter getter, BlockPos blockPos) {
        @Nullable DimensionalTearsFluidSpriteSet spriteSet = DimensionalTearsFluidSpriteSet.getConnected(getter, blockPos);

        if (spriteSet == null)
            spriteSet = DimensionalTearsFluidSpriteSet.getDisconnected(getter, blockPos);

        return spriteSet.location;
    }

    // fabric
    public void reloadTextures(TextureAtlas textureAtlas) {
        DimensionalTearsFluidSpriteSet.populateSpriteSetArrays(textureAtlas);
    }

    // fabric
    public TextureAtlasSprite[] getFluidSprites(@Nullable BlockAndTintGetter getter, @Nullable BlockPos blockPos, FluidState fluidState) {
        return DimensionalTearsFluidSpriteSet.getSpriteSet(getter, blockPos).sprites;
    }

    @Override
    public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float viewDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
        renderFog(camera.getEntity(), viewDistance);
    }

    public static void renderFog(Entity entity, float viewDistance) {
        if (entity.isSpectator()) {
            RenderSystem.setShaderFogStart(-8.0F);
            RenderSystem.setShaderFogEnd(viewDistance * 0.5F);
        }
        else {
            RenderSystem.setShaderFogStart(FOG_START);
            RenderSystem.setShaderFogEnd(FOG_END);
        }
    }

    /**
     * Currently applies identical logic to vanilla's water overlay.
     */
    public static void renderScreenEffect(LevelReader levelReader, LocalPlayer player, PoseStack poseStack) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, DimensionalTearsFluidSpriteSet.UNDER);

        BlockPos blockPos = BlockPos.containing(player.getX(), player.getEyeY(), player.getZ());
        float brightness = LightTexture.getBrightness(levelReader.dimensionType(), levelReader.getMaxLocalRawBrightness(blockPos));

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(brightness, brightness, brightness, 0.1F);

        float yaw = -player.getYRot() / 64.0F;
        float pitch = player.getXRot() / 64.0F;

        Matrix4f matrix4f = poseStack.last().pose();
        BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        bufferBuilder.addVertex(matrix4f, -1.0F, -1.0F, -0.5F).setUv(4.0F + yaw, 4.0F + pitch);
        bufferBuilder.addVertex(matrix4f, 1.0F, -1.0F, -0.5F).setUv(0.0F + yaw, 4.0F + pitch);
        bufferBuilder.addVertex(matrix4f, 1.0F, 1.0F, -0.5F).setUv(0.0F + yaw, 0.0F + pitch);
        bufferBuilder.addVertex(matrix4f, -1.0F, 1.0F, -0.5F).setUv(4.0F + yaw, 0.0F + pitch);

        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }

}
