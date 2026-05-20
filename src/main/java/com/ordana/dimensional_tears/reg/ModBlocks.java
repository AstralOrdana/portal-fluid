package com.ordana.dimensional_tears.reg;

import com.mojang.datafixers.types.Func;
import com.ordana.dimensional_tears.DimensionalTearsPlatform;
import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.blocks.DimensionalTearsBlock;
import com.ordana.dimensional_tears.blocks.DimensionalTearsCauldronBlock;
import com.ordana.dimensional_tears.fluids.DimensionalTearsFluid;
import net.minecraft.core.Registry;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;
import java.util.function.Supplier;

public interface ModBlocks {

    RegSupplier<LiquidBlock> DIMENSIONAL_TEARS = regBlock(
        "dimensional_tears",
        (p) -> new DimensionalTearsBlock(
            ModFluids.DIMENSIONAL_TEARS, p),
            BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)
                .noCollision()
                .strength(100f)
                .noLootTable()
                .lightLevel(blockState -> DimensionalTearsFluid.LUMINANCE)
    );

    RegSupplier<DimensionalTearsCauldronBlock> DIMENSIONAL_TEARS_CAULDRON = regBlock(
        "dimensional_tears_cauldron",
            (p)->new DimensionalTearsCauldronBlock(p, CauldronInteractions.EMPTY),
            BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON).lightLevel(blockStatex -> DimensionalTearsFluid.LUMINANCE)
    );

    static <T extends Block> RegSupplier<T> regBlock(String path, Function<BlockBehaviour.Properties, T> blockSupplier, BlockBehaviour.Properties properties) {
        Identifier res = DimensionalTearsRoot.res(path);
        return new RegSupplier<>(res, Registry.register(BuiltInRegistries.BLOCK, res, blockSupplier.apply(properties.setId(keyOfBlock(res)))));
    }

    private static ResourceKey<Block> keyOfBlock(Identifier name) {
        return ResourceKey.create(Registries.BLOCK, name);
    }

    static void init() {
        DimensionalTearsPlatform.addAlias(BuiltInRegistries.BLOCK, "portal_fluid", "dimensional_tears");
        DimensionalTearsPlatform.addAlias(BuiltInRegistries.BLOCK, "portal_cauldron", "dimensional_tears_cauldron");
    }

}
