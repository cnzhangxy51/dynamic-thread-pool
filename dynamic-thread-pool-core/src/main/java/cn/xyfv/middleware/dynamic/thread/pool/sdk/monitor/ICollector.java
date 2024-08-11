package cn.xyfv.middleware.dynamic.thread.pool.sdk.monitor;

import cn.xyfv.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;

import java.util.List;

/**
 * 线程池监控中心接口
 * <p>2024-08-07 14:18</p>
 *
 * @author cnzhangxy
 * @since 1.0.0
 **/
public interface ICollector {
    /**
     * 上报数据
     * <p>Date: 2024/8/7</p>
     *
     * @author zhangxy
     * @since 1.0.0
     */
    void collect();

    /**
     * 获得数据
     * <p>Date: 2024/8/7</p>
     *
     * @return threadPoolConfigEntities 线程池配置参数
     * @author zhangxy
     * @since 1.0.0
     */
    List<ThreadPoolConfigEntity> getData();
}
