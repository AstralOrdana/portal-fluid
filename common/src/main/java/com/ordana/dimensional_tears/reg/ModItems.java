package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.DimensionalTearsPlatform;
import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.items.DimensionalTearsBottleItem;
import com.ordana.dimensional_tears.items.DimensionalTearsBucketItem;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;

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
                .rarity(Rarity.UNCOMMON)
                .craftRemainder(Items.BUCKET)
        )
    );

    RegSupplier<SpawnEggItem> DIMENSIONAL_BEAR_SPAWN_EGG = regItem(
            "dimensional_bear_spawn_egg",
            () -> PlatHelper.newSpawnEgg(
                    ModEntityTypes.DIMENSIONAL_BEAR, 0x120c24, 0x8308e4, new Item.Properties()
            )
    );

    RegSupplier<ChorusFruitItem> BEAR_CLAW = regItem(
            "bear_claw",
            () -> new ChorusFruitItem(
                    new Item.Properties()
                            .food(Foods.CHORUS_FRUIT)
                            .stacksTo(16)
                            .rarity(Rarity.UNCOMMON)
            )
    );


    static <T extends Item> RegSupplier<T> regItem(String path, Supplier<T> itemSupplier) {
        DimensionalTearsPlatform.addAlias(BuiltInRegistries.ITEM, path);
        return RegHelper.registerItem(DimensionalTearsRoot.res(path), itemSupplier);
    }

    static void init() {}

}
