package com.ordana.portal_fluid.fabric;

import com.ordana.portal_fluid.configs.ClientConfigs;
import com.ordana.portal_fluid.configs.CommonConfigs;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.net.URI;
import java.net.URISyntaxException;

public class ConfigOptionsScreen extends Screen {
	private final Screen parent;
	private static final Component TITLE = Component.literal("Dimensional Tears");
	private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 32, 32);

	protected ConfigOptionsScreen(Screen parent) {
		super(TITLE);
		this.parent = parent;
	}

	@Override
	protected void init() {

		this.addRenderableWidget(this.layout.addToHeader(new StringWidget(TITLE, this.font)));

		LinearLayout linearLayout = this.layout.addToContents(LinearLayout.vertical().spacing(6));

		if (FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3") || FabricLoader.getInstance().isModLoaded("cloth-config")) {
			linearLayout.addChild(Button.builder(Component.translatable("neoforge.configuration.uitext.common"), button -> Minecraft.getInstance().setScreen(CommonConfigs.SERVER_SPEC.makeScreen(this))).size(200, 20).build());
			linearLayout.addChild(Button.builder(Component.translatable("neoforge.configuration.uitext.client"), button -> Minecraft.getInstance().setScreen(ClientConfigs.CONFIG_SPEC.makeScreen(this))).size(200, 20).build());
		} else {
			linearLayout.addChild(new MultiLineTextWidget(Component.translatable("moonlight.configuration.no_library"), this.font).setMaxWidth(200));
			linearLayout.addChild(Button.builder(Component.translatable("moonlight.configuration.download_library"), button -> {
				try {
					Util.getPlatform().openUri(new URI("https://modrinth.com/mod/cloth-config"));
				} catch (URISyntaxException e) {
					button.setMessage(Component.literal("Unable to open Modrinth!"));
				}
			}).size(200, 20).build());

		}



		this.addRenderableWidget(this.layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).size(100, 20).build()));

		this.layout.visitWidgets(this::addRenderableWidget);
		this.layout.arrangeElements();
	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(this.parent);
	}
}
