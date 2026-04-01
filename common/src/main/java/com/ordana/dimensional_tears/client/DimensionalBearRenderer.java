package com.ordana.dimensional_tears.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ordana.dimensional_tears.DimensionalTearsClient;
import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.entity.DimensionalBear;
import net.minecraft.client.model.PolarBearModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class DimensionalBearRenderer extends MobRenderer<DimensionalBear, PolarBearModel<DimensionalBear>> {
	private static final ResourceLocation BEAR_LOCATION = DimensionalTearsRoot.res("textures/entity/bear/dimensionalbear.png");

	public DimensionalBearRenderer(EntityRendererProvider.Context context) {
		super(context, new PolarBearModel<>(context.bakeLayer(DimensionalTearsClient.DIMENSIONAL_BEAR)), 0.9F);
	}

	@Override
	public ResourceLocation getTextureLocation(DimensionalBear polarBear) {
		return BEAR_LOCATION;
	}

	@Override
	protected void scale(DimensionalBear polarBear, PoseStack poseStack, float f) {
		poseStack.scale(1.2F, 1.2F, 1.2F);
		super.scale(polarBear, poseStack, f);
	}
}

