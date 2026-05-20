package com.ordana.dimensional_tears;

import com.ordana.dimensional_tears.particles.RiftFlameParticle;
//? fabric
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FlowingFluid;

public class ClientHelper {
    public static void registerFluidRenderType(FlowingFluid flowingFluid, ChunkSectionLayer chunkSectionLayer) {
        //fixme

    }

    public interface ParticleEvent {
        <T extends ParticleOptions> void register(SimpleParticleType particle, ParticleProvider<SimpleParticleType> aNew);
    }

    public record NetworkContext(Player player) {
        //? fabric {
        public static NetworkContext create(ClientPlayNetworking.Context context) {
            return new NetworkContext(context.player());
        }
        //?}
    }
}
