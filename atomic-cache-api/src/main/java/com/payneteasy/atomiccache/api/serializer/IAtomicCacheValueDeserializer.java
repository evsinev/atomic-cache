package com.payneteasy.atomiccache.api.serializer;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface IAtomicCacheValueDeserializer<P> {
    @NonNull
    P deserializeValueFromText(@NonNull String aText);
}
