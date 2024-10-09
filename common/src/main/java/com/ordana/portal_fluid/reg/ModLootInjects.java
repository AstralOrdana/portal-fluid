package com.ordana.portal_fluid.reg;

import com.ordana.portal_fluid.PortalFluidRoot;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ModLootInjects {

    private static final List<String> lootChests = List.of(
            "ruined_portal"
    );

    public static void onLootInject(RegHelper.LootInjectEvent event) {

        ResourceLocation name = event.getTable();

        for (var loot : lootChests) {
            if (name.equals(new ResourceLocation("minecraft", "chests/" + loot))) {
                event.addTableReference(PortalFluidRoot.res("injects/" + loot));
            }
        }

    }
}
