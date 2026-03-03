package com.ordana.portal_fluid.reg;

import com.ordana.portal_fluid.PortalFluidRoot;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class ModTags {

    public static final TagKey<Block> BASE_STONE_END = registerTag(Registries.BLOCK, "base_stone_end");
    public static final TagKey<Biome> HAS_END_NOISE = registerTag(Registries.BIOME, "has_end_noise");
    public static final TagKey<EntityType<?>> PORTAL_FLUID_IMMUNE = registerTag(Registries.ENTITY_TYPE, "portal_fluid_immune");
    public static final TagKey<Fluid> PORTAL_FLUID = registerTag(Registries.FLUID, "portal_fluid");

    private static <T> TagKey<T> registerTag(ResourceKey<Registry<T>> registry, String path) {
        return TagKey.create(registry, PortalFluidRoot.res(path));
    }

}
