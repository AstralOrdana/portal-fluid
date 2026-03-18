package com.ordana.dimensional_tears.fabric.mixins.stupid_fluid_workarounds;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.ordana.dimensional_tears.fluids.DimensionalTearsFluid;
import com.ordana.dimensional_tears.reg.ModTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow protected boolean firstTick;
    @Shadow public abstract boolean updateFluidHeightAndDoFluidPushing(TagKey<Fluid> tagKey, double d);

    @Shadow public abstract void resetFallDistance();

    @ModifyReturnValue(method = "updateInWaterStateAndDoFluidPushing", at = @At("RETURN"))
    private boolean nonWaterFluidPush(boolean original) {
        if (original)
            return true;

        if (this.updateFluidHeightAndDoFluidPushing(ModTags.DIMENSIONAL_TEARS, DimensionalTearsFluid.MOTION_SCALE)) {
            this.resetFallDistance();
            return true;
        }

        return false;
    }

    @ModifyReturnValue(method = "canSpawnSprintParticle", at = @At("RETURN"))
    private boolean canSpawnSprintParticleImpl(boolean original) {
        return original && !(DimensionalTearsFluid.isIn(Entity.class.cast(this)) && !this.firstTick);
    }

}
