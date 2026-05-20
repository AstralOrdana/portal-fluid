package com.ordana.dimensional_tears.reg;

import net.minecraft.resources.Identifier;

public record RegSupplier<T>(Identifier id, T entry) {
    public T get() {
        return entry;
    }
    public T value() {
        return entry;
    }
}
