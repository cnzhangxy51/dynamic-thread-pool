package cn.xyfv.middleware.dynamic.thread.pool.sdk.config;

import cn.xyfv.middleware.dynamic.thread.pool.sdk.domain.DynamicThreadPoolService;
import cn.xyfv.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import cn.xyfv.middleware.dynamic.thread.pool.sdk.domain.model.valobj.RegistryEnumVO;
import cn.xyfv.middleware.dynamic.thread.pool.sdk.monitor.ICollector;
import cn.xyfv.middleware.dynamic.thread.pool.sdk.notifier.INotifier;
import cn.xyfv.middleware.dynamic.thread.pool.sdk.refresher.IRefresher;
import com.alibaba.fastjson2.JSON;
import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 动态配置入口
 *
 * @author cnzhangxy
 * @since 2024-05-30 20:23
 **/

@Configuration
@EnableConfigurationProperties(DynamicThreadPoolAutoProperties.class)
public class DynamicThreadPoolAutoConfig {
    private final Logger logger = LoggerFactory.getLogger(DynamicThreadPoolAutoConfig.class);
    private String applicationName;

    @Bean("dynamicThreadPoolService")
    public DynamicThreadPoolService dynamicThreadPoolService(ApplicationContext applicationContext, Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {
        applicationName = applicationContext.getEnvironment().getProperty("spring.application.name");

        if (StringUtils.isBlank(applicationName)) {
            applicationName = "缺省的";
            logger.warn("动态线程池，启动提示。SpringBoot 应用未配置 spring.application.name 无法获取到应用名称！");
        }

        logger.info("线程池信息: {}", JSON.toJSONString(threadPoolExecutorMap.keySet()));

        return new DynamicThreadPoolService(applicationName, threadPoolExecutorMap);
    }

    @Bean
    @ConditionalOnProperty(prefix = "dynamic.thread.pool.config.notifier", name = "enabled", havingValue = "true")
    public INotifier notifier() {
        try {
            List<INotifier> notifiers = new ArrayList<>();
            ServiceLoader<INotifier> serviceLoader = ServiceLoader.load(INotifier.class);
            for (INotifier notifier : serviceLoader) {
                notifiers.add(notifier);
            }
            return notifiers.get(0);
        } catch (Exception e) {
            logger.warn("未指定告警中心，如需要请检查 META-INF/services 配置或配置文件中 dynamic.thread.pool.config.notifier 是否正确");
            return null;
        }
    }

    @Bean
    @ConditionalOnProperty(prefix = "dynamic.thread.pool.config.monitor", name = "enabled", havingValue = "true")
    public ICollector collector() {
        try {
            List<ICollector> collectors = new ArrayList<>();
            ServiceLoader<ICollector> serviceLoader = ServiceLoader.load(ICollector.class);
            for (ICollector collector : serviceLoader) {
                collectors.add(collector);
            }
            return collectors.get(0);
        } catch (Exception e) {
            logger.warn("未指定监控中心，如需要请检查 META-INF/services 配置或配置文件中 dynamic.thread.pool.config.monitor 是否正确");
            return null;
        }
    }

    @Bean
    @ConditionalOnProperty(prefix = "dynamic.thread.pool.config.refresher", name = "enabled", havingValue = "true")
    public IRefresher refresher() {
        try {
            List<IRefresher> refreshers = new ArrayList<>();
            ServiceLoader<IRefresher> serviceLoader = ServiceLoader.load(IRefresher.class);
            for (IRefresher refresher : serviceLoader) {
                refreshers.add(refresher);
            }
            return refreshers.get(0);
        } catch (Exception e) {
            logger.warn("未指定配置中心，如需要请检查 META-INF/services 配置或配置文件中 dynamic.thread.pool.config.refresher 是否正确");
            return null;
        }
    }


    @Bean(name = "dynamicThreadPoolRedisTopic")
    @ConditionalOnExpression("#{${dynamic.thread.pool.config.refresher.enabled} " +
            "and '${dynamic.thread.pool.config.refresher.channel.platform}' == 'redis'}")
    public RTopic redisRefresher(RedissonClient redissonClient, IRefresher redisRefresher) {
        RTopic topic = redissonClient.getTopic(RegistryEnumVO.DYNAMIC_THREAD_POOL_REDIS_TOPIC.getKey() + "_" + applicationName);
        topic.addListener(ThreadPoolConfigEntity.class, (MessageListener<ThreadPoolConfigEntity>) redisRefresher);
        return topic;
    }

    @Bean("redissonClient")
    @ConditionalOnProperty(prefix = "dynamic.thread.pool.config.redis", name = "enabled", havingValue = "true")
    public RedissonClient redissonClient(DynamicThreadPoolAutoProperties properties) {
        Config config = new Config();
        // 根据需要可以设定编解码器；https://github.com/redisson/redisson/wiki/4.-%E6%95%B0%E6%8D%AE%E5%BA%8F%E5%88%97%E5%8C%96
        config.setCodec(JsonJacksonCodec.INSTANCE);
        config.useSingleServer()
                .setAddress("redis://" + properties.getConfig().getRedis().getHost() + ":" + properties.getConfig().getRedis().getPort())
                .setPassword(properties.getConfig().getRedis().getPassword())
                .setConnectionPoolSize(properties.getConfig().getRedis().getPoolSize())
                .setConnectionMinimumIdleSize(properties.getConfig().getRedis().getMinIdleSize())
                .setIdleConnectionTimeout(properties.getConfig().getRedis().getIdleTimeout())
                .setConnectTimeout(properties.getConfig().getRedis().getConnectTimeout())
                .setRetryAttempts(properties.getConfig().getRedis().getRetryAttempts())
                .setRetryInterval(properties.getConfig().getRedis().getRetryInterval())
                .setPingConnectionInterval(properties.getConfig().getRedis().getPingInterval())
                .setKeepAlive(properties.getConfig().getRedis().isKeepAlive())
        ;

        RedissonClient redissonClient = Redisson.create(config);

        logger.info("动态线程池，注册器（redis）链接初始化完成。{} {} {}", properties.getConfig().getRedis().getHost(),
                properties.getConfig().getRedis().getPoolSize(), !redissonClient.isShutdown());

        return redissonClient;
    }

}