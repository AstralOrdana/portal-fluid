package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.DimensionalTearsPlatform;
import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.worldgen_features.BlockStripeFeature;
import com.ordana.dimensional_tears.worldgen_features.BlockStripeFeatureConfig;
import com.ordana.dimensional_tears.worldgen_features.DimensionalTearsOceanConfig;
import com.ordana.dimensional_tears.worldgen_features.DimensionalTearsOceanFeature;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.function.Supplier;

public interface ModWorldgenFeatures {

    RegSupplier<Feature<BlockStripeFeatureConfig>> BLOCK_STRIPE_FEATURE = registerFeature(
        "block_stripe",
        () -> new BlockStripeFeature(BlockStripeFeatureConfig.CODEC)
    );

    RegSupplier<Feature<DimensionalTearsOceanConfig>> DIMENSIONAL_TEARS_OCEAN_FEATURE = registerFeature(
        "dimensional_tears_ocean",
        () -> new DimensionalTearsOceanFeature(DimensionalTearsOceanConfig.CODEC)
    );

    private static <FC extends FeatureConfiguration, T extends Feature<FC>> RegSupplier<T> registerFeature(String path, Supplier<T> supplier) {
        Identifier res = DimensionalTearsRoot.res(path);
        return new RegSupplier<>(res, Registry.register(BuiltInRegistries.FEATURE, res, supplier.get()));
    }

    static void bootstrap() {
        carver(ModTags.HAS_END_NOISE, "end_cave");
        carver(ModTags.HAS_END_NOISE, "end_cave_extra");
        carver(ModTags.HAS_END_NOISE, "end_canyon");

        placedFeature(BiomeTags.IS_END, "dimensional_tears_ocean");
        placedFeature(ModTags.HAS_END_NOISE, "noise_end");
        placedFeature(ModTags.HAS_END_NOISE, "obsidian_patch");
        placedFeature(ModTags.HAS_END_NOISE, "dimensional_tears_pool");
        placedFeature(ModTags.HAS_END_NOISE, "dimensional_tears_spring");
    }

    private static void placedFeature(TagKey<Biome> biomeTag, String path) {
        DimensionalTearsPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, biomeTag, createPlacedFeatureKey(path));
    }

    private static void carver(TagKey<Biome> biomeTag, String path) {
        DimensionalTearsPlatform.addCarverToBiome(biomeTag, createCarverKey(path));
    }

    private static ResourceKey<ConfiguredWorldCarver<?>> createCarverKey(String path) {
        return ResourceKey.create(Registries.CONFIGURED_CARVER, DimensionalTearsRoot.res(path));
    }

    private static ResourceKey<PlacedFeature> createPlacedFeatureKey(String path) {
        return ResourceKey.create(Registries.PLACED_FEATURE, DimensionalTearsRoot.res(path));
    }

}
