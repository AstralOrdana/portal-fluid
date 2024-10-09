package com.ordana.portal_fluid.reg;

import com.ordana.portal_fluid.PortalFluidRoot;
import com.ordana.portal_fluid.items.PortalFluidBottleItem;
import com.ordana.portal_fluid.items.PortalFluidBucketItem;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.function.Supplier;

public class ModItems {
    public static void init() {
    }

    public static <T extends Item> Supplier<T> regItem(String name, Supplier<T> itemSup) {
        return RegHelper.registerItem(PortalFluidRoot.res(name), itemSup);
    }

    public static final Supplier<Item> PORTAL_FLUID_BOTTLE = regItem("portal_fluid_bottle", () ->
            new PortalFluidBottleItem(new Item.Properties().food(PortalFluidBottleItem.PORTAL_FLUID).stacksTo(16).rarity(Rarity.UNCOMMON)));

    public static final Supplier<Item> PORTAL_FLUID_BUCKET = regItem("portal_fluid_bucket", () ->
            new PortalFluidBucketItem(ModFluids.PORTAL_FLUID.get(), (new Item.Properties().stacksTo(1))));
}
