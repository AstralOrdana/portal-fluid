package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.DimensionalTearsPlatform;
import com.ordana.dimensional_tears.DimensionalTearsRoot;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

public interface ModParticles {

    SimpleParticleType RIFT_FLAME = registerParticle("rift_flame");

    static SimpleParticleType registerParticle(String path) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, DimensionalTearsRoot.res(path), DimensionalTearsPlatform.simpleParticle());
    }

    static void init() {}

}
