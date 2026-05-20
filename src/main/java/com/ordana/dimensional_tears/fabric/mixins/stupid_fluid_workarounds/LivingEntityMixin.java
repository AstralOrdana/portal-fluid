package com.ordana.dimensional_tears.fabric.mixins.stupid_fluid_workarounds;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.ordana.dimensional_tears.DimensionalTearsPlatform;
import com.ordana.dimensional_tears.fluids.DimensionalTearsFluid;
import com.ordana.dimensional_tears.reg.ModTags;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @WrapOperation(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getFluidHeight(Lnet/minecraft/tags/TagKey;)D", ordinal = 1))
    private double getDimTearsFluidHeight(LivingEntity instance, TagKey<Fluid> tagKey, Operation<Double> original, @Share("isInDimTears") LocalBooleanRef isInDimTearsRef) {
        boolean in = DimensionalTearsPlatform.isInDimTears(this);
        isInDimTearsRef.set(in);

        return in ? DimensionalTearsPlatform.getDimTearsHeight(this) : original.call(instance, tagKey);
    }

    @WrapOperation(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isInWater()Z"))
    private boolean shouldHandleJumpingLikeLava(LivingEntity instance, Operation<Boolean> original, @Share("isInDimTears") LocalBooleanRef isInDimTearsRef) {
        return original.call(instance) || isInDimTearsRef.get();
    }

    @WrapOperation(method = "aiStep", at = @At(value = "FIELD", target = "Lnet/minecraft/tags/FluidTags;WATER:Lnet/minecraft/tags/TagKey;", ordinal = 1, opcode = Opcodes.GETSTATIC))
    private TagKey<Fluid> finalizeTagForMovement(Operation<TagKey<Fluid>> original, @Share("isInDimTears") LocalBooleanRef isInDimTearsRef) {
        return isInDimTearsRef.get() ? ModTags.DIMENSIONAL_TEARS : original.call();
    }

}
