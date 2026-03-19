package com.ordana.dimensional_tears.mixins.rifting;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.ordana.dimensional_tears.util.TeleportHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ Entity.class, ServerPlayer.class })
public class EntityDimChangeMixin {

    @ModifyReturnValue(method = "changeDimension", at = @At("RETURN"))
    private Entity removeRiftingOnDimChange(@Nullable Entity original) {
        TeleportHelper.tryRemoveRiftingEffect(original);
        return original;
    }

}
