package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.DimensionalTearsPlatform;
import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.items.DimensionalTearsBottleItem;
import com.ordana.dimensional_tears.items.DimensionalTearsBucketItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.ordana.dimensional_tears.DimensionalTearsRoot.MOD_ID;

public interface ModItems {

    RegSupplier<DimensionalTearsBottleItem> DIMENSIONAL_TEARS_BOTTLE = regItem(
        "dimensional_tears_bottle",
        DimensionalTearsBottleItem::new,
            new Item.Properties()
                .food(DimensionalTearsBottleItem.FOOD_PROPERTIES)
                .stacksTo(16)
                .rarity(Rarity.UNCOMMON)
                .craftRemainder(Items.GLASS_BOTTLE)
    );

    RegSupplier<DimensionalTearsBucketItem> DIMENSIONAL_TEARS_BUCKET = regItem(
        "dimensional_tears_bucket",
        p->new DimensionalTearsBucketItem(ModFluids.DIMENSIONAL_TEARS.get(), p),
            new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.UNCOMMON)
                .craftRemainder(Items.BUCKET)
    );


    static <T extends Item> RegSupplier<T> regItem(String id, Function<Item.Properties, T> factory, Item.Properties settings) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, id));
        T block = factory.apply(settings.setId(key));
        var entry = Registry.register(BuiltInRegistries.ITEM, key, block);
        return new RegSupplier<>(key.identifier(), entry);
    }


    static void init() {}

}
