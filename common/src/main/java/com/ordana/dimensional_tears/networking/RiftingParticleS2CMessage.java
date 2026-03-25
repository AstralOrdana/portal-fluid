package com.ordana.dimensional_tears.networking;

import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.configs.ClientConfigs;
import com.ordana.dimensional_tears.reg.ModParticles;
import net.mehvahdjukaar.moonlight.api.platform.network.Message;
import net.mehvahdjukaar.moonlight.api.platform.network.NetworkHelper;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record RiftingParticleS2CMessage(AABB boundingBox, Vec3 position, byte count, float heightDelta, boolean extinguished) implements Message {

    private static final float MIN_HEIGHT_MUL = 0.6F;
    private static final float MAX_HEIGHT_MUL = 1.1F;

    public static final TypeAndCodec<RegistryFriendlyByteBuf, RiftingParticleS2CMessage> TYPE = Message.makeType(
        DimensionalTearsRoot.res("clientbound_rifting_particle"), RiftingParticleS2CMessage::new);

    public RiftingParticleS2CMessage(FriendlyByteBuf buf) {
        this(new AABB(buf.readVec3(), buf.readVec3()), buf.readVec3(), buf.readByte(), buf.readFloat(), buf.readBoolean());
    }

    public RiftingParticleS2CMessage(Entity entity, byte count, float heightDelta, boolean extinguished) {
        this(entity.getBoundingBox(), entity.position(), count, heightDelta, extinguished);
    }

    public static void send(ServerLevel serverLevel, Entity entity, byte count, float heightDelta, boolean extinguished) {
        NetworkHelper.sendToAllClientPlayersInParticleRange(serverLevel, entity.blockPosition(), new RiftingParticleS2CMessage(entity, count, heightDelta, extinguished));
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeVec3(this.boundingBox.getMinPosition());
        buf.writeVec3(this.boundingBox.getMaxPosition());
        buf.writeVec3(this.position);
        buf.writeByte(this.count);
        buf.writeFloat(this.heightDelta);
        buf.writeBoolean(this.extinguished);
    }

    @Override
    public void handle(Context context) {
        Level level = context.getPlayer().level();
        RandomSource random = level.getRandom();

        double xSize = this.boundingBox.getXsize(),
            ySize = this.boundingBox.getYsize(),
            zSize = this.boundingBox.getZsize();

        for (int i = 0; i < this.count; i++) {
            double x = this.position.x + this.randomRadius(random, xSize);
            double y = this.position.y + this.randomHeight(random, ySize);
            double z = this.position.z + this.randomRadius(random, zSize);

            level.addParticle(this.particle(), x, y, z, 0.0, 0.0, 0.0);
        }
    }

    private ParticleOptions particle() {
        return this.extinguished ? ParticleTypes.SMOKE : ModParticles.RIFT_FLAME.get();
    }

    private double randomRadius(RandomSource randomSource, double size) {
        return (2.0 * randomSource.nextDouble() - 1.0) * ClientConfigs.EFFECT_PARTICLE_RADIUS.get() * size;
    }

    private double randomHeight(RandomSource randomSource, double size) {
        return size * this.heightDelta * Mth.randomBetween(randomSource, MIN_HEIGHT_MUL, MAX_HEIGHT_MUL);
    }

    @Override
    @NotNull
    public Type<? extends CustomPacketPayload> type() {
        return TYPE.type();
    }

}
