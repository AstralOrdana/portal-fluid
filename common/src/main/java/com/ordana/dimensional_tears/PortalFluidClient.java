package com.ordana.dimensional_tears;

import com.ordana.dimensional_tears.particles.PortalFluidFlameParticle;
import com.ordana.dimensional_tears.reg.ModBlocks;
import com.ordana.dimensional_tears.reg.ModFluids;
import com.ordana.dimensional_tears.reg.ModParticles;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.minecraft.client.renderer.RenderType;

public class PortalFluidClient {
    private static boolean finishedSetup = false;

    public static void init() {
        ClientHelper.addClientSetup(PortalFluidClient::setup);
        ClientHelper.addParticleRegistration(PortalFluidClient::registerParticles);
    }

    public static void setup() {
        ClientHelper.registerFluidRenderType(ModFluids.FLOWING_DIMENSIONAL_TEARS.get(), RenderType.translucent());
        ClientHelper.registerFluidRenderType(ModFluids.DIMENSIONAL_TEARS.get(), RenderType.translucent());

        ClientHelper.registerRenderType(ModBlocks.DIMENSIONAL_TEARS.get(), RenderType.translucent());

        finishedSetup = true;
    }

    public static void checkIfFailed() {
        if(!finishedSetup) {
            throw new RuntimeException("Failed to run client setup. This is likely due to the mod integration code being outdated, crashing with other mods new versions. Terminating");
        }
    }

    private static void registerParticles(ClientHelper.ParticleEvent event) {
        event.register(ModParticles.PORTAL_FLAME.get(), PortalFluidFlameParticle.Provider::new);
    }
}