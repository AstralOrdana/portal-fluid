package com.ordana.dimensional_tears.fabric;

import com.ordana.dimensional_tears.DimensionalTearsClient;
import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.reg.ModInteractionEvents;
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

public class DimensionalTearsRootFabric implements ModInitializer {

    public static MinecraftServer currentServer;

    @Override
    public void onInitialize() {
        DimensionalTearsRoot.commonInit();

        ServerLifecycleEvents.SERVER_STARTING.register(s -> currentServer = s);

        if (PlatHelper.getPhysicalSide().isClient())
            DimensionalTearsClient.init();

        UseBlockCallback.EVENT.register(DimensionalTearsRootFabric::onRightClickBlock);
    }

    public static InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        return ModInteractionEvents.onBlockClicked(player.getItemInHand(hand), player, level, hand, hitResult);
    }

}
