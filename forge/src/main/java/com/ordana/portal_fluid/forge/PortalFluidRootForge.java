package com.ordana.portal_fluid.forge;

import com.ordana.portal_fluid.PortalFluidClient;
import com.ordana.portal_fluid.PortalFluidRoot;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraftforge.fml.common.Mod;

@Mod(PortalFluidRoot.MOD_ID)
public class PortalFluidRootForge {
    public static final String MOD_ID = PortalFluidRoot.MOD_ID;

    public PortalFluidRootForge() {
        PortalFluidRoot.commonInit();

        if (PlatHelper.getPhysicalSide().isClient()) {
            PortalFluidClient.init();
        }
    }
}

