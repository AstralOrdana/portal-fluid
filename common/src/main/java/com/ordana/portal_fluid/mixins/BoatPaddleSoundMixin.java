package com.ordana.portal_fluid.mixins;

import com.ordana.portal_fluid.reg.ModSoundEvents;
import com.ordana.portal_fluid.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Boat.class)
public abstract class BoatPaddleSoundMixin extends Entity {


    public BoatPaddleSoundMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "getPaddleSound", at = @At("HEAD"), cancellable = true)
    public void insertFluidTick(CallbackInfoReturnable<SoundEvent> cir) {
        if (this.isInPortalFluid()) cir.setReturnValue(ModSoundEvents.BOAT_PADDLE_PORTAL_FLUID.get());
    }

    @Unique
    private boolean isInPortalFluid() {
        AABB aABB = this.getBoundingBox();

        int minX = Mth.floor(aABB.minX);
        int maxX = Mth.ceil(aABB.maxX);
        int minY = Mth.floor(aABB.minY);
        int maxY = Mth.ceil(aABB.minY + 0.001);
        int minZ = Mth.floor(aABB.minZ);
        int maxZ = Mth.ceil(aABB.maxZ);

        boolean inPortalFluid = false;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    mutableBlockPos.set(x, y, z);
                    FluidState fluidState = this.level().getFluidState(mutableBlockPos);

                    if (fluidState.is(ModTags.PORTAL_FLUID)) {
                        float height = y + fluidState.getHeight(this.level(), mutableBlockPos);
                        inPortalFluid |= aABB.minY < height;
                    }
                }
            }
        }

        return inPortalFluid;
    }

}
