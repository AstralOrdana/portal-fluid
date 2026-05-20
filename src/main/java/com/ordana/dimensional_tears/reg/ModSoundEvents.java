package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.DimensionalTearsRoot;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

public interface ModSoundEvents {

    Holder<SoundEvent> APPLY_EFFECT_RIFTING = registerSoundEvent("event.mob_effect.rifting");
    Holder<SoundEvent> BOAT_PADDLE_DIMENSIONAL_TEARS = registerSoundEvent("entity.boat.paddle_dimensional_tears");
    Holder<SoundEvent> BOTTLE_EMPTY_DIMENSIONAL_TEARS = registerSoundEvent("item.bottle.empty_dimensional_tears");
    Holder<SoundEvent> BOTTLE_FILL_DIMENSIONAL_TEARS = registerSoundEvent("item.bottle.fill_dimensional_tears");
    Holder<SoundEvent> BUCKET_EMPTY_DIMENSIONAL_TEARS = registerSoundEvent("item.bucket.empty_dimensional_tears");
    Holder<SoundEvent> BUCKET_FILL_DIMENSIONAL_TEARS = registerSoundEvent("item.bucket.fill_dimensional_tears");
    Holder<SoundEvent> DIMENSIONAL_TEARS_AMBIENT = registerSoundEvent("block.dimensional_tears.ambient");
    Holder<SoundEvent> DIMENSIONAL_TEARS_BOTTLE_DRINK = registerSoundEvent("item.dimensional_tears_bottle.drink");
    Holder<SoundEvent> DIMENSIONAL_TEARS_TELEPORT = registerSoundEvent("block.dimensional_tears.teleport");
    Holder<SoundEvent> GENERIC_EXTINGUISH_RIFTING = registerSoundEvent("entity.generic.extinguish_rifting");
    Holder<SoundEvent> PORTAL_DESTROY = registerSoundEvent("block.portal.destroy");
    Holder<SoundEvent> PORTAL_SPAWN = registerSoundEvent("block.portal.spawn");
    Holder<SoundEvent> WATER_FREEZE = registerSoundEvent("block.water.freeze");

    private static Holder<SoundEvent> registerSoundEvent(String path) {
        return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, DimensionalTearsRoot.res(path), SoundEvent.createVariableRangeEvent(DimensionalTearsRoot.res(path)));
    }

    static void init() {}

}
