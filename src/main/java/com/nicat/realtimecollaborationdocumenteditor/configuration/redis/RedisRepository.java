package com.nicat.realtimecollaborationdocumenteditor.configuration.redis;

import java.time.Duration;
import java.util.Optional;

public interface RedisRepository<K, V> {

    Optional<V> findByKey(K key);

    boolean removeByKey(K key);

    void putByKey(K key, V data);
}
