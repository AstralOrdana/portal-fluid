package com.ordana.portal_fluid.reg;

import com.ordana.portal_fluid.PortalFluidRoot;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class ModTags {

    public static final TagKey<Block> BASE_STONE_END = registerBlockTag("base_stone_end");
    public static final TagKey<Biome> HAS_END_NOISE = registerBiomeTag("has_end_noise");
    public static final TagKey<EntityType<?>> PORTAL_FLUID_IMMUNE = registerEntityTag("portal_fluid_immune");
    public static final TagKey<Fluid> PORTAL_FLUID = registerFluidTag("portal_fluid");

    private ModTags() {
    }

    private static TagKey<Block> registerBlockTag(String id) {
        return TagKey.create(Registries.BLOCK, PortalFluidRoot.res(id));
    }

    private static TagKey<Biome> registerBiomeTag(String id) {
        return TagKey.create(Registries.BIOME, PortalFluidRoot.res(id));
    }

    private static TagKey<EntityType<?>> registerEntityTag(String id) {
        return TagKey.create(Registries.ENTITY_TYPE, PortalFluidRoot.res(id));
    }

    private static TagKey<Fluid> registerFluidTag(String id) {
        return TagKey.create(Registries.FLUID, PortalFluidRoot.res(id));
    }
}
