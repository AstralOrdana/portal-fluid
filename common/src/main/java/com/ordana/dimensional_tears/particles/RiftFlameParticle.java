package com.ordana.dimensional_tears.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class RiftFlameParticle extends TextureSheetParticle {

    private static final int MAX_AGE_TICKS = 10;
    private final SpriteSet spriteSet;

    RiftFlameParticle(ClientLevel clientLevel, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, velocityX, velocityY, velocityZ);
        this.spriteSet = spriteSet;
        this.lifetime = MAX_AGE_TICKS;
        this.scale(2.0F);
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public int getLightColor(float partialTick) {
        float g = (this.age + partialTick) / this.lifetime;
        g = Mth.clamp(g, 0.0F, 1.0F);
        int i = super.getLightColor(partialTick);
        int j = i & 0xFF;
        int k = i >> 16 & 0xFF;
        j += (int) (g * 15.0F * 16.0F);
        if (j > 240)
            j = 240;

        return j | k << 16;
    }

    @Override
    @NotNull
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    @NotNull
    public FacingCameraMode getFacingCameraMode() {
        return FacingCameraMode.LOOKAT_Y;
    }

    @Override
    public void tick() {
        this.setSpriteFromAge(this.spriteSet);

        if (this.age++ >= this.lifetime)
            this.remove();
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new RiftFlameParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites);
        }

    }

}
