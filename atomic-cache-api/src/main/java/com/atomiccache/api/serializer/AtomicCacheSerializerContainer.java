package com.atomiccache.api.serializer;

import org.checkerframework.checker.nullness.qual.NonNull;

import static java.util.Objects.requireNonNull;

public class AtomicCacheSerializerContainer<K, V> implements IAtomicCacheSerializer<K, V> {

    private final IAtomicCacheKeySerializer<K>     keySerializer;
    private final IAtomicCacheValueDeserializer<V> valueDeserializer;
    private final IAtomicCacheValueSerializer<V>   valueSerializer;

    public AtomicCacheSerializerContainer(IAtomicCacheKeySerializer<K> keySerializer, IAtomicCacheValueDeserializer<V> valueDeserializer, IAtomicCacheValueSerializer<V> valueSerializer) {
        this.keySerializer     = requireNonNull(keySerializer, "No KeySerializer provided");
        this.valueDeserializer = requireNonNull(valueDeserializer, "No ValueDeserializer provided");
        this.valueSerializer   = requireNonNull(valueSerializer, " No ValueSerializer provided");
    }

    @Override
    public @NonNull String serializeKey(@NonNull K key) {
        return keySerializer.serializeKey(key);
    }

    @Override
    public @NonNull V deserializeValueFromText(@NonNull String aText) {
        return valueDeserializer.deserializeValueFromText(aText);
    }

    @Override
    public @NonNull String serializeValue(@NonNull V aValue) {
        return valueSerializer.serializeValue(aValue);
    }
}
