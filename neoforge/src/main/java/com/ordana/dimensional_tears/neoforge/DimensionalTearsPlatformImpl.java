package com.ordana.dimensional_tears.neoforge;

import com.ordana.dimensional_tears.blocks.DimensionalTearsBlock;
import com.ordana.dimensional_tears.reg.ModFluids;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class DimensionalTearsPlatformImpl {

    public static void addFeatureToBiome(GenerationStep.Decoration step, TagKey<Biome> tagKey, ResourceKey<PlacedFeature> feature) {
    }

    public static void addCarverToBiome(GenerationStep.Carving step, TagKey<Biome> tagKey, ResourceKey<ConfiguredWorldCarver<?>> carver) {
    }

    public static LiquidBlock doPortalFluid(Supplier<FlowingFluid> flowingFluid, BlockBehaviour.Properties properties) {
        return new DimensionalTearsBlock(flowingFluid, properties, entity -> entity.isInFluidType(ModFluids.DIMENSIONAL_TEARS.get().getFluidType()));
    }

    public static boolean isEyeInDimTears(Entity entity) {
        return entity.isEyeInFluidType(ModFluids.DIMENSIONAL_TEARS.get().getFluidType());
    }

}
