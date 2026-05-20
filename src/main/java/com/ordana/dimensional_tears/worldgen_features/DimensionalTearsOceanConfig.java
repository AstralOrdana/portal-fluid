package com.ordana.dimensional_tears.worldgen_features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record DimensionalTearsOceanConfig(int floorElevation, int surfaceElevation) implements FeatureConfiguration {

    public static final Codec<DimensionalTearsOceanConfig> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            Codec.intRange(0, 128).fieldOf("ocean_floor_elevation").orElse(0).forGetter(DimensionalTearsOceanConfig::floorElevation),
            Codec.intRange(0, 128).fieldOf("ocean_surface_elevation").orElse(4).forGetter(DimensionalTearsOceanConfig::surfaceElevation)
        )
        .apply(instance, DimensionalTearsOceanConfig::new)
    );

}
