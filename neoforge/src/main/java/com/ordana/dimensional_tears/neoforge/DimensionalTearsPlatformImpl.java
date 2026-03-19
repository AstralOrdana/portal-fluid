package com.ordana.dimensional_tears.neoforge;

import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.neoforge.reg.ModFluidTypes;
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
    }

    public static void addCarverToBiome(GenerationStep.Carving step, TagKey<Biome> tagKey, ResourceKey<ConfiguredWorldCarver<?>> carver) {
    }

    public static double getDimTearsHeight(Entity entity) {
        return entity.getFluidTypeHeight(ModFluidTypes.DIMENSIONAL_TEARS_TYPE.get());
    }

    public static boolean isEyeInDimTears(Entity entity) {
        return DimensionalTearsRoot.isInitiated() && entity.isEyeInFluidType(ModFluidTypes.DIMENSIONAL_TEARS_TYPE.get());
    }

    public static void addAlias(Registry<?> registry, ResourceLocation oldPath, ResourceLocation newPath) {
        registry.addAlias(oldPath, newPath);
    }

}
