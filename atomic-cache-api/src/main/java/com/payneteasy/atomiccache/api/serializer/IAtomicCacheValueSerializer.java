package com.payneteasy.atomiccache.api.serializer;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface IAtomicCacheValueSerializer<P> {

    @NonNull
    String serializeValue(@NonNull P aValue);
}
