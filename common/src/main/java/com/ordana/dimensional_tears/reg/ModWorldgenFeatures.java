package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.PortalFluidPlatform;
import com.ordana.dimensional_tears.PortalFluidRoot;
import com.ordana.dimensional_tears.worldgen_features.BlockStripeFeature;
import com.ordana.dimensional_tears.worldgen_features.BlockStripeFeatureConfig;
import com.ordana.dimensional_tears.worldgen_features.PortalFluidOceanConfig;
import com.ordana.dimensional_tears.worldgen_features.PortalFluidOceanFeature;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.function.Supplier;

public class ModWorldgenFeatures {


    public static final RegSupplier<Feature<BlockStripeFeatureConfig>> BLOCK_STRIPE_FEATURE = registerFeature(
        "block_stripe",
        () -> new BlockStripeFeature(BlockStripeFeatureConfig.CODEC)
    );

    public static final RegSupplier<Feature<PortalFluidOceanConfig>> DIMENSIONAL_TEARS_OCEAN_FEATURE = registerFeature(
        "dimensional_tears_ocean",
        () -> new PortalFluidOceanFeature(PortalFluidOceanConfig.CODEC)
    );

    private static <FC extends FeatureConfiguration, T extends Feature<FC>> RegSupplier<T> registerFeature(String path, Supplier<T> supplier) {
        return RegHelper.registerFeature(PortalFluidRoot.res(path), supplier);
    }

    public static void init() {

        //carver generation
        ResourceKey<ConfiguredWorldCarver<?>> end_cave = ResourceKey.create(Registries.CONFIGURED_CARVER, PortalFluidRoot.res("end_cave"));
        PortalFluidPlatform.addCarverToBiome(GenerationStep.Carving.AIR, ModTags.HAS_END_NOISE, end_cave);

        ResourceKey<ConfiguredWorldCarver<?>> end_cave_extra = ResourceKey.create(Registries.CONFIGURED_CARVER, PortalFluidRoot.res("end_cave_extra"));
        PortalFluidPlatform.addCarverToBiome(GenerationStep.Carving.AIR, ModTags.HAS_END_NOISE, end_cave_extra);

        ResourceKey<ConfiguredWorldCarver<?>> end_canyon = ResourceKey.create(Registries.CONFIGURED_CARVER, PortalFluidRoot.res("end_canyon"));
        PortalFluidPlatform.addCarverToBiome(GenerationStep.Carving.AIR, ModTags.HAS_END_NOISE, end_canyon);

        ResourceKey<PlacedFeature> dimensional_tears_ocean = ResourceKey.create(Registries.PLACED_FEATURE, PortalFluidRoot.res("dimensional_tears_ocean"));
        PortalFluidPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, BiomeTags.IS_END, dimensional_tears_ocean);

        ResourceKey<PlacedFeature> noise_end = ResourceKey.create(Registries.PLACED_FEATURE, PortalFluidRoot.res("noise_end"));
        PortalFluidPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_END_NOISE, noise_end);

        ResourceKey<PlacedFeature> obsidian_patch = ResourceKey.create(Registries.PLACED_FEATURE, PortalFluidRoot.res("obsidian_patch"));
        PortalFluidPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_END_NOISE, obsidian_patch);

        ResourceKey<PlacedFeature> dimensional_tears_pool = ResourceKey.create(Registries.PLACED_FEATURE, PortalFluidRoot.res("dimensional_tears_pool"));
        PortalFluidPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_END_NOISE, dimensional_tears_pool);

        ResourceKey<PlacedFeature> dimensional_tears_spring = ResourceKey.create(Registries.PLACED_FEATURE, PortalFluidRoot.res("dimensional_tears_spring"));
        PortalFluidPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_END_NOISE, dimensional_tears_spring);
    }
}
