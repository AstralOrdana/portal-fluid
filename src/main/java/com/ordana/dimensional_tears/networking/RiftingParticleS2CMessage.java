package com.ordana.dimensional_tears.networking;

import com.ordana.dimensional_tears.ClientHelper;
import com.ordana.dimensional_tears.DimensionalTearsClient;
import com.ordana.dimensional_tears.DimensionalTearsPlatform;
import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.reg.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record RiftingParticleS2CMessage(AABB boundingBox, Vec3 position, byte count, float heightDelta, boolean extinguished) implements CustomPacketPayload {

    private static final float MIN_HEIGHT_MUL = 0.6F;
    private static final float MAX_HEIGHT_MUL = 1.1F;

    private static final StreamCodec<RegistryFriendlyByteBuf, RiftingParticleS2CMessage> STREAM_CODEC = StreamCodec.composite(
            Vec3.STREAM_CODEC, RiftingParticleS2CMessage::getMinPosition,
            Vec3.STREAM_CODEC, RiftingParticleS2CMessage::getMaxPosition,
            Vec3.STREAM_CODEC, RiftingParticleS2CMessage::position,
            ByteBufCodecs.BYTE, RiftingParticleS2CMessage::count,
            ByteBufCodecs.FLOAT, RiftingParticleS2CMessage::heightDelta,
            ByteBufCodecs.BOOL, RiftingParticleS2CMessage::extinguished,
            (min, max, position, count, heightDelta, extinguished)-> new RiftingParticleS2CMessage(AABB.encapsulatingFullBlocks(BlockPos.containing(min), BlockPos.containing(max)), position, count, heightDelta, extinguished)
    );

    private Vec3 getMinPosition() {
        return boundingBox.getMinPosition();
    }

    private Vec3 getMaxPosition() {
        return boundingBox.getMaxPosition();
    }

    public static final CustomPacketPayload.TypeAndCodec<RegistryFriendlyByteBuf, RiftingParticleS2CMessage> TYPE = new TypeAndCodec<>(
            new Type<>(DimensionalTearsRoot.res("clientbound_rifting_particle")), RiftingParticleS2CMessage.STREAM_CODEC);

    public RiftingParticleS2CMessage(Entity entity, byte count, float heightDelta, boolean extinguished) {
        this(entity.getBoundingBox(), entity.position(), count, heightDelta, extinguished);
    }

    public static void send(ServerLevel serverLevel, Entity entity, byte count, float heightDelta, boolean extinguished) {
        DimensionalTearsPlatform.sendToAllClientPlayersInParticleRange(serverLevel, entity.blockPosition(), new RiftingParticleS2CMessage(entity, count, heightDelta, extinguished));
    }

    public void handle(ClientHelper.NetworkContext context) {
        Level level = context.player().level();
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
        return this.extinguished ? ParticleTypes.SMOKE : ModParticles.RIFT_FLAME;
    }

    private double randomRadius(RandomSource randomSource, double size) {
        return (2.0 * randomSource.nextDouble() - 1.0) * DimensionalTearsClient.CONFIG.particles.EFFECT_PARTICLE_RADIUS * size;
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
