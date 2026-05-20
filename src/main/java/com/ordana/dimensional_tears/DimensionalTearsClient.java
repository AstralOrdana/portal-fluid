package com.ordana.dimensional_tears;

import com.ordana.dimensional_tears.configs.ClientConfigs;
import com.ordana.dimensional_tears.reg.ModFluids;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

import static com.ordana.dimensional_tears.DimensionalTearsRoot.MOD_ID;

public class DimensionalTearsClient {
    public static ClientConfigs CONFIG = ClientConfigs.createToml(DimensionalTearsPlatform.getConfigDirectory(), MOD_ID, "client", ClientConfigs.class);

    public static void init() {
        // the textures are opaque but need to be registered as translucent for the boat's "water patch" to work
        ClientHelper.registerFluidRenderType(ModFluids.FLOWING_DIMENSIONAL_TEARS.get(), ChunkSectionLayer.TRANSLUCENT);
        ClientHelper.registerFluidRenderType(ModFluids.DIMENSIONAL_TEARS.get(), ChunkSectionLayer.TRANSLUCENT);
    }

}