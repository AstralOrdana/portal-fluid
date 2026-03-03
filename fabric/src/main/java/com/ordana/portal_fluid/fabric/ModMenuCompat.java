package com.ordana.portal_fluid.fabric;

import com.ordana.portal_fluid.configs.CommonConfigs;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuCompat implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return ConfigOptionsScreen::new;
	}
}
