package com.nicat.realtimecollaborationdocumenteditor.configuration.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisRepositoryImpl<K, V> implements RedisRepository<K, V> {

    private final RedisTemplate<K, V> redisTemplate;

    @Override
    public Optional<V> findByKey(K key) {
        return Optional.ofNullable(redisTemplate
                .opsForValue()
                .get(key));
    }

    @Override
    public boolean removeByKey(K key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    @Override
    public void putByKey(
            K key,
            V data
    ) {
        redisTemplate
                .opsForValue()
                .set(
                        key,
                        data
                );
    }
}
