package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.items.DimensionalTearsBottleItem;
import com.ordana.dimensional_tears.items.DimensionalTearsBucketItem;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

import java.util.function.Supplier;

public interface ModItems {

    RegSupplier<DimensionalTearsBottleItem> DIMENSIONAL_TEARS_BOTTLE = regItem(
        "dimensional_tears_bottle",
        () -> new DimensionalTearsBottleItem(
            new Item.Properties()
                .food(DimensionalTearsBottleItem.FOOD_PROPERTIES)
                .stacksTo(16)
                .rarity(Rarity.UNCOMMON)
                .craftRemainder(Items.GLASS_BOTTLE)
        )
    );

    RegSupplier<DimensionalTearsBucketItem> DIMENSIONAL_TEARS_BUCKET = regItem(
        "dimensional_tears_bucket",
        () -> new DimensionalTearsBucketItem(
            ModFluids.DIMENSIONAL_TEARS.get(),
            new Item.Properties()
                .stacksTo(1)
                .craftRemainder(Items.BUCKET)
        )
    );

    static <T extends Item> RegSupplier<T> regItem(String path, Supplier<T> itemSupplier) {
        return RegHelper.registerItem(DimensionalTearsRoot.res(path), itemSupplier);
    }

    static void init() {}

}
