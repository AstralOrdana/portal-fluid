package com.ordana.portal_fluid.reg;

import com.ordana.portal_fluid.PortalFluidRoot;
import com.ordana.portal_fluid.effects.RiftingEffect;
import com.ordana.portal_fluid.items.PortalFluidBucketItem;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class ModEffects {
    public static void init() {
    }

    public static <T extends MobEffect> Supplier<T> regEffect(String name, Supplier<T> sup) {
        return RegHelper.registerEffect(PortalFluidRoot.res(name), sup);
    }

    public static final Supplier<MobEffect> RIFTING = regEffect("rifting", () -> new RiftingEffect(MobEffectCategory.NEUTRAL, 8587492).withSoundOnAdded(SoundEvents.APPLY_EFFECT_TRIAL_OMEN));


}
