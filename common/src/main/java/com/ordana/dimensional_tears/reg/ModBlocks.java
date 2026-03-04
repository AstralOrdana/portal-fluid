package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.PortalFluidPlatform;
import com.ordana.dimensional_tears.PortalFluidRoot;
import com.ordana.dimensional_tears.blocks.PortalFluidCauldronBlock;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public class ModBlocks {

    public static final Supplier<LiquidBlock> DIMENSIONAL_TEARS = regBlock(
        "dimensional_tears",
        () -> PortalFluidPlatform.doPortalFluid(
            ModFluids.DIMENSIONAL_TEARS,
            BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)
                .noCollission()
                .strength(100f)
                .noLootTable()
                .lightLevel(blockState -> 5)
        )
    );

    public static final Supplier<PortalFluidCauldronBlock> PORTAL_CAULDRON = regBlock(
        "portal_cauldron",
        () -> new PortalFluidCauldronBlock(
            BlockBehaviour.Properties.ofFullCopy(Blocks.CAULDRON).lightLevel(blockStatex -> 5),
            CauldronInteraction.WATER
        )
    );

    public static <T extends Block> Supplier<T> regBlock(String name, Supplier<T> block) {
        return RegHelper.registerBlock(PortalFluidRoot.res(name), block);
    }

    public static void init() {}

}
