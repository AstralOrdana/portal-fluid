package com.ordana.dimensional_tears.reg;

import com.ordana.dimensional_tears.PortalFluidRoot;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSoundEvents {

    public static final RegSupplier<SoundEvent> PORTAL_SPAWN = registerSoundEvent("block.portal.spawn");
    public static final RegSupplier<SoundEvent> DIMENSIONAL_TEARS_AMBIENT = registerSoundEvent("block.dimensional_tears.ambient");
    public static final RegSupplier<SoundEvent> DIMENSIONAL_TEARS_TELEPORT = registerSoundEvent("block.dimensional_tears.teleport");
    public static final RegSupplier<SoundEvent> BUCKET_EMPTY_DIMENSIONAL_TEARS = registerSoundEvent("item.bucket.empty_dimensional_tears");
    public static final RegSupplier<SoundEvent> BUCKET_FILL_DIMENSIONAL_TEARS = registerSoundEvent("item.bucket.fill_dimensional_tears");
    public static final RegSupplier<SoundEvent> BOTTLE_EMPTY_DIMENSIONAL_TEARS = registerSoundEvent("item.bottle.empty_dimensional_tears");
    public static final RegSupplier<SoundEvent> BOTTLE_FILL_DIMENSIONAL_TEARS = registerSoundEvent("item.bottle.fill_dimensional_tears");
    public static final RegSupplier<SoundEvent> BOAT_PADDLE_DIMENSIONAL_TEARS = registerSoundEvent("entity.boat.paddle_dimensional_tears");
    public static final RegSupplier<SoundEvent> APPLY_EFFECT_RIFTING = registerSoundEvent("event.mob_effect.rifting");
    public static final RegSupplier<SoundEvent> DIMENSIONAL_TEARS_BOTTLE_DRINK = registerSoundEvent("item.dimensional_tears_bottle.drink");

    private static RegSupplier<SoundEvent> registerSoundEvent(String path) {
        ResourceLocation id = PortalFluidRoot.res(path);
        return RegHelper.registerSound(id);
    }

    public static void init() {}

}
