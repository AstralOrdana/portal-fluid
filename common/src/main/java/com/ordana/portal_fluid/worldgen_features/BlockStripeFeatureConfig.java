package com.ordana.portal_fluid.worldgen_features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;

public record BlockStripeFeatureConfig(
    HolderSet<Block> firstTarget,
    List<StoneEntry> firstTargetPlacer,
    boolean useSecondTarget,
    HolderSet<Block> secondTarget,
    List<StoneEntry> secondTargetPlacer,
    boolean useBiomeFilter,
    HolderSet<Biome> biomes,
    float blankPatchChance,
    boolean useHeightFilter,
    int surfaceOffset,
    int bottomOffset
) implements FeatureConfiguration {

    public static final Codec<BlockStripeFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("first_target").forGetter(BlockStripeFeatureConfig::firstTarget),
            Codec.list(StoneEntry.CODEC).fieldOf("first_target_placer").forGetter(BlockStripeFeatureConfig::firstTargetPlacer),
            Codec.BOOL.fieldOf("use_second_target").orElse(false).forGetter(BlockStripeFeatureConfig::useSecondTarget),
            RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("second_target").orElse(null).forGetter(BlockStripeFeatureConfig::secondTarget),
            Codec.list(StoneEntry.CODEC).fieldOf("second_target_placer").orElse(null).forGetter(BlockStripeFeatureConfig::secondTargetPlacer),

            Codec.BOOL.fieldOf("use_biome_filter").orElse(false).forGetter(BlockStripeFeatureConfig::useBiomeFilter),
            RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes").orElse(null).forGetter(BlockStripeFeatureConfig::biomes),
            Codec.floatRange(0.0F, 1.0F).fieldOf("blank_patch_chance").orElse(0.0f).forGetter(BlockStripeFeatureConfig::blankPatchChance),

            Codec.BOOL.fieldOf("use_height_filter").orElse(false).forGetter(BlockStripeFeatureConfig::useHeightFilter),
            Codec.intRange(0, 64).fieldOf("surface_offset").orElse(0).forGetter(BlockStripeFeatureConfig::surfaceOffset),
            Codec.intRange(0, 64).fieldOf("bottom_offset").orElse(0).forGetter(BlockStripeFeatureConfig::bottomOffset)
        )
        .apply(instance, BlockStripeFeatureConfig::new)
    );

}
