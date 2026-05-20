package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.DimensionalTearsRoot;
import com.ordana.dimensional_tears.effects.RiftingEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import java.util.function.Supplier;

public interface ModEffects {

    Holder<MobEffect> RIFTING = regEffect(
        "rifting",
        () -> new RiftingEffect(MobEffectCategory.NEUTRAL, 0x8308E4)
            .withSoundOnAdded(ModSoundEvents.APPLY_EFFECT_RIFTING.value())
    );

    static <T extends MobEffect> Holder<T> regEffect(String path, Supplier<T> effectSupplier) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, DimensionalTearsRoot.res(path), effectSupplier.get());
    }

    static void init() {}

}
