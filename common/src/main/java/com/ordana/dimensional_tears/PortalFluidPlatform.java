package com.ordana.dimensional_tears;

import com.ordana.dimensional_tears.fluids.PortalFluid;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.Contract;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class PortalFluidPlatform {
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
    public static <T extends PortalFluid> LiquidBlock doPortalFluid(Supplier<T> flowingFluid, BlockBehaviour.Properties properties) {
        throw new AssertionError();
    }

}
