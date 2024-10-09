package com.ordana.portal_fluid.reg;

import com.ordana.portal_fluid.PortalFluidRoot;
import com.ordana.portal_fluid.fluids.PortalFluidFluid;
import net.mehvahdjukaar.moonlight.api.fluids.ModFlowingFluid;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Supplier;

public class ModFluids extends Fluids {
    public static void  init() {
    }

    public static final Supplier<FlowingFluid> FLOWING_PORTAL_FLUID = RegHelper.registerFluid(PortalFluidRoot.res("flowing_portal_fluid"), () ->
            new PortalFluidFluid.Flowing(ModFlowingFluid.properties().supportsBoating(true).lightLevel(5), ModBlocks.PORTAL_FLUID));
    public static final Supplier<FlowingFluid> PORTAL_FLUID = RegHelper.registerFluid(PortalFluidRoot.res("portal_fluid"), () ->
            new PortalFluidFluid.Source(ModFlowingFluid.properties().supportsBoating(true).lightLevel(5), ModBlocks.PORTAL_FLUID));

}
