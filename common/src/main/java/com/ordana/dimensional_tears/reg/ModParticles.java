package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.PortalFluidRoot;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.function.Supplier;

public class ModParticles {

    public static final Supplier<SimpleParticleType> PORTAL_FLAME = registerParticle("dimensional_tears_flame");

    public static Supplier<SimpleParticleType> registerParticle(String name) {
        return RegHelper.registerParticle(PortalFluidRoot.res(name));
    }

    public static void init() {}

}
