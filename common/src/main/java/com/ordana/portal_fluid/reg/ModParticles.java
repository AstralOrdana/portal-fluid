package com.ordana.portal_fluid.reg;

import com.ordana.portal_fluid.PortalFluidRoot;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.function.Supplier;

public class ModParticles {
    public static void init() {
    }

    public static Supplier<SimpleParticleType> registerParticle(String name) {
        return RegHelper.registerParticle(PortalFluidRoot.res(name));
    }

    public static final Supplier<SimpleParticleType> PORTAL_FLAME = registerParticle("portal_fluid_flame");

}
