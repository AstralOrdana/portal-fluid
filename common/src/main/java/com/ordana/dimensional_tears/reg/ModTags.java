package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.DimensionalTearsRoot;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public interface ModTags {

    TagKey<Block> BASE_STONE_END = registerTag(Registries.BLOCK, "base_stone_end");
    TagKey<Biome> HAS_END_NOISE = registerTag(Registries.BIOME, "has_end_noise");
    TagKey<EntityType<?>> DIMENSIONAL_TEARS_IMMUNE = registerTag(Registries.ENTITY_TYPE, "dimensional_tears_immune");
    TagKey<Fluid> DIMENSIONAL_TEARS = registerTag(Registries.FLUID, "dimensional_tears");

    private static <T> TagKey<T> registerTag(ResourceKey<Registry<T>> registry, String path) {
        return TagKey.create(registry, DimensionalTearsRoot.res(path));
    }

}
