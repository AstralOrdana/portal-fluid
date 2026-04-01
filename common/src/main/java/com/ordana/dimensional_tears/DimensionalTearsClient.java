package com.ordana.dimensional_tears;

import com.ordana.dimensional_tears.client.DimensionalBearRenderer;
import com.ordana.dimensional_tears.particles.RiftFlameParticle;
import com.ordana.dimensional_tears.reg.ModEntityTypes;
import com.ordana.dimensional_tears.reg.ModFluids;
import com.ordana.dimensional_tears.reg.ModParticles;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.minecraft.client.model.PolarBearModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.PolarBearRenderer;

import static com.ordana.dimensional_tears.DimensionalTearsRoot.res;

public class DimensionalTearsClient {
    public static final ModelLayerLocation DIMENSIONAL_BEAR = new ModelLayerLocation(DimensionalTearsRoot.res("bear/dimensionalbear"), "main");
    private static boolean finishedSetup = false;

    public static void init() {
        ClientHelper.addClientSetup(DimensionalTearsClient::setup);
        ClientHelper.addParticleRegistration(DimensionalTearsClient::registerParticles);
        ClientHelper.addEntityRenderersRegistration((entityRendererEvent -> {
            entityRendererEvent.register(ModEntityTypes.DIMENSIONAL_BEAR.get(), DimensionalBearRenderer::new);
        }));
        ClientHelper.addModelLayerRegistration((entityRendererEvent -> {
            entityRendererEvent.register(DIMENSIONAL_BEAR, PolarBearModel::createBodyLayer);
        }));
    }

    public static void setup() {
        // the textures are opaque but need to be registered as translucent for the boat's "water patch" to work
        ClientHelper.registerFluidRenderType(ModFluids.FLOWING_DIMENSIONAL_TEARS.get(), RenderType.translucent());
        ClientHelper.registerFluidRenderType(ModFluids.DIMENSIONAL_TEARS.get(), RenderType.translucent());

        finishedSetup = true;
    }

    public static void checkIfFailed() {
        if (!finishedSetup) {
            throw new RuntimeException("Failed to run client setup. This is likely due to the mod integration code being outdated, crashing with other mods new versions. Terminating");
        }
    }

    private static void registerParticles(ClientHelper.ParticleEvent event) {
        event.register(ModParticles.RIFT_FLAME.get(), RiftFlameParticle.Provider::new);
    }
}