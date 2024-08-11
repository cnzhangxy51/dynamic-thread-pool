package cn.xyfv.middleware.dynamic.thread.pool.sdk.refresher.impl;

import cn.xyfv.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import cn.xyfv.middleware.dynamic.thread.pool.sdk.refresher.AbstractRefresher;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * redis 动态线程池监听变更
 *
 * @author cnzhangxy
 * @since 2024-08-02 00:39
 **/
public class RedisRefresher extends AbstractRefresher implements MessageListener<ThreadPoolConfigEntity> {

    private final Logger logger = LoggerFactory.getLogger(RedisRefresher.class);

    @Override
    public void onMessage(CharSequence charSequence, ThreadPoolConfigEntity threadPoolConfigEntity) {
        logger.info("动态线程池，调整线程池配置。线程池名称:{} 核心线程数:{} 最大线程数:{}", threadPoolConfigEntity.getThreadPoolName(),
                threadPoolConfigEntity.getCorePoolSize(), threadPoolConfigEntity.getMaximumPoolSize());
        refresh(threadPoolConfigEntity);
    }
}
