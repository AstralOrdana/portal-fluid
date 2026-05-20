package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.DimensionalTearsRoot;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public interface ModComponents {

    RegSupplier<DataComponentType<GlobalPos>> ANCHOR_POS = regComponent(
        "anchor_pos",
        () -> DataComponentType.<GlobalPos>builder()
            .persistent(GlobalPos.CODEC)
            .networkSynchronized(GlobalPos.STREAM_CODEC)
            .build()
    );

    static <T> RegSupplier<DataComponentType<T>> regComponent(String path, Supplier<DataComponentType<T>> dataComponentSupplier) {
        Identifier id = DimensionalTearsRoot.res(path);
        return new RegSupplier<>(id, Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id, dataComponentSupplier.get()));
    }

    static void init() {}

}
