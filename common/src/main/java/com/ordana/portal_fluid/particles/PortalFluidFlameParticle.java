package com.ordana.portal_fluid.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class PortalFluidFlameParticle extends CylindricalBillboardParticle {

    private static final int MAX_AGE_TICKS = 10;
    private final SpriteSet spriteSet;

    PortalFluidFlameParticle(ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, velocityX, velocityY, velocityZ);
        this.spriteSet = spriteSet;
        this.lifetime = MAX_AGE_TICKS;
        this.scale(2F);
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public int getLightColor(float partialTick) {
        return LightTexture.FULL_BLOCK;
    }

    @Override
    @NotNull
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.setSpriteFromAge(this.spriteSet);

        if (this.age++ >= this.lifetime)
            this.remove();
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new PortalFluidFlameParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites);
        }

    }

}
