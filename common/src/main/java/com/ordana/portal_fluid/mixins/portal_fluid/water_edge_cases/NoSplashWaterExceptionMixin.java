package com.ordana.portal_fluid.mixins.portal_fluid.water_edge_cases;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.ordana.portal_fluid.fluids.PortalFluid;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class NoSplashWaterExceptionMixin {

    @Shadow public abstract Level level();
    @Shadow public abstract AABB getBoundingBox();

    @WrapWithCondition(method = "updateInWaterStateAndDoWaterCurrentPushing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;doWaterSplashEffect()V"))
    private boolean doSplashIfNotTouchingPortalFluid(Entity instance) {
        return !PortalFluid.isTouching(instance.level(), this.getBoundingBox().deflate(0.001));
    }

}
