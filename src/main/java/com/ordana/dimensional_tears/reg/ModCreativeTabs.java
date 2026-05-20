
package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.DimensionalTearsPlatform;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.Arrays;
import java.util.List;

public interface ModCreativeTabs {

    static void registerItemsToTabs(DimensionalTearsPlatform.CreativeTabEvent event) {
        after(event, Items.MILK_BUCKET, CreativeModeTabs.TOOLS_AND_UTILITIES,
            ModItems.DIMENSIONAL_TEARS_BUCKET, ModItems.DIMENSIONAL_TEARS_BOTTLE
        );
    }

    private static void after(DimensionalTearsPlatform.CreativeTabEvent event, Item target, ResourceKey<CreativeModeTab> tab, RegSupplier<?>... items) {
        List<ItemLike> entries = Arrays.stream(items).map(supplier -> (ItemLike) supplier.get()).toList().reversed();

        for (ItemLike entry : entries) {
            if (tab.equals(event.tabKey()))
                event.insertAfter(target.getDefaultInstance(), entry.asItem().getDefaultInstance());
        }
    }

}