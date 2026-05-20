package com.ordana.dimensional_tears.mixins.gateway;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.ordana.dimensional_tears.reg.ModTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Fixes <a href="https://github.com/AstralOrdana/portal-fluid/issues/2">GitHub issue #2</a>
 */
@Mixin(TheEndGatewayBlockEntity.class)
public abstract class TheEndGatewayBlockEntityMixin {

    @Shadow
    private static LevelChunk getChunk(Level level, Vec3 vec3) {
        throw new AssertionError();
    }

    @ModifyReturnValue(method = "isChunkEmpty", at = @At("RETURN"))
    private static boolean skipPastDimTears(boolean original, ServerLevel serverLevel, Vec3 vec3) {
        if (original)
            return true;

        LevelChunkSection[] levelChunkSections = getChunk(serverLevel, vec3).getSections();

        for (int i = levelChunkSections.length - 1; i >= 0; i--) {
            if (levelChunkSections[i].maybeHas(blockState -> !blockState.isAir() && !blockState.getFluidState().is(ModTags.DIMENSIONAL_TEARS)))
                return false;
        }

        return true;
    }

}
