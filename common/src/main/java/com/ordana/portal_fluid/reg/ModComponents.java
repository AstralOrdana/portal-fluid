package com.ordana.portal_fluid.reg;

import com.mojang.serialization.Codec;
import com.ordana.portal_fluid.PortalFluidRoot;
import com.ordana.portal_fluid.items.PortalFluidBottleItem;
import com.ordana.portal_fluid.items.PortalFluidBucketItem;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.function.Supplier;

public class ModComponents {
    public static void init() {
    }

    public static <T> Supplier<DataComponentType<T>> regComponent(String name, Supplier<DataComponentType<T>> itemSup) {
        return RegHelper.registerDataComponent(PortalFluidRoot.res(name), itemSup);
    }

    public static final Supplier<DataComponentType<GlobalPos>> ANCHOR_POS = regComponent("anchor_pos", ()->
            DataComponentType.<GlobalPos>builder().persistent(GlobalPos.CODEC).networkSynchronized(GlobalPos.STREAM_CODEC).build());

    public static final Supplier<DataComponentType<Boolean>> BOOL = regComponent("bool", ()->
            DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
}
