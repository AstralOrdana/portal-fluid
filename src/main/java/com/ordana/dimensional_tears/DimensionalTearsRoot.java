package com.ordana.dimensional_tears;

import com.ordana.dimensional_tears.configs.CommonConfigs;
import com.ordana.dimensional_tears.networking.RiftingParticleS2CMessage;
import com.ordana.dimensional_tears.reg.*;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DimensionalTearsRoot {

    public static final String MOD_ID = "dimensional_tears";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final CommonConfigs CONFIG = CommonConfigs.createToml(DimensionalTearsPlatform.getConfigDirectory(), MOD_ID, "common", CommonConfigs.class);

    private static boolean initiated = false;

    public static Identifier res(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static boolean isInitiated() {
        return initiated;
    }

    public static void setInitiated() {
        initiated = true;
    }

    public static void registerMessages(DimensionalTearsPlatform.RegisterMessagesEvent event) {
        event.registerClientBound(RiftingParticleS2CMessage.TYPE);
    }

}