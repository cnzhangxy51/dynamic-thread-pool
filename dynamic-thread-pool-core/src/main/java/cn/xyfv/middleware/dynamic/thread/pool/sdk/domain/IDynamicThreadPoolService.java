package cn.xyfv.middleware.dynamic.thread.pool.sdk.domain;

import cn.xyfv.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;

import java.util.List;

/**
 * 动态线程池服务,用于获取应用中的线程池信息
 * @author cnzhangxy
 * @since 2024-05-30 20:12
 **/
public interface IDynamicThreadPoolService {
    /**
     * 查询所有线程池
     * @return 线程池列表
     * @author cnzhangxy
     * @since 2024/5/30
     */
    List<ThreadPoolConfigEntity> queryThreadPoolList();

    /**
     *  查询指定线程池的配置
     * @param threadPoolName 线程池名字
     * @return 线程池配置参数
     * @author cnzhangxy
     * @since 2024/5/30
     */
    ThreadPoolConfigEntity queryThreadPoolConfigByName(String threadPoolName);

    /**
     *  更新指定线程池配置
     * @param threadPoolConfigEntity 线程池参数配置
     * @author cnzhangxy
     * @since 2024/5/30
     */
    void updateThreadPoolConfig(ThreadPoolConfigEntity threadPoolConfigEntity);

}
