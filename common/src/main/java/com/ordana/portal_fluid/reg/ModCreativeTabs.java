package com.ordana.portal_fluid.reg;

import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.Arrays;
import java.util.function.Supplier;

public class ModCreativeTabs {

    public static void registerItemsToTabs(RegHelper.ItemToTabEvent event) {
        after(event, Items.MILK_BUCKET, CreativeModeTabs.TOOLS_AND_UTILITIES,
            ModItems.PORTAL_FLUID_BUCKET, ModItems.PORTAL_FLUID_BOTTLE
        );
    }

    private static void after(RegHelper.ItemToTabEvent event, Item target, ResourceKey<CreativeModeTab> tab, Supplier<?>... items) {
        ItemLike[] entries = Arrays.stream(items).map(supplier -> (ItemLike) supplier.get()).toArray(ItemLike[]::new);
        event.addAfter(tab, itemStack -> itemStack.is(target), entries);
    }

    private static void before(RegHelper.ItemToTabEvent event, Item target, ResourceKey<CreativeModeTab> tab, Supplier<?>... items) {
        ItemLike[] entries = Arrays.stream(items).map(supplier -> (ItemLike) supplier.get()).toArray(ItemLike[]::new);
        event.addBefore(tab, itemStack -> itemStack.is(target), entries);
    }

    public static void init() {
        RegHelper.addItemsToTabsRegistration(ModCreativeTabs::registerItemsToTabs);
    }

}