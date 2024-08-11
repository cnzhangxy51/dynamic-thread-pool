package cn.xyfv.middleware.dynamic.thread.pool.sdk.notifier.message;

import cn.xyfv.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 告警信息生成
 * <p>2024-08-06 20:45</p>
 *
 * @author cnzhangxy
 * @since 1.0.0
 **/
public class NotifyMessageTemplate {
    // 定义告警信息模板
    public static final String ALARM_TEMPLATE =
            "服务名称：%s \n" +
                    "线程池名称：%s \n" +
                    "报警项：%s \n" +
                    "报警阈值：%s \n" +
                    "当前值：%s \n" +
                    "核心线程数：%s \n" +
                    "最大线程数：%s \n" +
                    "活跃线程数：%s \n" +
                    "队列类型：%s \n" +
                    "队列任务数量：%s \n" +
                    "队列剩余容量：%s \n" +
                    "报警时间：%s \n" +
                    "接收人：@%s \n" +
                    "报警间隔：%s \n" +
                    "扩展信息：%s \n";
}
