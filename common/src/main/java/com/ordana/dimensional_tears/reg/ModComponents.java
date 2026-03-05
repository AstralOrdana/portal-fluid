package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.DimensionalTearsRoot;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponentType;

import java.util.function.Supplier;

public interface ModComponents {

    Supplier<DataComponentType<GlobalPos>> ANCHOR_POS = regComponent(
        "anchor_pos",
        () -> DataComponentType.<GlobalPos>builder()
            .persistent(GlobalPos.CODEC)
            .networkSynchronized(GlobalPos.STREAM_CODEC)
            .build()
    );

    static <T> Supplier<DataComponentType<T>> regComponent(String path, Supplier<DataComponentType<T>> dataComponentSupplier) {
        return RegHelper.registerDataComponent(DimensionalTearsRoot.res(path), dataComponentSupplier);
    }

    static void init() {}

}
