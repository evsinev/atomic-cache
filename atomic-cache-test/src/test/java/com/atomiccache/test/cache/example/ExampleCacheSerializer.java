package com.atomiccache.test.cache.example;

import com.atomiccache.api.serializer.IAtomicCacheSerializer;
import com.atomiccache.test.cache.example.model.ExampleKey;
import com.atomiccache.test.cache.example.model.ExampleValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ExampleCacheSerializer implements IAtomicCacheSerializer<ExampleKey, ExampleValue> {

    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    @Override
    public @NonNull String serializeKey(@NonNull ExampleKey key) {
        return key.getId() + "/" + key.getType();
    }

    @Override
    public @NonNull ExampleValue deserializeValueFromText(@NonNull String aText) {
        return GSON.fromJson(aText, ExampleValue.class);
    }

    @Override
    public @NonNull String serializeValue(@NonNull ExampleValue aValue) {
        return GSON.toJson(aValue);
    }
}
