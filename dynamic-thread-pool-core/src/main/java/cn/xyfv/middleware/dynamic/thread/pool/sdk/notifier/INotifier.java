package cn.xyfv.middleware.dynamic.thread.pool.sdk.notifier;

import cn.xyfv.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;

import java.util.List;
import java.util.Map;

/**
 * 告警顶层接口
 * <p>2024-08-06 17:57</p>
 *
 * @author cnzhangxy
 * @since 1.0.0
 **/
public interface INotifier {
    /**
     * 告警主要方法
     * <p>Date: 2024/8/7</p>
     *
     * @author zhangxy
     * @since 1.0.0
     */
    void notifier();

    /**
     * 检查条件
     * <p>Date: 2024/8/7</p>
     *
     * @return 需要告警的线程池列表和当前值
     * @author zhangxy
     * @since 1.0.0
     */
    Map<ThreadPoolConfigEntity, Double> checkCondition();

    /**
     * 获得告警信息
     * <p>Date: 2024/8/7</p>
     *
     * @return 告警信息
     * @author zhangxy
     * @since 1.0.0
     */
    String getNotifyMessage(Map.Entry<ThreadPoolConfigEntity, Double> entry);

    /**
     * 拓展告警信息
     * <p>Date: 2024/8/9</p>
     *
     * @param before 拓展前的告警信息
     * @param messages 需要加入的告警信息
     * @return 拓展后的告警信息
     * @author zhangxy
     * @since 1.0.0
     */
    String extendNotifyMessage(String before, Map<String, String> messages);

}
