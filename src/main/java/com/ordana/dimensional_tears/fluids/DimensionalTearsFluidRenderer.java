package com.ordana.dimensional_tears.fluids;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
//? fabric {
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
//?} else {
/*import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
*///?}
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.block.FluidRenderer;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.jspecify.annotations.Nullable;

import static com.ordana.dimensional_tears.fluids.DimensionalTearsFluidSpriteSet.renderFog;

public class DimensionalTearsFluidRenderer
    //? fabric
        implements FluidRenderHandler
    //? neoforge
    //implements IClientFluidTypeExtensions
{

    public static final float FOG_START = 0.1F;
    public static final float FOG_END = 2.0F;
    public static final Vector3f FOG_COLOR = ARGB.vector3fFromRGB24(0x100C1C);

    public DimensionalTearsFluidRenderer() {
    }

    public static FluidModel.Unbaked model() {
        return new FluidModel.Unbaked(new Material(DimensionalTearsFluidSpriteSet.DISCONNECTED.location, true), new Material(DimensionalTearsFluidSpriteSet.FLOWING, true), new Material(DimensionalTearsFluidSpriteSet.OVERLAY), null);
    }

    //? fabric {
    @Override
    public void renderFluid(FluidRenderer fluidRenderer, BlockPos pos, BlockAndTintGetter level, FluidRenderer.Output output, BlockState blockState, FluidState fluidState) {
        FluidRenderHandler.super.renderFluid(fluidRenderer, pos, level, output, blockState, fluidState);
    }
    //?}
    
    public static void renderScreenEffect(LevelReader levelReader, LocalPlayer player, PoseStack poseStack) {
        //fixme
    }

    //? neoforge {
    /*@Override
    public @Nullable Identifier getRenderOverlayTexture(Minecraft mc) {
        return DimensionalTearsFluidSpriteSet.UNDER;
    }

    @Override
    public void modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector4f fluidFogColor) {
        IClientFluidTypeExtensions.super.modifyFogColor(camera, partialTick, level, renderDistance, darkenWorldAmount, new Vector4f(FOG_COLOR.x, FOG_COLOR.y, FOG_COLOR.z, 255));
    }

    *///?}

    /*

    @Nullable
    @Override
    public Identifier getOverlayTexture() {
        return DimensionalTearsFluidSpriteSet.OVERLAY;
    }

    @Nullable
    @Override
    public Identifier getRenderOverlayTexture(Minecraft mc) {
        return DimensionalTearsFluidSpriteSet.UNDER;
    }

    @NotNull
    @Override
    public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
        return FOG_COLOR;
    }

    @Override
    public Identifier getStillTexture(FluidState fluidState, BlockAndTintGetter getter, BlockPos blockPos) {
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

    // Currently applies identical logic to water's fluid overlay
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

     */


}
