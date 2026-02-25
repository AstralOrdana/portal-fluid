package com.ordana.portal_fluid.reg;

import com.ordana.portal_fluid.PortalFluidRoot;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponentType;

import java.util.function.Supplier;

public class ModComponents {
    public static void init() {
    }

    public static <T> Supplier<DataComponentType<T>> regComponent(String name, Supplier<DataComponentType<T>> itemSup) {
        return RegHelper.registerDataComponent(PortalFluidRoot.res(name), itemSup);
    }

    public static final Supplier<DataComponentType<GlobalPos>> ANCHOR_POS = regComponent("anchor_pos", ()->
            DataComponentType.<GlobalPos>builder().persistent(GlobalPos.CODEC).networkSynchronized(GlobalPos.STREAM_CODEC).build());

}
