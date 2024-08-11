package cn.xyfv.middleware.dynamic.thread.pool.sdk.refresher;

import cn.xyfv.middleware.dynamic.thread.pool.sdk.domain.IDynamicThreadPoolService;
import cn.xyfv.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * 动态线程池监听变更
 * 使用方法：继承并调用 refresh 方法
 *
 * @author cnzhangxy
 * @since 2024-08-02 00:39
 **/
public abstract class AbstractRefresher implements IRefresher{

    @Resource
    private  IDynamicThreadPoolService dynamicThreadPoolService;

    public AbstractRefresher(){}

    public void refresh(ThreadPoolConfigEntity threadPoolConfigEntity) {
        dynamicThreadPoolService.updateThreadPoolConfig(threadPoolConfigEntity);
    }

}
