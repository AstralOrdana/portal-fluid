package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.PortalFluidRoot;
import com.ordana.dimensional_tears.items.PortalFluidBottleItem;
import com.ordana.dimensional_tears.items.PortalFluidBucketItem;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

import java.util.function.Supplier;

public class ModItems {

    public static final RegSupplier<PortalFluidBottleItem> DIMENSIONAL_TEARS_BOTTLE = regItem(
        "dimensional_tears_bottle",
        () -> new PortalFluidBottleItem(
            new Item.Properties()
                .food(PortalFluidBottleItem.FOOD_PROPERTIES)
                .stacksTo(16)
                .rarity(Rarity.UNCOMMON)
                .craftRemainder(Items.GLASS_BOTTLE)
        )
    );

    public static final RegSupplier<PortalFluidBucketItem> DIMENSIONAL_TEARS_BUCKET = regItem(
        "dimensional_tears_bucket",
        () -> new PortalFluidBucketItem(
            ModFluids.DIMENSIONAL_TEARS.get(),
            new Item.Properties()
                .stacksTo(1)
                .craftRemainder(Items.BUCKET)
        )
    );

    public static <T extends Item> RegSupplier<T> regItem(String name, Supplier<T> itemSup) {
        return RegHelper.registerItem(PortalFluidRoot.res(name), itemSup);
    }

    public static void init() {}

}
