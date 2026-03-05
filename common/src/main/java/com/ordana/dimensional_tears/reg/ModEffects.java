package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.effects.RiftingEffect;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import java.util.function.Supplier;

public interface ModEffects {

    RegSupplier<MobEffect> RIFTING = regEffect(
        "rifting",
        () -> new RiftingEffect(MobEffectCategory.NEUTRAL, 0x8308E4)
            .withSoundOnAdded(ModSoundEvents.APPLY_EFFECT_RIFTING.get())
    );

    static <T extends MobEffect> RegSupplier<T> regEffect(String path, Supplier<T> effectSupplier) {
        return RegHelper.registerEffect(DimensionalTearsRoot.res(path), effectSupplier);
    }

    static void init() {}

}
