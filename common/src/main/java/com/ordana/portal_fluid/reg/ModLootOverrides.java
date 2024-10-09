package com.ordana.portal_fluid.reg;

import com.google.gson.JsonElement;
import com.ordana.portal_fluid.PortalFluidRoot;
import com.ordana.portal_fluid.configs.CommonConfigs;
import net.mehvahdjukaar.moonlight.api.resources.RPUtils;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynServerResourcesGenerator;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicDataPack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ModLootOverrides extends DynServerResourcesGenerator {

    public static final ModLootOverrides INSTANCE = new ModLootOverrides();

    public ModLootOverrides() {
        super(new DynamicDataPack(PortalFluidRoot.res("generated_pack"), Pack.Position.TOP, true, true));
        this.dynamicPack.setGenerateDebugResources(false);
        this.dynamicPack.addNamespaces("minecraft");
    }

    @Override
    public Logger getLogger() {
        return PortalFluidRoot.LOGGER;
    }

    @Override
    public boolean dependsOnLoadedPacks() {
        return true;
    }

    public void overrideDataFile(ResourceManager manager, List list, String targetNamespace, String targetPath, String sourcePath, ResType resType) {
        for (var recipe : list) {
            ResourceLocation target = new ResourceLocation(targetNamespace, targetPath + recipe);
            ResourceLocation source = new ResourceLocation("portal_fluid", sourcePath + recipe + ".json");

            try (var bsStream = manager.getResource(source).orElseThrow().open()) {
                JsonElement bsElement = RPUtils.deserializeJson(bsStream);
                dynamicPack.addJson(target, bsElement, resType);

            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void regenerateDynamicAssets(ResourceManager manager) {

        var vanillaLootPiglins = List.of(
                "piglin_bartering"
        );

        if (CommonConfigs.PIGLINS_GIVE_CRYING_OBSIDIAN.get()) {

            //make piglins not give you crying obsidian as barter loot
            overrideDataFile(manager, vanillaLootPiglins,
                    "minecraft", "gameplay/",
                    "overrides/loot_tables/", ResType.LOOT_TABLES);
        }
    }
}