/*FIXME
package com.ordana.dimensional_tears.reg;

import com.google.gson.JsonElement;
import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.configs.CommonConfigs;
import net.mehvahdjukaar.moonlight.api.resources.RPUtils;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.List;

@SuppressWarnings("removal")
public class ModLootOverrides extends net.mehvahdjukaar.moonlight.api.resources.pack.DynServerResourcesGenerator {

    public static final ModLootOverrides INSTANCE = new ModLootOverrides();

    public ModLootOverrides() {
        super(new net.mehvahdjukaar.moonlight.api.resources.pack.DynamicDataPack(DimensionalTearsRoot.res("generated_pack"), Pack.Position.TOP, true, true));
        this.dynamicPack.setGenerateDebugResources(false);
        this.dynamicPack.addNamespaces("minecraft");
    }

    @Override
    public Logger getLogger() {
        return DimensionalTearsRoot.LOGGER;
    }

//    @Override
//    public boolean dependsOnLoadedPacks() {
//        return true;
//    }

    public void overrideDataFile(ResourceManager manager, List<String> recipePaths, String targetNamespace, String targetPath, String sourcePath, ResType resType) {
        for (String recipePath : recipePaths) {
            Identifier target = Identifier.fromNamespaceAndPath(targetNamespace, recipePath).withPrefix(targetPath);
            Identifier source = ResType.JSON.getPath(DimensionalTearsRoot.res(recipePath).withPrefix(sourcePath));

            try (InputStream inputStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(inputStream);
                this.dynamicPack.addJson(target, bsElement, resType);
            }
            catch (Exception ignored) {}
        }
    }

    @Override
    public void regenerateDynamicAssets(ResourceManager manager) {
        if (!CommonConfigs.PIGLINS_GIVE_CRYING_OBSIDIAN.get())
            return;

        //make piglins not give you crying obsidian as barter loot
        this.overrideDataFile(
            manager,
            List.of("piglin_bartering"),
            "minecraft",
            "gameplay/",
            "overrides/loot_tables/",
            ResType.LOOT_TABLES
        );
    }

}

 */