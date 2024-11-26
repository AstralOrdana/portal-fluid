package com.ordana.portal_fluid.reg;

import com.ordana.portal_fluid.PortalFluidPlatform;
import com.ordana.portal_fluid.PortalFluidRoot;
import com.ordana.portal_fluid.worldgen_features.BlockStripeFeature;
import com.ordana.portal_fluid.worldgen_features.BlockStripeFeatureConfig;
import com.ordana.portal_fluid.worldgen_features.PortalFluidOceanConfig;
import com.ordana.portal_fluid.worldgen_features.PortalFluidOceanFeature;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.function.Supplier;

public class ModWorldgenFeatures {


    public static final Supplier<Feature<BlockStripeFeatureConfig>> BLOCK_STRIPE_FEATURE = RegHelper.registerFeature(
            PortalFluidRoot.res("block_stripe"), () ->
                    new BlockStripeFeature(BlockStripeFeatureConfig.CODEC));

    public static final Supplier<Feature<PortalFluidOceanConfig>> PORTAL_FLUID_OCEAN_FEATURE = RegHelper.registerFeature(
            PortalFluidRoot.res("portal_fluid_ocean"), () ->
                    new PortalFluidOceanFeature(PortalFluidOceanConfig.CODEC));

    public static void init() {

        //carver generation
        ResourceKey<ConfiguredWorldCarver<?>> end_cave = ResourceKey.create(Registries.CONFIGURED_CARVER, PortalFluidRoot.res("end_cave"));
        PortalFluidPlatform.addCarverToBiome(GenerationStep.Carving.AIR, ModTags.HAS_END_NOISE, end_cave);

        ResourceKey<ConfiguredWorldCarver<?>> end_cave_extra = ResourceKey.create(Registries.CONFIGURED_CARVER, PortalFluidRoot.res("end_cave_extra"));
        PortalFluidPlatform.addCarverToBiome(GenerationStep.Carving.AIR, ModTags.HAS_END_NOISE, end_cave_extra);

        ResourceKey<ConfiguredWorldCarver<?>> end_canyon = ResourceKey.create(Registries.CONFIGURED_CARVER, PortalFluidRoot.res("end_canyon"));
        PortalFluidPlatform.addCarverToBiome(GenerationStep.Carving.AIR, ModTags.HAS_END_NOISE, end_canyon);

        ResourceKey<PlacedFeature> portal_fluid_ocean = ResourceKey.create(Registries.PLACED_FEATURE, PortalFluidRoot.res("portal_fluid_ocean"));
        PortalFluidPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, BiomeTags.IS_END, portal_fluid_ocean);

        ResourceKey<PlacedFeature> noise_end = ResourceKey.create(Registries.PLACED_FEATURE, PortalFluidRoot.res("noise_end"));
        PortalFluidPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_END_NOISE, noise_end);

        ResourceKey<PlacedFeature> obsidian_patch = ResourceKey.create(Registries.PLACED_FEATURE, PortalFluidRoot.res("obsidian_patch"));
        PortalFluidPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_END_NOISE, obsidian_patch);

        ResourceKey<PlacedFeature> portal_fluid_pool = ResourceKey.create(Registries.PLACED_FEATURE, PortalFluidRoot.res("portal_fluid_pool"));
        PortalFluidPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_END_NOISE, portal_fluid_pool);

        ResourceKey<PlacedFeature> portal_fluid_spring = ResourceKey.create(Registries.PLACED_FEATURE, PortalFluidRoot.res("portal_fluid_spring"));
        PortalFluidPlatform.addFeatureToBiome(GenerationStep.Decoration.RAW_GENERATION, ModTags.HAS_END_NOISE, portal_fluid_spring);
    }
}
