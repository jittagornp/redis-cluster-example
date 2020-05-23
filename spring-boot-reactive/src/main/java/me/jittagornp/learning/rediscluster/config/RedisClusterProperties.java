package me.jittagornp.learning.rediscluster.config;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author jitta
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.redis.cluster")
public class RedisClusterProperties {
    
    private String password;
    
    private List<String> nodes;
    
}
