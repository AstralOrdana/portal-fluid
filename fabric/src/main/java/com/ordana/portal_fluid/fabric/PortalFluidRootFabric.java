package com.ordana.portal_fluid.fabric;

import com.ordana.portal_fluid.PortalFluidClient;
import com.ordana.portal_fluid.PortalFluidRoot;
import com.ordana.portal_fluid.reg.ModEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;


public class PortalFluidRootFabric implements ModInitializer {

    public static MinecraftServer currentServer;

    @Override
    public void onInitialize() {
        PortalFluidRoot.commonInit();

        ServerLifecycleEvents.SERVER_STARTING.register(s -> currentServer = s);

        if(PlatHelper.getPhysicalSide().isClient()) {
            PortalFluidClient.init();
        }

        UseBlockCallback.EVENT.register(PortalFluidRootFabric::onRightClickBlock);
    }

    public static InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        return ModEvents.onBlockCLicked(player.getItemInHand(hand), player, level, hand, hitResult);
    }

}
