package com.ordana.dimensional_tears.configs;

import folk.sisby.kaleido.api.WrappedConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.FloatRange;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.IntegerRange;

public class ClientConfigs extends WrappedConfig {

    public Particles particles = new Particles();
    public static class Particles implements WrappedConfig.Section {
        @FloatRange(min =  0.0f, max = 1.0f)
        public double AMBIENT_PARTICLE_CHANCE = 0.05;
        @IntegerRange(min = 0, max = 50)
        public int EFFECT_PARTICLE_DENSITY = 10;
        @FloatRange(min = 0.0f, max = 2.0f)
        public double EFFECT_PARTICLE_RADIUS = 1.65;
    }

    public Sounds sounds = new Sounds();
    public static class Sounds implements WrappedConfig.Section {
        @FloatRange(min = 0.0f, max = 1.0f)
        public double AMBIENT_SOUND_CHANCE = 0.005;
        @FloatRange(min = 0.0f, max = 1.0f)
        public double MAX_SOUND_PITCH_DEVIATION = 0.1;
        @FloatRange(min = 0.0f, max = 32.0f)
        public double MIN_SOUND_ATTENUATION_DIST = 4;
        @IntegerRange(min = 1, max = 10)
        public int MAX_SOUND_ATTENUATION_DIST_MULTIPLIER = 4;
    }
}