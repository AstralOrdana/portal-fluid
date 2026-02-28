package com.ordana.portal_fluid.reg;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public record RespawnData(Vec3 position, @Nullable ServerLevel serverLevel) {

    private static final RespawnData EMPTY = new RespawnData(Vec3.ZERO, null);

    public static RespawnData create(MinecraftServer server, ResourceKey<Level> respawnDimension, @Nullable BlockPos respawnPosition, float respawnAngle) {
        ServerLevel serverLevel = server.getLevel(respawnDimension);

        if (serverLevel == null)
            return EMPTY;

        AtomicReference<Vec3> exactSpawnPosition = new AtomicReference<>();

        if (respawnPosition != null) {
            Optional<ServerPlayer.RespawnPosAngle> spawnPosition = ServerPlayer.findRespawnAndUseSpawnBlock(serverLevel, respawnPosition, respawnAngle, true, true);
            spawnPosition.ifPresent(respawnPosAngle -> exactSpawnPosition.set(respawnPosAngle.position()));
        }

        if (exactSpawnPosition.get() == null) {
            serverLevel = server.getLevel(Level.OVERWORLD);
            assert serverLevel != null;

            exactSpawnPosition.set(serverLevel.getSharedSpawnPos().getCenter());
        }

        return new RespawnData(exactSpawnPosition.get(), serverLevel);
    }

    public static RespawnData create(ServerPlayer serverPlayer) {
        return create(serverPlayer.server, serverPlayer.getRespawnDimension(), serverPlayer.getRespawnPosition(), serverPlayer.getRespawnAngle());
    }

    public void teleport(ServerPlayer serverPlayer) {
        serverPlayer.teleportTo(this.serverLevel, this.position.x, this.position.y, this.position.z, serverPlayer.getYRot(), serverPlayer.getXRot());
    }

}
