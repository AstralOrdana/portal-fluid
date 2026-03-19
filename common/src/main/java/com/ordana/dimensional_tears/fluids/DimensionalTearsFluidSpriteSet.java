package com.ordana.dimensional_tears.fluids;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.reg.ModTags;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;
import java.util.Objects;

public class DimensionalTearsFluidSpriteSet {

    public static final List<DimensionalTearsFluidSpriteSet> SETS = Lists.newArrayList();

    private static final int UNCOMMON_RARITY = 10;
    private static final int RARE_RARITY = 200;

    private static final float FOG_START = 0.1F;
    private static final float FOG_END = 2.0F;
    public static final Vector3f FOG_COLOR = Vec3.fromRGB24(0x100C1C).toVector3f();

    public static final ResourceLocation FLOWING = DimensionalTearsRoot.res("block/dimensional_tears_flowing");
    public static final ResourceLocation OVERLAY = DimensionalTearsRoot.res("block/dimensional_tears_overlay");
    public static final ResourceLocation SCREEN = DimensionalTearsRoot.res("textures/block/dimensional_tears_screen.png");

    public static final DimensionalTearsFluidSpriteSet
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

    public final TextureAtlasSprite[] sprites = new TextureAtlasSprite[3];
    public final ResourceLocation location;

    public DimensionalTearsFluidSpriteSet(ResourceLocation location) {
        this.location = location;
        SETS.add(this);
    }

    static DimensionalTearsFluidSpriteSet createSpriteSet(String path) {
        return new DimensionalTearsFluidSpriteSet(DimensionalTearsRoot.res(path));
    }

    public static void populateSpriteSetArrays(TextureAtlas textureAtlas) {
        for (DimensionalTearsFluidSpriteSet set : SETS) {
            set.sprites[0] = textureAtlas.getSprite(set.location);
            set.sprites[1] = textureAtlas.getSprite(FLOWING);
            set.sprites[2] = textureAtlas.getSprite(OVERLAY);
        }
    }

    public static DimensionalTearsFluidSpriteSet getSpriteSet(BlockAndTintGetter getter, BlockPos blockPos) {
        if (blockPos == null || getter == null)
            return DISCONNECTED;

        return Objects.requireNonNullElse(getConnected(getter, blockPos), getDisconnected(getter, blockPos));
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

    @Nullable
    public static DimensionalTearsFluidSpriteSet getConnected(BlockAndTintGetter getter, BlockPos blockPos) {
        if (areAllNeighborsNonFluid(getter, blockPos))
            return ALL;

        boolean northNonFluid = !hasConnectibleNeighbor(getter, blockPos, Direction.NORTH);
        boolean southNonFluid = !hasConnectibleNeighbor(getter, blockPos, Direction.SOUTH);
        boolean eastNonFluid = !hasConnectibleNeighbor(getter, blockPos, Direction.EAST);
        boolean westNonFluid = !hasConnectibleNeighbor(getter, blockPos, Direction.WEST);

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

    public static DimensionalTearsFluidSpriteSet getDisconnected(BlockAndTintGetter getter, BlockPos blockPos) {
        if (getter.getBlockState(blockPos.below()).is(Blocks.RAW_IRON_BLOCK))
            return DISCONNECTED_SNENCE;

        if (isRandomlySelectedPosition(blockPos, RARE_RARITY))
            return DISCONNECTED_RARE;

        if (isRandomlySelectedPosition(blockPos, UNCOMMON_RARITY))
            return DISCONNECTED_UNCOMMON;

        return DISCONNECTED;
    }

    @SuppressWarnings("deprecation")
    private static boolean isRandomlySelectedPosition(BlockPos pos, int rarity) {
        return RandomSource.create(Mth.getSeed(pos)).nextInt(rarity) == 0;
    }

    private static boolean areAllNeighborsNonFluid(BlockAndTintGetter getter, BlockPos pos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (hasConnectibleNeighbor(getter, pos, direction))
                return false;
        }

        return true;
    }

    private static boolean hasConnectibleNeighbor(BlockAndTintGetter getter, BlockPos pos, Direction dir) {
        BlockPos blockPos = pos.relative(dir);
        FluidState fluidState = getter.getFluidState(blockPos);

        return fluidState.is(ModTags.DIMENSIONAL_TEARS);
    }

}
