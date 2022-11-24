package config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.List;

@Configuration
@Slf4j
public class CaffeineConfig {
    public static final int DEFAULT_MAXSIZE = 5000;

    @Bean("rolePermissionListCache")
    public Cache<String, List<String>> rolePermissionListCache(){
        return Caffeine.newBuilder().maximumSize(DEFAULT_MAXSIZE).recordStats().build();
    }
}
