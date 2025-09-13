package com.payneteasy.atomiccache.api.serializer.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.payneteasy.atomiccache.api.serializer.IAtomicCacheSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

public class AtomicCacheGsonSerializer<K, V> implements IAtomicCacheSerializer<K, V> {

    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    private final Class<V> valueType;

    public AtomicCacheGsonSerializer(Class<V> valueType) {
        this.valueType = valueType;
    }

    @Override
    public @NonNull String serializeKey(@NonNull Object key) {
        return GSON.toJson(key);
    }

    @Override
    public @NonNull V deserializeValueFromText(@NonNull String aText) {
        return GSON.fromJson(aText, valueType);
    }

    @Override
    public @NonNull String serializeValue(@NonNull V aValue) {
        return GSON.toJson(aValue);
    }
}
