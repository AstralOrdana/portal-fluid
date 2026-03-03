package com.ordana.portal_fluid.reg;

import com.ordana.portal_fluid.PortalFluidRoot;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSoundEvents {

    public static final RegSupplier<SoundEvent> PORTAL_SPAWN = registerSoundEvent("block.portal.spawn");
    public static final RegSupplier<SoundEvent> PORTAL_FLUID_AMBIENT = registerSoundEvent("block.portal_fluid.ambient");
    public static final RegSupplier<SoundEvent> PORTAL_FLUID_TELEPORT = registerSoundEvent("block.portal_fluid.teleport");
    public static final RegSupplier<SoundEvent> BUCKET_EMPTY_PORTAL_FLUID = registerSoundEvent("item.bucket.empty_portal_fluid");
    public static final RegSupplier<SoundEvent> BUCKET_FILL_PORTAL_FLUID = registerSoundEvent("item.bucket.fill_portal_fluid");
    public static final RegSupplier<SoundEvent> BOTTLE_EMPTY_PORTAL_FLUID = registerSoundEvent("item.bottle.empty_portal_fluid");
    public static final RegSupplier<SoundEvent> BOTTLE_FILL_PORTAL_FLUID = registerSoundEvent("item.bottle.fill_portal_fluid");
    public static final RegSupplier<SoundEvent> BOAT_PADDLE_PORTAL_FLUID = registerSoundEvent("entity.boat.paddle_portal_fluid");
    public static final RegSupplier<SoundEvent> APPLY_EFFECT_RIFTING = registerSoundEvent("event.mob_effect.rifting");
    public static final RegSupplier<SoundEvent> PORTAL_FLUID_BOTTLE_DRINK = registerSoundEvent("item.portal_fluid_bottle.drink");

    private static RegSupplier<SoundEvent> registerSoundEvent(String path) {
        ResourceLocation id = PortalFluidRoot.res(path);
        return RegHelper.registerSound(id);
    }

    public static void init() {}

}
