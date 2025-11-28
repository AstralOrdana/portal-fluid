package com.ordana.portal_fluid.neoforge;

import com.ordana.portal_fluid.PortalFluidClient;
import com.ordana.portal_fluid.PortalFluidRoot;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.neoforged.fml.common.Mod;

@Mod(PortalFluidRoot.MOD_ID)
public class PortalFluidRootNeoForge {
    public static final String MOD_ID = PortalFluidRoot.MOD_ID;

    public PortalFluidRootNeoForge() {
        PortalFluidRoot.commonInit();

        if (PlatHelper.getPhysicalSide().isClient()) {
            PortalFluidClient.init();
        }
    }
}

