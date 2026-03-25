package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.DimensionalTearsRoot;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.sounds.SoundEvent;

public interface ModSoundEvents {

    RegSupplier<SoundEvent> APPLY_EFFECT_RIFTING = registerSoundEvent("event.mob_effect.rifting");
    RegSupplier<SoundEvent> BOAT_PADDLE_DIMENSIONAL_TEARS = registerSoundEvent("entity.boat.paddle_dimensional_tears");
    RegSupplier<SoundEvent> BOTTLE_EMPTY_DIMENSIONAL_TEARS = registerSoundEvent("item.bottle.empty_dimensional_tears");
    RegSupplier<SoundEvent> BOTTLE_FILL_DIMENSIONAL_TEARS = registerSoundEvent("item.bottle.fill_dimensional_tears");
    RegSupplier<SoundEvent> BUCKET_EMPTY_DIMENSIONAL_TEARS = registerSoundEvent("item.bucket.empty_dimensional_tears");
    RegSupplier<SoundEvent> BUCKET_FILL_DIMENSIONAL_TEARS = registerSoundEvent("item.bucket.fill_dimensional_tears");
    RegSupplier<SoundEvent> DIMENSIONAL_TEARS_AMBIENT = registerSoundEvent("block.dimensional_tears.ambient");
    RegSupplier<SoundEvent> DIMENSIONAL_TEARS_BOTTLE_DRINK = registerSoundEvent("item.dimensional_tears_bottle.drink");
    RegSupplier<SoundEvent> DIMENSIONAL_TEARS_TELEPORT = registerSoundEvent("block.dimensional_tears.teleport");
    RegSupplier<SoundEvent> GENERIC_EXTINGUISH_RIFTING = registerSoundEvent("entity.generic.extinguish_rifting");
    RegSupplier<SoundEvent> PORTAL_DESTROY = registerSoundEvent("block.portal.destroy");
    RegSupplier<SoundEvent> PORTAL_SPAWN = registerSoundEvent("block.portal.spawn");
    RegSupplier<SoundEvent> WATER_FREEZE = registerSoundEvent("block.water.freeze");

    private static RegSupplier<SoundEvent> registerSoundEvent(String path) {
        return RegHelper.registerSound(DimensionalTearsRoot.res(path));
    }

    static void init() {}

}
