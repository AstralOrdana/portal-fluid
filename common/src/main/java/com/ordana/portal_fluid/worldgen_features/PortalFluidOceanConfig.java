package com.ordana.portal_fluid.worldgen_features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class PortalFluidOceanConfig implements FeatureConfiguration {

    public static final Codec<PortalFluidOceanConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.intRange(0, 128).fieldOf("ocean_floor_elevation").orElse(0).forGetter((config) -> config.floorElevation),
            Codec.intRange(0, 128).fieldOf("ocean_surface_elevation").orElse(4).forGetter((config) -> config.surfaceElevation))

            .apply(instance, PortalFluidOceanConfig::new));

    public final int floorElevation;
    public final int surfaceElevation;

    public PortalFluidOceanConfig(
            int floorElevation,
            int surfaceElevation) {
        this.floorElevation = floorElevation;
        this.surfaceElevation = surfaceElevation;
    }

}
