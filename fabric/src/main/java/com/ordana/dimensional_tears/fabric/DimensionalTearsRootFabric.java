package com.ordana.dimensional_tears.fabric;

import com.ordana.dimensional_tears.DimensionalTearsClient;
import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.blocks.DimensionalTearsCauldronBlock;
import com.ordana.dimensional_tears.entity.DimensionalBear;
import com.ordana.dimensional_tears.fluids.DimensionalTearsFluidRenderer;
import com.ordana.dimensional_tears.reg.ModBlocks;
import com.ordana.dimensional_tears.reg.ModEntityTypes;
import com.ordana.dimensional_tears.reg.ModFluids;
import com.ordana.dimensional_tears.reg.ModInteractionEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.transfer.v1.fluid.CauldronFluidContent;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class DimensionalTearsRootFabric implements ModInitializer {

    public static MinecraftServer currentServer;

    @SuppressWarnings({ "CastToIncompatibleInterface", "DataFlowIssue" })
    @Override
    public void onInitialize() {
        DimensionalTearsRoot.commonInit();

        ServerLifecycleEvents.SERVER_STARTING.register(s -> currentServer = s);

        if (PlatHelper.getPhysicalSide().isClient()) {
            DimensionalTearsClient.init();
            ClientLifecycleEvents.CLIENT_STARTED.register(minecraft ->
                FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.DIMENSIONAL_TEARS.get(), ModFluids.FLOWING_DIMENSIONAL_TEARS.get(), (FluidRenderHandler) new DimensionalTearsFluidRenderer())
            );
        }

        UseBlockCallback.EVENT.register(DimensionalTearsRootFabric::onRightClickBlock);
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            // delayed to prevent a crash from accessing it before registration
            CauldronFluidContent.registerCauldron(ModBlocks.DIMENSIONAL_TEARS_CAULDRON.get(), ModFluids.DIMENSIONAL_TEARS.get(), FluidConstants.BOTTLE, DimensionalTearsCauldronBlock.LEVEL);
        });
        PlatHelper.addCommonSetup(()->{
            FabricDefaultAttributeRegistry.register(ModEntityTypes.DIMENSIONAL_BEAR.get(), DimensionalBear.createAttributes());
        });
    }

    public static InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        return ModInteractionEvents.onBlockClicked(player.getItemInHand(hand), player, level, hand, hitResult);
    }

}
