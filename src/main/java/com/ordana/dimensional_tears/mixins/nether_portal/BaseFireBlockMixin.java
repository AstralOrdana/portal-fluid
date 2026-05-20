package com.ordana.dimensional_tears.mixins.nether_portal;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.configs.CommonConfigs;
import com.ordana.dimensional_tears.reg.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BaseFireBlock.class)
public class BaseFireBlockMixin {

	@ModifyExpressionValue(method = "onPlace", at = @At(value = "INVOKE", target = "Ljava/util/Optional;isPresent()Z"))
	private boolean canOpenPortal(boolean original, BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (!original || !DimensionalTearsRoot.CONFIG.capabilities.FlINT_AND_STEEL_PORTAL_LIGHTING)
			return false;

        if (DimensionalTearsRoot.CONFIG.sounds.PORTAL_CREATION_SOUND)
            level.playSound(null, blockPos, ModSoundEvents.PORTAL_SPAWN.value(), SoundSource.BLOCKS);

        return true;

    }

}