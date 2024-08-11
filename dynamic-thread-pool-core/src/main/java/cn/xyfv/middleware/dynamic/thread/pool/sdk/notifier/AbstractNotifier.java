package cn.xyfv.middleware.dynamic.thread.pool.sdk.notifier;

import cn.xyfv.middleware.dynamic.thread.pool.sdk.domain.IDynamicThreadPoolService;
import cn.xyfv.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 告警
 * <p>2024-08-06 19:45</p>
 *
 * @author cnzhangxy
 * @since 1.0.0
 **/
public abstract class AbstractNotifier implements INotifier {

    @Resource
    public IDynamicThreadPoolService dynamicThreadPoolService;

    public AbstractNotifier() {
    }

    /**
     * 队列容量超过阈值告警
     * <p>Date: 2024/8/6</p>
     *
     * @return alertThreadPoolConfigEntities 需要告警的线程池
     * @author zhangxy
     * @since 1.0.0
     */
    public Map<ThreadPoolConfigEntity, Double> checkCondition() {
        Map<ThreadPoolConfigEntity, Double> alertThreadPoolConfigEntities = new HashMap<>();
        List<ThreadPoolConfigEntity> threadPoolConfigEntities = dynamicThreadPoolService.queryThreadPoolList();
        if (threadPoolConfigEntities == null || threadPoolConfigEntities.size() == 0) {
            return null;
        }
        for (ThreadPoolConfigEntity threadPoolConfigEntity : threadPoolConfigEntities) {
            int queueSize = threadPoolConfigEntity.getQueueSize();
            int remainingCapacity = threadPoolConfigEntity.getRemainingCapacity();
            if (queueSize + remainingCapacity != 0) {
                double usageRatio = (double) queueSize / ((double) remainingCapacity + (double) queueSize);
                if (usageRatio > 0.8) {
                    alertThreadPoolConfigEntities.put(threadPoolConfigEntity, usageRatio);
                }
            }
        }
        return alertThreadPoolConfigEntities;
    }
}
