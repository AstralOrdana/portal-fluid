package com.ordana.portal_fluid.mixins;

import com.ordana.portal_fluid.PortalFluidRoot;
import com.ordana.portal_fluid.configs.CommonConfigs;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public abstract class NetherPortalSoundMixin {

    @Shadow protected abstract Block asBlock();
    @Unique private static final SoundType NETHER_PORTAL = new SoundType(1.0F, 0.9F, SoundEvents.RESPAWN_ANCHOR_DEPLETE.value(), SoundEvents.GLASS_STEP, SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundEvents.GLASS_HIT, SoundEvents.GLASS_FALL);

    @Inject(method = "getSoundType", at = @At("TAIL"), cancellable = true)
    private void getSoundGroupMixin(CallbackInfoReturnable<SoundType> cir){
        if (PortalFluidRoot.isInitiated() && CommonConfigs.PORTAL_DESTRUCTION_SOUND.get() && this.asBlock() == Blocks.NETHER_PORTAL)
            cir.setReturnValue(NETHER_PORTAL);
    }

}