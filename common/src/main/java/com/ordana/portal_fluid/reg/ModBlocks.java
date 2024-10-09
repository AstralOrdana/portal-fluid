package com.ordana.portal_fluid.reg;

import com.ordana.portal_fluid.PortalFluidPlatform;
import com.ordana.portal_fluid.PortalFluidRoot;
import com.ordana.portal_fluid.blocks.PortalFluidCauldronBlock;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public class ModBlocks {
    public static void init() {
    }

    public static <T extends Block> Supplier<T> regBlock(String name, Supplier<T> block) {
        return RegHelper.registerBlock(PortalFluidRoot.res(name), block);
    }

    public static final Supplier<LiquidBlock> PORTAL_FLUID = regBlock("portal_fluid", () ->
            PortalFluidPlatform.doPortalFluid(ModFluids.PORTAL_FLUID, BlockBehaviour.Properties.copy(Blocks.WATER).noCollission().strength(100f).noLootTable().lightLevel((blockStatex) -> 5)));

    public static final Supplier<Block> PORTAL_CAULDRON = regBlock("portal_cauldron", () ->
            new PortalFluidCauldronBlock(BlockBehaviour.Properties.copy(Blocks.CAULDRON).lightLevel((blockStatex) -> 5), CauldronInteraction.WATER));

}
