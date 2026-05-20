package com.ordana.dimensional_tears.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public class RiftFlameParticle extends SingleQuadParticle {

    private static final int MAX_LIFETIME = 10;
    private final SpriteSet spriteSet;

    RiftFlameParticle(ClientLevel clientLevel, double x, double y, double z, SpriteSet spriteSet) {
        super(clientLevel, x, y, z, spriteSet.first());
        this.spriteSet = spriteSet;
        this.lifetime = MAX_LIFETIME;

        this.scale(2.0F);
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public int getLightCoords(float partialTick) {
        float g = (this.age + partialTick) / this.lifetime;
        g = Mth.clamp(g, 0.0F, 1.0F);
        int i = super.getLightCoords(partialTick);
        int j = i & 0xFF;
        int k = i >> 16 & 0xFF;
        j += (int) (g * 15.0F * 16.0F);
        if (j > 240)
            j = 240;

        return j | k << 16;
    }

    @Override
    @NotNull
    public FacingCameraMode getFacingCameraMode() {
        return FacingCameraMode.LOOKAT_Y;
    }

    @Override
    protected Layer getLayer() {
        return Layer.OPAQUE;
    }

    @Override
    public void tick() {
        this.setSpriteFromAge(this.spriteSet);

        if (this.age++ >= this.lifetime)
            this.remove();
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
        public @Nullable Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
            return new RiftFlameParticle(level, x, y, z, this.sprites);
        }
    }

}
