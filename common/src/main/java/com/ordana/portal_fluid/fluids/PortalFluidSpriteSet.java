package com.ordana.portal_fluid.fluids;

import com.google.common.collect.Lists;
import com.ordana.portal_fluid.PortalFluidRoot;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class PortalFluidSpriteSet {

    private static final List<PortalFluidSpriteSet> SETS = Lists.newArrayList();

    public static final ResourceLocation FLOWING = PortalFluidRoot.res("block/portal_fluid_flowing");
    public static final ResourceLocation OVERLAY = PortalFluidRoot.res("block/portal_fluid_overlay");

    final TextureAtlasSprite[] sprites = new TextureAtlasSprite[3];
    final ResourceLocation still;

    public PortalFluidSpriteSet(ResourceLocation still) {
        this.still = still;
        SETS.add(this);
    }

    static PortalFluidSpriteSet createSpriteSet(String path) {
        return new PortalFluidSpriteSet(PortalFluidRoot.res(path));
    }

    public static void populateSpriteSetArrays(TextureAtlas textureAtlas) {
        for (PortalFluidSpriteSet set : SETS) {
            set.sprites[0] = textureAtlas.getSprite(set.still);
            set.sprites[1] = textureAtlas.getSprite(FLOWING);
            set.sprites[2] = textureAtlas.getSprite(OVERLAY);
        }
    }

}
