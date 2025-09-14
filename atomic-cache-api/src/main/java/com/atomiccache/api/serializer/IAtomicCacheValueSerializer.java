package com.atomiccache.api.serializer;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface IAtomicCacheValueSerializer<V> {

    @NonNull
    String serializeValue(@NonNull V aValue);
}
