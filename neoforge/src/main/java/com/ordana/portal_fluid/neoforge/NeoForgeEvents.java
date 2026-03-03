package com.ordana.portal_fluid.neoforge;

import com.ordana.portal_fluid.reg.ModEvents;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = PortalFluidRootNeoForge.MOD_ID)
public class NeoForgeEvents {

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        InteractionResult interactionResult = ModEvents.onBlockClicked(event.getItemStack(), event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec());

        if (interactionResult != InteractionResult.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(interactionResult);
        }
    }

}
