package com.ordana.dimensional_tears.configs;

import com.ordana.dimensional_tears.DimensionalTearsRoot;
import folk.sisby.kaleido.api.WrappedConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.FloatRange;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.IntegerRange;
import net.minecraft.SharedConstants;

import java.util.function.Supplier;

public class CommonConfigs extends WrappedConfig {

    public Capabilities capabilities = new Capabilities();
    public static class Capabilities implements Section {
        public boolean FlINT_AND_STEEL_PORTAL_LIGHTING = true;
        public boolean DIMENSIONAL_TEARS_DRINKING = true;
        public boolean END_OCEAN_BUCKETABLE = false;
    }

    public Sounds sounds = new Sounds();
    public static class Sounds implements Section {
        public boolean PORTAL_CREATION_SOUND = true;
        public boolean PORTAL_DESTRUCTION_SOUND = true;
    }

    public Obtaining obtaining = new Obtaining();
    public static class Obtaining implements Section {
        public boolean CRYING_OBSIDIAN_DIMENSIONAL_TEARS = false;
        public boolean RESPAWN_ANCHOR_DIMENSIONAL_TEARS = true;
        @FloatRange(min =  0.0, max = 1.0)
        public double PORTAL_DESTRUCTION_CRYING_OBSIDIAN_CHANCE = 0.25;
        @FloatRange(min =  0.0, max = 1.0)
        public double LAVA_INTERACTION_CRYING_OBSIDIAN_CHANCE = 0.1;
        public boolean PIGLINS_GIVE_CRYING_OBSIDIAN = true;
    }


    public Fluids fluids = new Fluids();
    public static class Fluids implements Section {
        public boolean DIMENSIONAL_TEARS_OCEAN = false;
        public boolean DIMENSIONAL_TEARS_SOURCE_CONVERSION = false;
        @IntegerRange(min =  5, max = SharedConstants.TICKS_PER_SECOND * 10)
        public int FLUID_FLOWING_TICK_RATE = 10;
    }

    public Teleportation teleportation = new Teleportation();
    public static class Teleportation implements Section {
        @IntegerRange(min = 1, max = 300)
        public int RIFTING_DELAY_SECONDS = 10;
        public boolean FULLY_SUBMERGED_INSTANT_TELEPORT = true;
        @IntegerRange(min = 0, max = 32)
        public int SPAWN_BYPASS_INSTANT_TELEPORTATION_RANGE = 4;
    }

}
