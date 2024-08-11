package cn.xyfv.middleware.dynamic.thread.pool.sdk.notifier.webhook;

import cn.xyfv.middleware.dynamic.thread.pool.sdk.config.DynamicThreadPoolAutoProperties;
import cn.xyfv.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import cn.xyfv.middleware.dynamic.thread.pool.sdk.notifier.AbstractNotifier;
import cn.xyfv.middleware.dynamic.thread.pool.sdk.notifier.INotifier;
import cn.xyfv.middleware.dynamic.thread.pool.sdk.notifier.message.NotifyMessageTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * WEBHOOK 告警
 * <p>2024-08-09 15:39</p>
 *
 * @author cnzhangxy
 * @since 1.0.0
 **/
public abstract class AbstractWebhookNotifier extends AbstractNotifier implements INotifier {
    @Resource
    public DynamicThreadPoolAutoProperties dynamicThreadPoolAutoProperties;
    @Override
    public String getNotifyMessage(Map.Entry<ThreadPoolConfigEntity, Double> entry) {
        ThreadPoolConfigEntity threadPoolConfigEntity = entry.getKey();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);
        String message = String.format(NotifyMessageTemplate.ALARM_TEMPLATE,
                threadPoolConfigEntity.getAppName(),
                threadPoolConfigEntity.getThreadPoolName(),
                "队列容量告警",
                "0.8",
                entry.getValue(),
                threadPoolConfigEntity.getCorePoolSize(),
                threadPoolConfigEntity.getMaximumPoolSize(),
                threadPoolConfigEntity.getActiveCount(),
                threadPoolConfigEntity.getQueueType(),
                threadPoolConfigEntity.getQueueSize(),
                threadPoolConfigEntity.getRemainingCapacity(),
                formattedNow,
                dynamicThreadPoolAutoProperties.getConfig().getNotifier().getChannel().getMessages().getUser(),
                dynamicThreadPoolAutoProperties.getConfig().getNotifier().getChannel().getCron(),
                dynamicThreadPoolAutoProperties.getConfig().getNotifier().getChannel().getMessages().getExtend());
        return message;
    }

    @Override public String extendNotifyMessage(String before, Map<String, String> messages){
        String after = before;
        for (Map.Entry<String, String> message : messages.entrySet()) {
            after = after + message.getKey() + "：" + message.getValue()+" \n";
        }
        return after;
    }
}
