package com.ordana.dimensional_tears.mixins.rifting;

import com.ordana.dimensional_tears.fluids.DimensionalTearsFluid;
import com.ordana.dimensional_tears.util.TeleportHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class ExtinguishRiftingMixin {

    @Inject(method = "extinguishFire", at = @At("TAIL"))
    private void getSoundGroupMixin(CallbackInfo ci) {
        if (TeleportHelper.tryRemoveRiftingEffect((Entity)(Object)this))
            ((Entity)(Object)this).level().playSound(null, BlockPos.containing(((Entity)(Object)this).position()), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
    }
}
