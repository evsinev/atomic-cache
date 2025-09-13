package com.payneteasy.atomiccache.api;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface IAtomicCacheFetch<K, V> {

    @NonNull
    V fetchValue(@NonNull K key) throws Exception;

}
