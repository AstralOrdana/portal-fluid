package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.DimensionalTearsRoot;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.particles.SimpleParticleType;

public interface ModParticles {

    RegSupplier<SimpleParticleType> RIFT_FLAME = registerParticle("rift_flame");

    static RegSupplier<SimpleParticleType> registerParticle(String path) {
        return RegHelper.registerParticle(DimensionalTearsRoot.res(path));
    }

    static void init() {}

}
