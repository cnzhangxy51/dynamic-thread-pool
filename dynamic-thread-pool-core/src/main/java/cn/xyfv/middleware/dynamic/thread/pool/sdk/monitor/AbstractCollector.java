package cn.xyfv.middleware.dynamic.thread.pool.sdk.monitor;

import cn.xyfv.middleware.dynamic.thread.pool.sdk.domain.IDynamicThreadPoolService;
import cn.xyfv.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 线程池数据上报
 *
 * @author cnzhangxy
 * @since 2024-05-12 16:29
 */
public abstract class AbstractCollector implements ICollector {
    @Resource
    private IDynamicThreadPoolService dynamicThreadPoolService;

    @Override
    public List<ThreadPoolConfigEntity> getData() {
        List<ThreadPoolConfigEntity> threadPoolConfigEntities = dynamicThreadPoolService.queryThreadPoolList();
        return threadPoolConfigEntities;
    }
}
