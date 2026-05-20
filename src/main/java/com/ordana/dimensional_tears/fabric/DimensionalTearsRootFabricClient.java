
//? fabric {
package com.ordana.dimensional_tears.fabric;

import com.ordana.dimensional_tears.ClientHelper;
import com.ordana.dimensional_tears.DimensionalTearsClient;
import com.ordana.dimensional_tears.fluids.DimensionalTearsFluidRenderer;
import com.ordana.dimensional_tears.networking.RiftingParticleS2CMessage;
import com.ordana.dimensional_tears.particles.RiftFlameParticle;
import com.ordana.dimensional_tears.reg.ModFluids;
import com.ordana.dimensional_tears.reg.ModParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderingRegistry;

public class DimensionalTearsRootFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        DimensionalTearsClient.init();
        ParticleProviderRegistry.getInstance().register(ModParticles.RIFT_FLAME, RiftFlameParticle.Provider::new);
        ClientLifecycleEvents.CLIENT_STARTED.register(minecraft ->
            FluidRenderingRegistry.register(ModFluids.DIMENSIONAL_TEARS.get(), ModFluids.FLOWING_DIMENSIONAL_TEARS.get(), DimensionalTearsFluidRenderer.model(), new DimensionalTearsFluidRenderer())
        );

        ClientPlayNetworking.registerGlobalReceiver(RiftingParticleS2CMessage.TYPE.type(), (payload, context) -> {
            payload.handle(ClientHelper.NetworkContext.create(context));
        });
    }

}
//?}