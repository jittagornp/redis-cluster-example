package me.jittagornp.learning.rediscluster.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.jittagornp.learning.rediscluster.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 *
 * @author jitta
 */
@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisClusterProperties clusterProperties;

    @Bean
    @Primary
    public ReactiveRedisConnectionFactory connectionFactory() {
        final RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(clusterProperties.getNodes());
        redisClusterConfiguration.setPassword(RedisPassword.of(clusterProperties.getPassword()));
        return new LettuceConnectionFactory(redisClusterConfiguration);
    }

    private <T extends Object> ReactiveRedisOperations<String, T> redisOperations(
            final ReactiveRedisConnectionFactory factory,
            final RedisSerializer<T> valueSerializer
    ) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        return new ReactiveRedisTemplate<>(factory,
                RedisSerializationContext.<String, T>newSerializationContext()
                        .key(keySerializer)
                        .value(valueSerializer)
                        .hashKey(keySerializer)
                        .hashValue(valueSerializer)
                        .build()
        );
    }

    private <T extends Object> ReactiveRedisOperations<String, T> jsonRedisOperations(
            final ReactiveRedisConnectionFactory factory,
            final ObjectMapper objectMapper,
            final Class<T> typeClass
    ) {
        final Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<>(typeClass);
        serializer.setObjectMapper(objectMapper);
        return redisOperations(factory, serializer);
    }

    @Bean
    public ReactiveRedisOperations<String, Object> defaultRedisOperations(
            final ReactiveRedisConnectionFactory factory
    ) {
        return redisOperations(factory, new JdkSerializationRedisSerializer(getClass().getClassLoader()));
    }

    @Bean
    public ReactiveRedisOperations<String, User> userRedisOperations(
            final ObjectMapper objectMapper,
            final ReactiveRedisConnectionFactory factory
    ) {
        return jsonRedisOperations(factory, objectMapper, User.class);
    }
}
