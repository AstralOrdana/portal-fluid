package com.ordana.dimensional_tears.mixins.nether_portal;

import com.ordana.dimensional_tears.PortalFluidRoot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public abstract class NetherPortalSoundMixin {

    @Shadow protected abstract Block asBlock();

    @Inject(method = "getSoundType", at = @At("TAIL"), cancellable = true)
    private void getSoundGroupMixin(CallbackInfoReturnable<SoundType> cir){
        if (PortalFluidRoot.isInitiated() && this.asBlock() == Blocks.NETHER_PORTAL)
            cir.setReturnValue(SoundType.EMPTY);
    }

}