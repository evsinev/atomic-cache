package com.atomiccache.api.serializer;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface IAtomicCacheValueDeserializer<V> {
    @NonNull
    V deserializeValueFromText(@NonNull String aText);
}
