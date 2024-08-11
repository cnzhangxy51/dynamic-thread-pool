package cn.xyfv.middleware.dynamic.thread.pool.sdk.monitor.impl;

import cn.xyfv.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import cn.xyfv.middleware.dynamic.thread.pool.sdk.domain.model.valobj.RegistryEnumVO;
import cn.xyfv.middleware.dynamic.thread.pool.sdk.monitor.AbstractCollector;
import com.alibaba.fastjson.JSON;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;

/**
 * redis 监控中心
 * <p>2024-08-06 17:10</p>
 *
 * @author cnzhangxy
 * @since 1.0.0
 **/
public class RedisCollector extends AbstractCollector {
    private final Logger logger = LoggerFactory.getLogger(RedisCollector.class);

    @Resource
    private RedissonClient redissonClient;

    public void reportThreadPool(List<ThreadPoolConfigEntity> threadPoolEntities) {
        RList<ThreadPoolConfigEntity> list = redissonClient.getList(RegistryEnumVO.THREAD_POOL_CONFIG_LIST_KEY.getKey());
        list.delete();
        list.addAll(threadPoolEntities);
    }

    public void reportThreadPoolConfigParameter(ThreadPoolConfigEntity threadPoolConfigEntity) {
        String cacheKey = RegistryEnumVO.THREAD_POOL_CONFIG_PARAMETER_LIST_KEY.getKey() + "_" + threadPoolConfigEntity.getAppName() + "_" + threadPoolConfigEntity.getThreadPoolName();
        RBucket<ThreadPoolConfigEntity> bucket = redissonClient.getBucket(cacheKey);
        // 过期时间设为三十天
        bucket.set(threadPoolConfigEntity, Duration.ofDays(30));
    }


    @Scheduled(cron = "${dynamic.thread.pool.config.monitor.channel.cron}")
    @Override
    public void collect() {
        List<ThreadPoolConfigEntity> threadPoolConfigEntities = getData();
        reportThreadPool(threadPoolConfigEntities);
        logger.info("动态线程池，上报线程池列表信息：{}", JSON.toJSONString(threadPoolConfigEntities));

        for (ThreadPoolConfigEntity threadPoolConfigEntity : threadPoolConfigEntities) {
            reportThreadPoolConfigParameter(threadPoolConfigEntity);
            logger.info("动态线程池，上报线程池配置：{}", JSON.toJSONString(threadPoolConfigEntity));
        }
    }

}
