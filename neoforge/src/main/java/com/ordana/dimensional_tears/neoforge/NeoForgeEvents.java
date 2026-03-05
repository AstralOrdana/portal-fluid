package com.ordana.dimensional_tears.neoforge;

import com.ordana.dimensional_tears.reg.ModInteractionEvents;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = DimensionalTearsRootNeoForge.MOD_ID)
public class NeoForgeEvents {

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        InteractionResult interactionResult = ModInteractionEvents.onBlockClicked(event.getItemStack(), event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec());

        if (interactionResult != InteractionResult.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(interactionResult);
        }
    }

}
