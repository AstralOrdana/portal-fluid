package com.ordana.dimensional_tears.neoforge;

import com.ordana.dimensional_tears.ClientHelper;
import com.ordana.dimensional_tears.DimensionalTearsClient;
import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.fluids.DimensionalTearsFluidRenderer;
import com.ordana.dimensional_tears.neoforge.reg.ModFluidTypes;
import com.ordana.dimensional_tears.reg.ModFluids;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;

@EventBusSubscriber(value = Dist.CLIENT)
public class DimensionalTearsRootNeoForgeClient {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        DimensionalTearsClient.init();
    }

    @SubscribeEvent
    public static void registerModels(RegisterFluidModelsEvent event) {
        event.register(DimensionalTearsFluidRenderer.model(), ModFluids.DIMENSIONAL_TEARS.get(), ModFluids.FLOWING_DIMENSIONAL_TEARS.get());
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerClientFluid(RegisterClientExtensionsEvent event) {
        event.registerFluidType(new DimensionalTearsFluidRenderer(), ModFluidTypes.DIMENSIONAL_TEARS_TYPE.get());
    }
}
