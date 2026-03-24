package com.ordana.dimensional_tears.mixins.rifting;

import com.ordana.dimensional_tears.DimensionalTearsPlatform;
import com.ordana.dimensional_tears.util.TeleportHelper;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class ExtinguishRiftingMixin {

    @Inject(method = "extinguishFire", at = @At("TAIL"))
    private void removeRiftingEffectWithSound(CallbackInfo ci) {
        Entity thisEntity = (Entity) (Object) this;

        if (!DimensionalTearsPlatform.isInDimTears(thisEntity))
            TeleportHelper.tryRemoveRiftingEffect(thisEntity, true);
    }

}
