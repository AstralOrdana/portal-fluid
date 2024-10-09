package com.ordana.portal_fluid.fluids;

import com.ordana.portal_fluid.PortalFluidRoot;
import com.ordana.portal_fluid.configs.ClientConfigs;
import com.ordana.portal_fluid.reg.ModFluids;
import net.mehvahdjukaar.moonlight.api.client.ModFluidRenderProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class PortalFluidFluidRenderer extends ModFluidRenderProperties {
    private final ResourceLocation overlay;
    private final ResourceLocation renderOverlay;
    private final Vec3 fogColor;

    public PortalFluidFluidRenderer(ResourceLocation still, ResourceLocation flowing, int tint, ResourceLocation overlay, ResourceLocation renderOverlay, Vec3 fogColor) {
        super(still, flowing, tint);
        this.overlay = overlay;
        this.renderOverlay = renderOverlay;
        this.fogColor = fogColor;
    }

    static ResourceLocation portalFluidLargeSW = PortalFluidRoot.res("block/portal_fluid_large_sw");
    static ResourceLocation portalFluidLargeSE = PortalFluidRoot.res("block/portal_fluid_large_se");
    static ResourceLocation portalFluidLargeNW = PortalFluidRoot.res("block/portal_fluid_large_nw");
    static ResourceLocation portalFluidLargeNE = PortalFluidRoot.res("block/portal_fluid_large_ne");

    static ResourceLocation portalFluidN = PortalFluidRoot.res("block/portal_fluid_n");
    static ResourceLocation portalFluidE = PortalFluidRoot.res("block/portal_fluid_e");
    static ResourceLocation portalFluidS = PortalFluidRoot.res("block/portal_fluid_s");
    static ResourceLocation portalFluidW = PortalFluidRoot.res("block/portal_fluid_w");

    static ResourceLocation portalFluidSW = PortalFluidRoot.res("block/portal_fluid_sw");
    static ResourceLocation portalFluidSE = PortalFluidRoot.res("block/portal_fluid_se");
    static ResourceLocation portalFluidNW = PortalFluidRoot.res("block/portal_fluid_nw");
    static ResourceLocation portalFluidNE = PortalFluidRoot.res("block/portal_fluid_ne");

    static ResourceLocation portalFluidNS = PortalFluidRoot.res("block/portal_fluid_ns");
    static ResourceLocation portalFluidEW = PortalFluidRoot.res("block/portal_fluid_ew");

    static ResourceLocation portalFluidWNE = PortalFluidRoot.res("block/portal_fluid_wne");
    static ResourceLocation portalFluidNES = PortalFluidRoot.res("block/portal_fluid_nes");
    static ResourceLocation portalFluidESW = PortalFluidRoot.res("block/portal_fluid_esw");
    static ResourceLocation portalFluidSWN = PortalFluidRoot.res("block/portal_fluid_swn");

    static ResourceLocation portalFluidNESW = PortalFluidRoot.res("block/portal_fluid_nesw");
    static ResourceLocation portalFluidNONE = PortalFluidRoot.res("block/portal_fluid_none");
    static ResourceLocation portalFluidUncommon = PortalFluidRoot.res("block/portal_fluid_uncommon");
    static ResourceLocation portalFluidRare = PortalFluidRoot.res("block/portal_fluid_rare");

    static ResourceLocation portalFluidSnence = PortalFluidRoot.res("block/portal_fluid_snence");
    static ResourceLocation portalFluidMaple = PortalFluidRoot.res("block/portal_fluid_maple");



    TextureAtlasSprite[] portalFluidSpriteLargeSW = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteLargeSE = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteLargeNW = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteLargeNE = new TextureAtlasSprite[3];

    TextureAtlasSprite[] portalFluidSpriteN = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteE = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteS = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteW = new TextureAtlasSprite[3];

    TextureAtlasSprite[] portalFluidSpriteSW = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteSE = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteNW = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteNE = new TextureAtlasSprite[3];

    TextureAtlasSprite[] portalFluidSpriteNS = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteEW = new TextureAtlasSprite[3];

    TextureAtlasSprite[] portalFluidSpriteWNE  = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteNES  = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteESW  = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteSWN  = new TextureAtlasSprite[3];

    TextureAtlasSprite[] portalFluidSpriteNESW = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteNONE = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteUncommon = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteRare = new TextureAtlasSprite[3];

    TextureAtlasSprite[] portalFluidSpriteSnence = new TextureAtlasSprite[3];
    TextureAtlasSprite[] portalFluidSpriteMaple = new TextureAtlasSprite[3];

    private boolean isPortalFluid(FluidState state) {
        return state.is(ModFluids.PORTAL_FLUID.get()) || state.is(ModFluids.FLOWING_PORTAL_FLUID.get());
    }

    private boolean isRandomPos(BlockPos pos, int rarity) {
        Random random = new Random((long) (Mth.getSeed(pos) * ClientConfigs.PORTAL_FLUID_SEED.get()));
        return random.nextInt(rarity) == 0;
    }

    private boolean areAllNeighborsFluid(BlockPos pos) {
        Level level = Minecraft.getInstance().level;
        return (level != null && isPortalFluid(level.getFluidState(pos.relative(Direction.NORTH))))
                && isPortalFluid(level.getFluidState(pos.relative(Direction.EAST)))
                && isPortalFluid(level.getFluidState(pos.relative(Direction.SOUTH)))
                && isPortalFluid(level.getFluidState(pos.relative(Direction.WEST)));
    }

    private boolean isNonFluidAdjacent(BlockPos pos, Direction dir) {
        Level level = Minecraft.getInstance().level;
        return level != null && !isPortalFluid(level.getFluidState(pos.relative(dir)));
    }

    private BlockState getBelowBlock(BlockPos pos) {
        Level level = Minecraft.getInstance().level;
        return level.getBlockState(pos.below());
    }

    @Override
    public ResourceLocation getStillTexture(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        var texture = portalFluidNONE;
        if (isRandomPos(pos, 10)) texture = portalFluidUncommon;
        if (isRandomPos(pos, 20)) texture = portalFluidRare;

        if (isNonFluidAdjacent(pos, Direction.NORTH) && isNonFluidAdjacent(pos, Direction.SOUTH) && isNonFluidAdjacent(pos, Direction.EAST) && isNonFluidAdjacent(pos, Direction.WEST)) {
            return portalFluidNESW;
        }

        else if (getBelowBlock(pos).is(Blocks.HONEY_BLOCK)) return portalFluidMaple;
        else if (getBelowBlock(pos).is(Blocks.RAW_IRON_BLOCK)) return portalFluidSnence;
        else if (isNonFluidAdjacent(pos, Direction.NORTH)) {
            texture = portalFluidN;

            if (isNonFluidAdjacent(pos, Direction.EAST)) {
                texture = portalFluidNE;

                if (isNonFluidAdjacent(pos, Direction.WEST)) {
                    return portalFluidWNE;
                }
            }

            if (isNonFluidAdjacent(pos, Direction.WEST)) {
                texture = portalFluidNW;

                if (isNonFluidAdjacent(pos, Direction.SOUTH)) {
                    return portalFluidSWN;
                }
            }

            if (isNonFluidAdjacent(pos, Direction.SOUTH)) {
                texture = portalFluidNS;

                if (isNonFluidAdjacent(pos, Direction.EAST)) {
                    return portalFluidNES;
                }
            }
        }

        else if (isNonFluidAdjacent(pos, Direction.EAST)) {
            texture = portalFluidE;

            if (isNonFluidAdjacent(pos, Direction.SOUTH)) {
                texture = portalFluidSE;

                if (isNonFluidAdjacent(pos, Direction.WEST)) {
                    return portalFluidESW;
                }
            }

            else if (isNonFluidAdjacent(pos, Direction.WEST)) {
                return portalFluidEW;
            }
        }

        else if (isNonFluidAdjacent(pos, Direction.SOUTH)) {
            texture = portalFluidS;

            if (isNonFluidAdjacent(pos, Direction.WEST)) {
                return portalFluidSW;
            }
        }

        else if (isNonFluidAdjacent(pos, Direction.WEST)) {
            return portalFluidW;
        }

        if (areAllNeighborsFluid(pos)) {
            if (areAllNeighborsFluid(pos.north()) && areAllNeighborsFluid(pos.west()) && areAllNeighborsFluid(pos.west().north())) {

                var origin = isRandomPos(pos, 200);
                var south = isRandomPos(pos.south(), 200);
                var east = isRandomPos(pos.east(), 200);
                var southEast = isRandomPos(pos.south().east(), 200);

                if (origin) return portalFluidLargeSE;
                else if (south) return portalFluidLargeNW;
                else if (east) return portalFluidLargeSW;
                else if (southEast) return portalFluidLargeNE;
            }
        }

        return texture;
    }

    public void reloadTextures(TextureAtlas textureAtlas) {

        //still textures
        portalFluidSpriteLargeSW[0] = textureAtlas.getSprite(portalFluidLargeSW);
        portalFluidSpriteLargeSE[0] = textureAtlas.getSprite(portalFluidLargeSE);
        portalFluidSpriteLargeNW[0] = textureAtlas.getSprite(portalFluidLargeNW);
        portalFluidSpriteLargeNE[0] = textureAtlas.getSprite(portalFluidLargeNE);

        portalFluidSpriteN[0] = textureAtlas.getSprite(portalFluidN);
        portalFluidSpriteE[0] = textureAtlas.getSprite(portalFluidE);
        portalFluidSpriteS[0] = textureAtlas.getSprite(portalFluidS);
        portalFluidSpriteW[0] = textureAtlas.getSprite(portalFluidW);

        portalFluidSpriteSW[0] = textureAtlas.getSprite(portalFluidSW);
        portalFluidSpriteSE[0] = textureAtlas.getSprite(portalFluidSE);
        portalFluidSpriteNW[0] = textureAtlas.getSprite(portalFluidNW);
        portalFluidSpriteNE[0] = textureAtlas.getSprite(portalFluidNE);

        portalFluidSpriteNS[0] = textureAtlas.getSprite(portalFluidNS);
        portalFluidSpriteEW[0] = textureAtlas.getSprite(portalFluidEW);

        portalFluidSpriteWNE[0] = textureAtlas.getSprite(portalFluidWNE);
        portalFluidSpriteNES[0] = textureAtlas.getSprite(portalFluidNES);
        portalFluidSpriteESW[0] = textureAtlas.getSprite(portalFluidESW);
        portalFluidSpriteSWN[0] = textureAtlas.getSprite(portalFluidSWN);

        portalFluidSpriteNESW[0] = textureAtlas.getSprite(portalFluidNESW);
        portalFluidSpriteNONE[0] = textureAtlas.getSprite(portalFluidNONE);
        portalFluidSpriteUncommon[0] = textureAtlas.getSprite(portalFluidUncommon);
        portalFluidSpriteRare[0] = textureAtlas.getSprite(portalFluidRare);

        portalFluidSpriteSnence[0] = textureAtlas.getSprite(portalFluidSnence);
        portalFluidSpriteMaple[0] = textureAtlas.getSprite(portalFluidMaple);

        //flowing textures

        portalFluidSpriteLargeSW[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteLargeSE[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteLargeNW[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteLargeNE[1] = textureAtlas.getSprite(getFlowingTexture());

        portalFluidSpriteN[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteE[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteS[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteW[1] = textureAtlas.getSprite(getFlowingTexture());

        portalFluidSpriteSW[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteSE[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteNW[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteNE[1] = textureAtlas.getSprite(getFlowingTexture());

        portalFluidSpriteNS[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteEW[1] = textureAtlas.getSprite(getFlowingTexture());

        portalFluidSpriteWNE[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteNES[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteESW[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteSWN[1] = textureAtlas.getSprite(getFlowingTexture());

        portalFluidSpriteNESW[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteNONE[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteUncommon[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteRare[1] = textureAtlas.getSprite(getFlowingTexture());

        portalFluidSpriteSnence[1] = textureAtlas.getSprite(getFlowingTexture());
        portalFluidSpriteMaple[1] = textureAtlas.getSprite(getFlowingTexture());


        var overlayTexture = this.getOverlayTexture();
        if (overlayTexture != null) {

            portalFluidSpriteLargeSW[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteLargeSE[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteLargeNW[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteLargeNE[2] = textureAtlas.getSprite(overlayTexture);

            portalFluidSpriteN[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteE[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteS[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteW[2] = textureAtlas.getSprite(overlayTexture);

            portalFluidSpriteSW[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteSE[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteNW[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteNE[2] = textureAtlas.getSprite(overlayTexture);

            portalFluidSpriteNS[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteEW[2] = textureAtlas.getSprite(overlayTexture);

            portalFluidSpriteWNE[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteNES[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteESW[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteSWN[2] = textureAtlas.getSprite(overlayTexture);

            portalFluidSpriteNESW[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteNONE[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteUncommon[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteRare[2] = textureAtlas.getSprite(overlayTexture);

            portalFluidSpriteSnence[2] = textureAtlas.getSprite(overlayTexture);
            portalFluidSpriteMaple[2] = textureAtlas.getSprite(overlayTexture);
        }
    }

    public TextureAtlasSprite[] getFluidSprites(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
        if (pos == null) return portalFluidSpriteNONE;

        var texture = portalFluidSpriteNONE;
        if (isRandomPos(pos, 10)) texture = portalFluidSpriteUncommon;
        if (isRandomPos(pos, 20)) texture = portalFluidSpriteRare;

        if (isNonFluidAdjacent(pos, Direction.NORTH) && isNonFluidAdjacent(pos, Direction.SOUTH) && isNonFluidAdjacent(pos, Direction.EAST) && isNonFluidAdjacent(pos, Direction.WEST)) {
            return portalFluidSpriteNESW;
        }
        else if (getBelowBlock(pos).is(Blocks.HONEY_BLOCK)) return portalFluidSpriteMaple;
        else if (getBelowBlock(pos).is(Blocks.RAW_IRON_BLOCK)) return portalFluidSpriteSnence;
        else if (isNonFluidAdjacent(pos, Direction.NORTH)) {
            texture = portalFluidSpriteN;

            if (isNonFluidAdjacent(pos, Direction.EAST)) {
                texture = portalFluidSpriteNE;

                if (isNonFluidAdjacent(pos, Direction.WEST)) {
                    return portalFluidSpriteWNE;
                }
            }

            if (isNonFluidAdjacent(pos, Direction.WEST)) {
                texture = portalFluidSpriteNW;

                if (isNonFluidAdjacent(pos, Direction.SOUTH)) {
                    return portalFluidSpriteSWN;
                }
            }

            if (isNonFluidAdjacent(pos, Direction.SOUTH)) {
                texture = portalFluidSpriteNS;

                if (isNonFluidAdjacent(pos, Direction.EAST)) {
                    return portalFluidSpriteNES;
                }
            }
        }

        else if (isNonFluidAdjacent(pos, Direction.EAST)) {
            texture = portalFluidSpriteE;

            if (isNonFluidAdjacent(pos, Direction.SOUTH)) {
                texture = portalFluidSpriteSE;

                if (isNonFluidAdjacent(pos, Direction.WEST)) {
                    return portalFluidSpriteESW;
                }
            }

            else if (isNonFluidAdjacent(pos, Direction.WEST)) {
                return portalFluidSpriteEW;
            }
        }

        else if (isNonFluidAdjacent(pos, Direction.SOUTH)) {
            texture = portalFluidSpriteS;

            if (isNonFluidAdjacent(pos, Direction.WEST)) {
                return portalFluidSpriteSW;
            }
        }

        else if (isNonFluidAdjacent(pos, Direction.WEST)) {
            return portalFluidSpriteW;
        }

        if (areAllNeighborsFluid(pos)) {
            if (areAllNeighborsFluid(pos.north()) && areAllNeighborsFluid(pos.west()) && areAllNeighborsFluid(pos.west().north())) {

                var origin = isRandomPos(pos, 200);
                var south = isRandomPos(pos.south(), 200);
                var east = isRandomPos(pos.east(), 200);
                var southEast = isRandomPos(pos.south().east(), 200);

                if (origin) return portalFluidSpriteLargeSE;
                else if (south) return portalFluidSpriteLargeNW;
                else if (east) return portalFluidSpriteLargeSW;
                else if (southEast) return portalFluidSpriteLargeNE;
            }
        }
        
        return texture;
    }

    @Nullable
    @Override
    public ResourceLocation getOverlayTexture() {
        return this.overlay;
    }

    public ResourceLocation getOverlayTexture(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return this.getOverlayTexture();
    }

    public ResourceLocation getRenderOverlayTexture() {
        return this.renderOverlay;
    }

    public ResourceLocation getRenderOverlayTexture(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return this.getRenderOverlayTexture();
    }

    public Vec3 modifyFogColor() {
        return this.fogColor;
    }

    public Vec3 modifyFogColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
        return this.modifyFogColor();
    }

}
