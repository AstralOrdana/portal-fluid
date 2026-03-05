package com.ordana.dimensional_tears.neoforge;

import com.ordana.dimensional_tears.DimensionalTearsClient;
import com.ordana.dimensional_tears.DimensionalTearsRoot;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.neoforged.fml.common.Mod;

@Mod(DimensionalTearsRoot.MOD_ID)
public class DimensionalTearsRootNeoForge {

    public static final String MOD_ID = DimensionalTearsRoot.MOD_ID;

    public DimensionalTearsRootNeoForge() {
        DimensionalTearsRoot.commonInit();

        if (PlatHelper.getPhysicalSide().isClient())
            DimensionalTearsClient.init();
    }

}

