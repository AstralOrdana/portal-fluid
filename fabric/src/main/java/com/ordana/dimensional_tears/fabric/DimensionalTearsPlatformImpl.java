package com.ordana.dimensional_tears.fabric;

import com.ordana.dimensional_tears.reg.ModTags;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

@SuppressWarnings("unused")
public class DimensionalTearsPlatformImpl {

    public static void addFeatureToBiome(GenerationStep.Decoration step, TagKey<Biome> tagKey, ResourceKey<PlacedFeature> feature) {
        BiomeModifications.addFeature(BiomeSelectors.tag(tagKey), step, feature);
    }

    public static void addCarverToBiome(GenerationStep.Carving step, TagKey<Biome> tagKey, ResourceKey<ConfiguredWorldCarver<?>> carver) {
        BiomeModifications.addCarver(BiomeSelectors.tag(tagKey), step, carver);
    }

    public static double getDimTearsHeight(Entity entity) {
        return entity.getFluidHeight(ModTags.DIMENSIONAL_TEARS);
    }

    public static boolean isEyeInDimTears(Entity entity) {
        return entity.isEyeInFluid(ModTags.DIMENSIONAL_TEARS);
    }

    public static void addAlias(Registry<?> registry, ResourceLocation oldPath, ResourceLocation newPath) {
        registry.addAlias(oldPath, newPath);
    }

}
