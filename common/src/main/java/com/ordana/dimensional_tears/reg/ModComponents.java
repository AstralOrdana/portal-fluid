package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.PortalFluidRoot;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponentType;

import java.util.function.Supplier;

public class ModComponents {

    public static final Supplier<DataComponentType<GlobalPos>> ANCHOR_POS = regComponent(
        "anchor_pos",
        () -> DataComponentType.<GlobalPos>builder()
            .persistent(GlobalPos.CODEC)
            .networkSynchronized(GlobalPos.STREAM_CODEC)
            .build()
    );

    public static <T> Supplier<DataComponentType<T>> regComponent(String name, Supplier<DataComponentType<T>> itemSup) {
        return RegHelper.registerDataComponent(PortalFluidRoot.res(name), itemSup);
    }

    public static void init() {}

}
