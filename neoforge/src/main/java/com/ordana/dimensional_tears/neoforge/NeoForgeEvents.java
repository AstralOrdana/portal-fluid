package com.ordana.dimensional_tears.neoforge;

import com.ordana.dimensional_tears.blocks.DimensionalTearsCauldronBlock;
import com.ordana.dimensional_tears.entity.DimensionalBear;
import com.ordana.dimensional_tears.fluids.DimensionalTearsFluidRenderer;
import com.ordana.dimensional_tears.neoforge.reg.ModFluidTypes;
import com.ordana.dimensional_tears.reg.ModBlocks;
import com.ordana.dimensional_tears.reg.ModEntityTypes;
import com.ordana.dimensional_tears.reg.ModFluids;
import com.ordana.dimensional_tears.reg.ModInteractionEvents;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.RegisterCauldronFluidContentEvent;

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

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerCauldron(RegisterCauldronFluidContentEvent event) {
        event.register(ModBlocks.DIMENSIONAL_TEARS_CAULDRON.get(), ModFluids.DIMENSIONAL_TEARS.get(), FluidType.BUCKET_VOLUME, DimensionalTearsCauldronBlock.LEVEL);
    }

    @SuppressWarnings({ "CastToIncompatibleInterface", "DataFlowIssue" })
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerClientFluid(RegisterClientExtensionsEvent event) {
        event.registerFluidType((IClientFluidTypeExtensions) new DimensionalTearsFluidRenderer(), ModFluidTypes.DIMENSIONAL_TEARS_TYPE.get());
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerClientFluid(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.DIMENSIONAL_BEAR.get(), DimensionalBear.createAttributes().build());
    }

}
