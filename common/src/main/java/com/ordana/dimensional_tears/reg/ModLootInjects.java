package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.PortalFluidRoot;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ModLootInjects {

    private static final List<String> lootChests = List.of("ruined_portal");

    public static void onLootInject(RegHelper.LootInjectEvent event) {
        ResourceLocation name = event.getTable();

        for (String loot : lootChests) {
            if (name.equals(ResourceLocation.withDefaultNamespace("chests/" + loot))) {
                event.addTableReference(PortalFluidRoot.res("injects/" + loot));
            }
        }
    }

}
