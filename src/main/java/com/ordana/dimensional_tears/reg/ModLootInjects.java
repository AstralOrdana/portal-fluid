/*FIXME
package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.DimensionalTearsRoot;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.resources.Identifier;

import java.util.List;

public class ModLootInjects {

    private static final List<String> lootChests = List.of("ruined_portal");

    public static void onLootInject(RegHelper.LootInjectEvent event) {
        Identifier name = event.getTable();

        for (String loot : lootChests) {
            if (name.equals(Identifier.withDefaultNamespace("chests/" + loot)))
                event.addTableReference(DimensionalTearsRoot.res(loot).withPrefix("injects/"));
        }
    }

}


 */