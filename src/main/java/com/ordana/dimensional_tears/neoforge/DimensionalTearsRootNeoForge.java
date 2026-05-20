//? neoforge {
/*package com.ordana.dimensional_tears.neoforge;

import com.ordana.dimensional_tears.ClientHelper;
import com.ordana.dimensional_tears.DimensionalTearsPlatform;
import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.blocks.DimensionalTearsCauldronBlock;
import com.ordana.dimensional_tears.neoforge.reg.ModFluidTypes;
import com.ordana.dimensional_tears.reg.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.RegisterCauldronFluidContentEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(modid = DimensionalTearsRootNeoForge.MOD_ID)
@Mod(DimensionalTearsRoot.MOD_ID)
public class DimensionalTearsRootNeoForge {

    public static final String MOD_ID = DimensionalTearsRoot.MOD_ID;

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        InteractionResult interactionResult = ModInteractionEvents.onBlockClicked(event.getItemStack(), event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec());

        if (interactionResult != InteractionResult.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(interactionResult);
        }
    }

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        ResourceKey<? extends Registry<?>> key = event.getRegistryKey();
        if (key.equals(Registries.BLOCK)) {
           DimensionalTearsRoot.setInitiated();;
            ModBlocks.init();
            ModEffects.init();
            ModComponents.init();
            ModParticles.init();
            ModSoundEvents.init();
            ModWorldgenFeatures.bootstrap();
        } else if (key.equals(Registries.FLUID)) {
            ModFluids.init();
            ModFluidTypes.init();
        } else if (key.equals(Registries.ITEM)) {
            ModItems.init();
        }
        //FIXME
//        ModLootOverrides.INSTANCE.register();
//        RegHelper.addLootTableInjects(ModLootInjects::onLootInject);
    }

    @SubscribeEvent
    public static void register(BuildCreativeModeTabContentsEvent event) {
        ModCreativeTabs.registerItemsToTabs(new DimensionalTearsPlatform.CreativeTabEvent() {
            @Override
            public ResourceKey<CreativeModeTab> tabKey() {
                return event.getTabKey();
            }

            @Override
            public void insertAfter(ItemStack anchor, ItemStack newItem) {
                event.insertAfter(anchor, newItem, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }

            @Override
            public void insertBefore(ItemStack anchor, ItemStack newItem) {
                event.insertBefore(anchor, newItem, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        });
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerCauldron(RegisterCauldronFluidContentEvent event) {
        event.register(ModBlocks.DIMENSIONAL_TEARS_CAULDRON.get(), ModFluids.DIMENSIONAL_TEARS.get(), FluidType.BUCKET_VOLUME, DimensionalTearsCauldronBlock.LEVEL);
    }

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");
        DimensionalTearsRoot.registerMessages(type -> {
            registrar.playToClient(type.type(), type.codec(), (payload, context)->{
                payload.handle(new ClientHelper.NetworkContext(context.player()));
            });
        });
    }

}

*///?}