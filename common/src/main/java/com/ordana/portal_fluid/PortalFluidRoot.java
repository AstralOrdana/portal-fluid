package com.ordana.portal_fluid;

import com.ordana.portal_fluid.configs.ClientConfigs;
import com.ordana.portal_fluid.configs.CommonConfigs;
import com.ordana.portal_fluid.reg.*;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PortalFluidRoot {

    public static final String MOD_ID = "portal_fluid";
    public static final Logger LOGGER = LogManager.getLogger();
    private static boolean initiated = false;

    public static ResourceLocation res(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static void commonInit() {
        if (initiated) {
            return;
        }
        initiated = true;

        CommonConfigs.init();

        if(PlatHelper.getPhysicalSide().isClient()) ClientConfigs.init();

        ModLootOverrides.INSTANCE.register();
        ModBlocks.init();
        ModFluids.init();
        ModItems.init();
        ModParticles.init();
        ModSoundEvents.init();
        ModCreativeTabs.init();
        ModWorldgenFeatures.init();
        RegHelper.addLootTableInjects(ModLootInjects::onLootInject);
    }

    public static boolean isInitiated() {
        return initiated;
    }

}