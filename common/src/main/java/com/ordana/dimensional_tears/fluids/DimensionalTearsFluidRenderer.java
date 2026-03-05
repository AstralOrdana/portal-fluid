package com.ordana.dimensional_tears.fluids;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.ordana.dimensional_tears.configs.ClientConfigs;
import com.ordana.dimensional_tears.reg.ModTags;
import net.mehvahdjukaar.moonlight.api.client.ModFluidRenderProperties;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import static com.ordana.dimensional_tears.fluids.DimensionalTearsFluidSpriteSet.createSpriteSet;

@SuppressWarnings("unused")
public class DimensionalTearsFluidRenderer extends ModFluidRenderProperties {

    private static final int UNCOMMON_RARITY = 10;
    private static final int RARE_RARITY = 600;

    public static final float FOG_START = 0.1F;
    public static final float FOG_END = 2.0F;
    public static final Vector3f FOG_COLOR = Vec3.fromRGB24(0x100C1C).toVector3f();
    
    private static final DimensionalTearsFluidSpriteSet
        NORTH = createSpriteSet("block/dimensional_tears_n"),
        EAST = createSpriteSet("block/dimensional_tears_e"),
        SOUTH = createSpriteSet("block/dimensional_tears_s"),
        WEST = createSpriteSet("block/dimensional_tears_w"),
        SOUTH_WEST = createSpriteSet("block/dimensional_tears_sw"),
        SOUTH_EAST = createSpriteSet("block/dimensional_tears_se"),
        NORTH_WEST = createSpriteSet("block/dimensional_tears_nw"),
        NORTH_EAST = createSpriteSet("block/dimensional_tears_ne"),
        NORTH_SOUTH = createSpriteSet("block/dimensional_tears_ns"),
        EAST_WEST = createSpriteSet("block/dimensional_tears_ew"),
        WEST_NORTH_EAST = createSpriteSet("block/dimensional_tears_wne"),
        NORTH_EAST_SOUTH = createSpriteSet("block/dimensional_tears_nes"),
        EAST_SOUTH_WEST = createSpriteSet("block/dimensional_tears_esw"),
        SOUTH_WEST_NORTH = createSpriteSet("block/dimensional_tears_swn"),
        ALL = createSpriteSet("block/dimensional_tears_all"),
        DISCONNECTED = createSpriteSet("block/dimensional_tears_disconnected"),
        DISCONNECTED_UNCOMMON = createSpriteSet("block/dimensional_tears_disconnected_uncommon"),
        DISCONNECTED_RARE = createSpriteSet("block/dimensional_tears_disconnected_rare"),
        DISCONNECTED_SNENCE = createSpriteSet("block/dimensional_tears_disconnected_snence");

    public DimensionalTearsFluidRenderer() {
        super(DISCONNECTED.still, DimensionalTearsFluidSpriteSet.FLOWING);
    }

    @Nullable
    @Override
    public ResourceLocation getOverlayTexture() {
        return DimensionalTearsFluidSpriteSet.OVERLAY;
    }

    @Nullable
    @Override
    public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
        return DimensionalTearsFluidSpriteSet.SCREEN;
    }

    @NotNull
    @Override
    public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
        return FOG_COLOR;
    }

    @Override
    public ResourceLocation getStillTexture(FluidState fluidState, BlockAndTintGetter getter, BlockPos blockPos) {
        @Nullable DimensionalTearsFluidSpriteSet spriteSet = this.getConnectedSpriteSet(getter, blockPos);

        if (spriteSet == null)
            spriteSet = this.getDisconnectedSpriteSet(getter, blockPos);

        return spriteSet.still;
    }

    @Nullable
    private DimensionalTearsFluidSpriteSet getConnectedSpriteSet(BlockAndTintGetter getter, BlockPos blockPos) {
        if (this.areAllNeighborsNonFluid(getter, blockPos))
            return ALL;

        boolean northNonFluid = !this.hasConnectibleNeighbor(getter, blockPos, Direction.NORTH);
        boolean southNonFluid = !this.hasConnectibleNeighbor(getter, blockPos, Direction.SOUTH);
        boolean eastNonFluid = !this.hasConnectibleNeighbor(getter, blockPos, Direction.EAST);
        boolean westNonFluid = !this.hasConnectibleNeighbor(getter, blockPos, Direction.WEST);

        DimensionalTearsFluidSpriteSet spriteSet = null;

        if (northNonFluid) {
            spriteSet = NORTH;
            if (eastNonFluid) {
                spriteSet = NORTH_EAST;
                if (westNonFluid) {
                    return WEST_NORTH_EAST;
                }
            }
            else if (westNonFluid) {
                spriteSet = NORTH_WEST;
                if (southNonFluid) {
                    return SOUTH_WEST_NORTH;
                }
            }

            if (southNonFluid) {
                spriteSet = NORTH_SOUTH;
                if (eastNonFluid) {
                    return NORTH_EAST_SOUTH;
                }
            }
            return spriteSet;
        }
        else if (eastNonFluid) {
            spriteSet = EAST;
            if (southNonFluid) {
                spriteSet = SOUTH_EAST;
                if (westNonFluid) {
                    return EAST_SOUTH_WEST;
                }
            }
            else if (westNonFluid) {
                spriteSet = EAST_WEST;
            }
        }
        else if (southNonFluid) {
            spriteSet = SOUTH;
            if (westNonFluid) {
                return SOUTH_WEST;
            }
        }
        else if (westNonFluid) {
            spriteSet = WEST;
        }

        return spriteSet;
    }

    public void reloadTextures(TextureAtlas textureAtlas) {
        DimensionalTearsFluidSpriteSet.populateSpriteSetArrays(textureAtlas);
    }

    public TextureAtlasSprite[] getFluidSprites(@Nullable BlockAndTintGetter getter, @Nullable BlockPos blockPos, FluidState fluidState) {
        DimensionalTearsFluidSpriteSet spriteSet = DISCONNECTED;

        if (blockPos != null && getter != null) {
            spriteSet = this.getConnectedSpriteSet(getter, blockPos);

            if (spriteSet == null)
                spriteSet = this.getDisconnectedSpriteSet(getter, blockPos);
        }

        return spriteSet.sprites;
    }

    @Override
    public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float viewDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
        if (camera.getEntity().isSpectator()) {
            RenderSystem.setShaderFogStart(-8.0F);
            RenderSystem.setShaderFogEnd(viewDistance * 0.5F);
        }
        else {
            RenderSystem.setShaderFogStart(DimensionalTearsFluidRenderer.FOG_START);
            RenderSystem.setShaderFogEnd(DimensionalTearsFluidRenderer.FOG_END);
        }
    }

    private DimensionalTearsFluidSpriteSet getDisconnectedSpriteSet(BlockAndTintGetter getter, BlockPos blockPos) {
        if (getter.getBlockState(blockPos.below()).is(Blocks.RAW_IRON_BLOCK))
            return DISCONNECTED_SNENCE;

        if (this.isRandomPos(blockPos, RARE_RARITY))
            return DISCONNECTED_RARE;

        if (this.isRandomPos(blockPos, UNCOMMON_RARITY))
            return DISCONNECTED_UNCOMMON;

        return DISCONNECTED;
    }

    private boolean canConnectTo(BlockAndTintGetter getter, BlockPos blockPos, FluidState other) {
        return other.is(ModTags.DIMENSIONAL_TEARS) && other.getFlow(getter, blockPos).horizontalDistanceSqr() == 0;
    }

    @SuppressWarnings("deprecation")
    private boolean isRandomPos(BlockPos pos, int rarity) {
        long seed = (long) (Mth.getSeed(pos) * ClientConfigs.DIMENSIONAL_TEARS_SEED.get());
        return RandomSource.create(seed).nextInt(rarity) == 0;
    }

    private boolean areAllNeighborsNonFluid(BlockAndTintGetter getter, BlockPos pos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (this.hasConnectibleNeighbor(getter, pos, direction))
                return false;
        }

        return true;
    }

    private boolean hasConnectibleNeighbor(BlockAndTintGetter getter, BlockPos pos, Direction dir) {
        BlockPos blockPos = pos.relative(dir);
        FluidState fluidState = getter.getFluidState(blockPos);
        return this.canConnectTo(getter, blockPos, fluidState);
    }

}
