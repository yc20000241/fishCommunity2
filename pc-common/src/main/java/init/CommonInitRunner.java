package init;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import service.CacheService;


@Component
@Slf4j
public class CommonInitRunner implements ApplicationRunner {

    @Autowired
    private CacheService cacheService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("开始加载数据到缓存中");
        cacheService.initRolePermissionList();
        log.info("加载数据加载完成....");
        log.info("加载参数到缓存");

    }
}
