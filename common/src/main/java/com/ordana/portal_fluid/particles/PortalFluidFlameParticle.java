package com.ordana.portal_fluid.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.ordana.portal_fluid.reg.ModParticles;
import com.ordana.portal_fluid.reg.ModSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class PortalFluidFlameParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    protected boolean isGlowing;
    private final Quaternionf rotation = new Quaternionf(0F, 0F, 0F, 0F);

    PortalFluidFlameParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i, SpriteSet spriteSet) {
        super(clientLevel, d, e, f, g, h, i);
        this.sprites = spriteSet;
        this.scale(2F);
        this.lifetime = 10;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        Vec3 vec3 = renderInfo.getPosition();
        float f = (float) (Mth.lerp(partialTicks, this.xo, this.x) - vec3.x());
        float g = (float) (Mth.lerp(partialTicks, this.yo, this.y) - vec3.y());
        float h = (float) (Mth.lerp(partialTicks, this.zo, this.z) - vec3.z());
        this.rotation.set(0.0f, 0.0f, 0.0f, 1.0f);
        this.rotation.mul(Axis.YP.rotationDegrees(-renderInfo.getYRot()));
        //this.rotation.mul(Axis.XP.rotationDegrees(renderInfo.getXRot() * (Mth.lerp(partialTicks, this.prevXRotMultiplier, this.xRotMultiplier))));
        if (this.roll != 0.0f) {
            float i = Mth.lerp(partialTicks, this.oRoll, this.roll);
            this.rotation.mul(Axis.ZP.rotation(i));
        }
        Vector3f[] vector3fs = new Vector3f[]{new Vector3f(-1.0f, -1.0f, 0.0f), new Vector3f(-1.0f, 1.0f, 0.0f), new Vector3f(1.0f, 1.0f, 0.0f), new Vector3f(1.0f, -1.0f, 0.0f)};
        float j = this.getQuadSize(partialTicks);
        for (int k = 0; k < 4; ++k) {
            Vector3f vector3f2 = vector3fs[k];
            vector3f2.rotate(this.rotation);
            vector3f2.mul(j);
            vector3f2.add(f, g, h);
        }
        float l = this.getU0();
        float m = this.getU1();
        float n = this.getV0();
        float o = this.getV1();
        int p = this.getLightColor(partialTicks);
        buffer.addVertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z()).setUv(m, o).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(p);
        buffer.addVertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z()).setUv(m, n).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(p);
        buffer.addVertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z()).setUv(l, n).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(p);
        buffer.addVertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z()).setUv(l, o).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(p);
    }

    @Override
    public int getLightColor(float partialTick) {
        return this.isGlowing ? 240 : super.getLightColor(partialTick);
    }

    @Override
    @NotNull
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.setSpriteFromAge(this.sprites);
        if (this.age++ >= this.lifetime) {
            this.remove();
        }
    }

    public static void onAnimateTick(Level level, BlockPos blockPos, RandomSource randomSource) {
        BlockPos above = blockPos.above();

        if (!level.isEmptyBlock(above) || level.getBlockState(above).isSolidRender(level, above))
            return;

        if (randomSource.nextInt(20) == 0) {
            double x = above.getX() + randomSource.nextDouble();
            double y = above.getY() + 0.2;
            double z = above.getZ() + randomSource.nextDouble();

            level.addParticle(ModParticles.PORTAL_FLAME.get(), x, y, z, 0.0, 0.0, 0.0);
        }

        if (randomSource.nextInt(200) != 0)
            return;

        level.playLocalSound(
            blockPos,
            ModSoundEvents.PORTAL_FLUID_AMBIENT.get(),
            SoundSource.BLOCKS,
            Mth.randomBetween(randomSource, 0.2F, 0.4F),
            Mth.randomBetween(randomSource, 0.9F, 1.05F),
            false
        );
    }

    @Environment(EnvType.CLIENT)
    public record Provider(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new PortalFluidFlameParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }

    }
}
