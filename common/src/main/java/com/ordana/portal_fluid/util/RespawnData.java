package com.ordana.portal_fluid.util;

import com.ordana.portal_fluid.items.PortalFluidBottleItem;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record RespawnData(@Nullable Vec3 position, @Nullable ServerLevel serverLevel) {

    public static RespawnData create(MinecraftServer server, ResourceKey<Level> respawnDimension, @Nullable BlockPos respawnPosition, float respawnAngle, @Nullable ItemStack itemStack) {
        ServerLevel serverLevel = server.getLevel(respawnDimension);
        Vec3 adjustSpawnPosition = preciseRespawnPosition(serverLevel, server, respawnPosition, respawnAngle, itemStack);

        return new RespawnData(adjustSpawnPosition, serverLevel);
    }

    @Nullable
    private static Vec3 preciseRespawnPosition(@Nullable ServerLevel serverLevel, MinecraftServer minecraftServer, @Nullable BlockPos blockPos, float respawnAngle, @Nullable ItemStack itemStack) {
        if (serverLevel == null)
            return null;

        Vec3 vec3 = PortalFluidBottleItem.getAnchorRespawnPosition(minecraftServer, itemStack);

        if (blockPos != null) {
            Optional<ServerPlayer.RespawnPosAngle> spawnPosition = ServerPlayer.findRespawnAndUseSpawnBlock(serverLevel, blockPos, respawnAngle, true, true);

            if (spawnPosition.isPresent())
                vec3 = spawnPosition.get().position();
        }

        if (vec3 == null) {
            serverLevel = minecraftServer.getLevel(Level.OVERWORLD);
            return serverLevel == null ? null : serverLevel.getSharedSpawnPos().getBottomCenter();
        }

        return vec3;
    }

    public static RespawnData create(ServerPlayer serverPlayer, @Nullable ItemStack itemStack) {
        return create(serverPlayer.server, serverPlayer.getRespawnDimension(), serverPlayer.getRespawnPosition(), serverPlayer.getRespawnAngle(), itemStack);
    }

    public void teleport(ServerPlayer serverPlayer) {
        if (this.position == null)
            return;

        serverPlayer.teleportTo(this.serverLevel, this.position.x, this.position.y, this.position.z, serverPlayer.getYRot(), serverPlayer.getXRot());
    }

}
