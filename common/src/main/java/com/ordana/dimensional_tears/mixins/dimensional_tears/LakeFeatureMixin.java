package com.ordana.dimensional_tears.mixins.dimensional_tears;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * <a href="https://tangled.org/lerariemann.nekoweb.org/ProjectInfinity/blob/master/src/main/java/net/lerariemann/infinity/mixin/fixes/LakeFeatureMixin.java">Originally authored by LeraRiemann</a> (modified from)
 */
@SuppressWarnings("deprecation")
@Mixin(LakeFeature.class)
public class LakeFeatureMixin {

    @WrapOperation(method="place(Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/WorldGenLevel;getBiome(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/core/Holder;"))
    private Holder<Biome> fixVanillaLakeCrash(WorldGenLevel instance, BlockPos blockPos, Operation<Holder<Biome>> original, @Local(ordinal = 0) BlockPos origin) {
        return instance.getBiome(origin);
    }

}