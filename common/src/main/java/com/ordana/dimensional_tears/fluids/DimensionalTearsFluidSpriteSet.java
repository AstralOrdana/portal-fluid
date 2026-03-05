package com.ordana.dimensional_tears.fluids;

import com.google.common.collect.Lists;
import com.ordana.dimensional_tears.DimensionalTearsRoot;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class DimensionalTearsFluidSpriteSet {

    private static final List<DimensionalTearsFluidSpriteSet> SETS = Lists.newArrayList();

    public static final ResourceLocation FLOWING = DimensionalTearsRoot.res("block/dimensional_tears_flowing");
    public static final ResourceLocation OVERLAY = DimensionalTearsRoot.res("block/dimensional_tears_overlay");
    public static final ResourceLocation SCREEN = DimensionalTearsRoot.res("textures/block/dimensional_tears_screen.png");

    final TextureAtlasSprite[] sprites = new TextureAtlasSprite[3];
    final ResourceLocation still;

    public DimensionalTearsFluidSpriteSet(ResourceLocation still) {
        this.still = still;
        SETS.add(this);
    }

    static DimensionalTearsFluidSpriteSet createSpriteSet(String path) {
        return new DimensionalTearsFluidSpriteSet(DimensionalTearsRoot.res(path));
    }

    public static void populateSpriteSetArrays(TextureAtlas textureAtlas) {
        for (DimensionalTearsFluidSpriteSet set : SETS) {
            set.sprites[0] = textureAtlas.getSprite(set.still);
            set.sprites[1] = textureAtlas.getSprite(FLOWING);
            set.sprites[2] = textureAtlas.getSprite(OVERLAY);
        }
    }

}
