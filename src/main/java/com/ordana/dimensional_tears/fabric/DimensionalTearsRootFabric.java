
//? fabric {
package com.ordana.dimensional_tears.fabric;

import com.ordana.dimensional_tears.DimensionalTearsPlatform;
import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.blocks.DimensionalTearsCauldronBlock;
import com.ordana.dimensional_tears.reg.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.registry.fluid.EntityFluidInteractionRegistry;
import net.fabricmc.fabric.api.registry.fluid.FluidBehavior;
import net.fabricmc.fabric.api.transfer.v1.fluid.CauldronFluidContent;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class DimensionalTearsRootFabric implements ModInitializer {

    public static MinecraftServer currentServer;

    @Override
    public void onInitialize() {
        if (DimensionalTearsRoot.isInitiated())
            return;

        DimensionalTearsRoot.setInitiated();;

        //FIXME
//        ModLootOverrides.INSTANCE.register();
        ModBlocks.init();
        ModFluids.init();
        ModItems.init();
        ModEffects.init();
        ModComponents.init();
        ModParticles.init();
        ModSoundEvents.init();
        ModWorldgenFeatures.bootstrap();
        CreativeModeTabEvents.MODIFY_OUTPUT_ALL.register((tab, output) -> ModCreativeTabs.registerItemsToTabs(new DimensionalTearsPlatform.CreativeTabEvent() {
            @Override
            public ResourceKey<CreativeModeTab> tabKey() {
               return BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(tab).orElseThrow();
            }

            @Override
            public void insertAfter(ItemStack anchor, ItemStack newItem) {
                output.insertAfter(anchor, newItem);
            }

            @Override
            public void insertBefore(ItemStack anchor, ItemStack newItem) {
                output.insertBefore(anchor, newItem);
            }
        }));

//        RegHelper.addLootTableInjects(ModLootInjects::onLootInject);

        ServerLifecycleEvents.SERVER_STARTING.register(s -> currentServer = s);


        UseBlockCallback.EVENT.register(DimensionalTearsRootFabric::onRightClickBlock);
        CauldronFluidContent.registerCauldron(ModBlocks.DIMENSIONAL_TEARS_CAULDRON.get(), ModFluids.DIMENSIONAL_TEARS.get(), FluidConstants.BOTTLE, DimensionalTearsCauldronBlock.LEVEL);
        DimensionalTearsRoot.registerMessages(type -> PayloadTypeRegistry.clientboundPlay().register(type.type(), type.codec()));
        EntityFluidInteractionRegistry.register(ModTags.DIMENSIONAL_TEARS, DimensionalTearsBehavior.DIMENSIONAL_TEARS);
    }

    public static InteractionResult onRightClickBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        return ModInteractionEvents.onBlockClicked(player.getItemInHand(hand), player, level, hand, hitResult);
    }

}
//?}