package cn.xyfv.middleware.dynamic.thread.pool.sdk.refresher;

import cn.xyfv.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;

/**
 * 线程池配置中心接口
 * <p>2024-08-07 14:17</p>
 *
 * @author cnzhangxy
 * @since 1.0.0
 **/
public interface IRefresher {
    void refresh(ThreadPoolConfigEntity threadPoolConfigEntity);
}
