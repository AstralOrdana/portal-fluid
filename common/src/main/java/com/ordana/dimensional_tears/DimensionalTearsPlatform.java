package com.ordana.dimensional_tears;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.Contract;

@SuppressWarnings("unused")
public class DimensionalTearsPlatform {

    @Contract
    @ExpectPlatform
    public static void addFeatureToBiome(GenerationStep.Decoration step, TagKey<Biome> tagKey, ResourceKey<PlacedFeature> feature) {
        throw new AssertionError();
    }

    @Contract
    @ExpectPlatform
    public static void addCarverToBiome(GenerationStep.Carving step, TagKey<Biome> tagKey, ResourceKey<ConfiguredWorldCarver<?>> feature) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isEyeInDimTears(Entity entity) {
        throw new AssertionError();
    }

    @Contract
    @ExpectPlatform
    public static double getDimTearsHeight(Entity entity) {
        throw new AssertionError();
    }

    public static boolean isInDimTears(Entity entity) {
        return getDimTearsHeight(entity) > 0;
    }

    public static void addAlias(Registry<?> registry, String path) {
        addAlias(registry, ResourceLocation.fromNamespaceAndPath("portal_fluid", path.replace("dimensional_tears", "portal_fluid")), ResourceLocation.fromNamespaceAndPath("dimensional_tears", path));
    }

	public static void addAlias(Registry<?> registry, String oldPath, String newPath) {
        addAlias(registry, ResourceLocation.fromNamespaceAndPath("portal_fluid", oldPath), ResourceLocation.fromNamespaceAndPath("dimensional_tears", newPath));
	}

    @ExpectPlatform
    public static void addAlias(Registry<?> registry, ResourceLocation oldPath, ResourceLocation newPath) {
        throw new AssertionError();
    }
}
