package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.DimensionalTearsPlatform;
import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.blocks.DimensionalTearsCauldronBlock;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public interface ModBlocks {

    Supplier<LiquidBlock> DIMENSIONAL_TEARS = regBlock(
        "dimensional_tears",
        () -> DimensionalTearsPlatform.doPortalFluid(
            ModFluids.DIMENSIONAL_TEARS,
            BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)
                .noCollission()
                .strength(100f)
                .noLootTable()
                .lightLevel(blockState -> 5)
        )
    );

    Supplier<DimensionalTearsCauldronBlock> DIMENSIONAL_TEARS_CAULDRON = regBlock(
        "dimensional_tears_cauldron",
        () -> new DimensionalTearsCauldronBlock(
            BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON).lightLevel(blockStatex -> 5),
            CauldronInteraction.WATER
        )
    );

    static <T extends Block> Supplier<T> regBlock(String path, Supplier<T> blockSupplier) {
        return RegHelper.registerBlock(DimensionalTearsRoot.res(path), blockSupplier);
    }

    static void init() {}

}
