package com.ordana.portal_fluid.reg;

import com.ordana.portal_fluid.PortalFluidRoot;
import com.ordana.portal_fluid.effects.RiftingEffect;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import java.util.function.Supplier;

public class ModEffects {

    public static final RegSupplier<MobEffect> RIFTING = regEffect(
        "rifting",
        () -> new RiftingEffect(MobEffectCategory.NEUTRAL, 0x8308E4)
            .withSoundOnAdded(ModSoundEvents.APPLY_EFFECT_RIFTING.get())
    );

    public static <T extends MobEffect> RegSupplier<T> regEffect(String name, Supplier<T> sup) {
        return RegHelper.registerEffect(PortalFluidRoot.res(name), sup);
    }

    public static void init() {}


}
