package com.ordana.portal_fluid;

import com.ordana.portal_fluid.particles.PortalFluidFlameParticle;
import com.ordana.portal_fluid.reg.ModBlocks;
import com.ordana.portal_fluid.reg.ModFluids;
import com.ordana.portal_fluid.reg.ModParticles;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.minecraft.client.renderer.RenderType;

public class PortalFluidClient {
    private static boolean finishedSetup = false;

    public static void init() {
        ClientHelper.addClientSetup(PortalFluidClient::setup);
        ClientHelper.addParticleRegistration(PortalFluidClient::registerParticles);
    }

    public static void setup() {
        ClientHelper.registerFluidRenderType(ModFluids.FLOWING_PORTAL_FLUID.get(), RenderType.translucent());
        ClientHelper.registerFluidRenderType(ModFluids.PORTAL_FLUID.get(), RenderType.translucent());

        ClientHelper.registerRenderType(ModBlocks.PORTAL_FLUID.get(), RenderType.translucent());

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