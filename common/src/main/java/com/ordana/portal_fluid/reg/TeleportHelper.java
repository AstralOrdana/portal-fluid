/*
Methods in this class from MajruszLibrary under the MIT license

Full repo can be found here: https://github.com/Majrusz/MajruszLibrary

        Permission is hereby granted, free of charge, to any person obtaining
        a copy of this software and associated documentation files (the
        'Software'), to deal in the Software without restriction, including
        without limitation the rights to use, copy, modify, merge, publish,
        distribute, sublicense, and/or sell copies of the Software, and to
        permit persons to whom the Software is furnished to do so, subject to
        the following conditions:

        The above copyright notice and this permission notice shall be
        included in all copies or substantial portions of the Software.

        THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
        EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
        MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
        IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
        CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
        TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
        SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.ordana.portal_fluid.reg;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class TeleportHelper {

    public static void teleportEntity(Level level, Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer && serverPlayer.getRespawnPosition() != null)
            TeleportHelper.teleportPlayerToSpawnPosition(serverPlayer);
        else TeleportHelper.teleportToWorldspawn(level, entity);

        playSound(entity, 0.5F);
    }

    public static void teleportPlayerToSpawnPosition(ServerPlayer serverPlayer) {
        RespawnData.create(serverPlayer).teleport(serverPlayer);
        playSound(serverPlayer);
    }

    public static void teleportToAnchorPosition(ServerPlayer serverPlayer, GlobalPos globalPos) {
        if (globalPos == null)
            return;

        Vec3 tpPos = globalPos.pos().above().getBottomCenter();

        serverPlayer.teleportTo(serverPlayer.server.getLevel(globalPos.dimension()), tpPos.x, tpPos.y, tpPos.z, serverPlayer.getYRot(), serverPlayer.getXRot());
        playSound(serverPlayer);
    }

    public static void teleportToWorldspawn(Level level, Entity entity) {
        Vec3 spawnPosition = Vec3.atCenterOf(level.getSharedSpawnPos());
        entity.teleportTo(spawnPosition.x, spawnPosition.y, spawnPosition.z);

        playSound(entity);
    }

    private static void playSound(Entity entity) {
        playSound(entity, 1.0F);
    }

    private static void playSound(Entity entity, float pitch) {
        entity.playSound(ModSoundEvents.PORTAL_FLUID_TELEPORT.get(), pitch, 1.0F);
    }

    public static boolean canTeleport(Level level, Entity entity, BlockPos blockPos) {
        if (entity instanceof LocalPlayer || entity.getType().is(ModTags.PORTAL_FLUID_IMMUNE))
            return false;

        if (entity.isPassenger() || entity.isVehicle())
            return false;

        return !entity.isCrouching() && !blockPos.equals(level.getSharedSpawnPos());
    }

}
