package cn.xyfv.middleware.dynamic.thread.pool.sdk.notifier.webhook.impl;

import cn.xyfv.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import cn.xyfv.middleware.dynamic.thread.pool.sdk.notifier.webhook.AbstractWebhookNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * <p>2024-08-09 15:59</p>
 *
 * @author cnzhangxy
 * @since 1.0.0
 **/
public class WechatNotifier extends AbstractWebhookNotifier {
    private final Logger logger = LoggerFactory.getLogger(WechatNotifier.class);

    @Scheduled(cron = "${dynamic.thread.pool.config.notifier.channel.cron}")
    @Override
    public void notifier() {
        Map<ThreadPoolConfigEntity, Double> alertThreadPoolConfigEntities = checkCondition();
        if (alertThreadPoolConfigEntities == null || alertThreadPoolConfigEntities.size() == 0) return;
        String key = dynamicThreadPoolAutoProperties.getConfig().getNotifier().getChannel().getKey();
        String WEBHOOK_URL = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=" + key;
        RestTemplate restTemplate = new RestTemplate();
        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        try {
            for (Map.Entry<ThreadPoolConfigEntity, Double> entry : alertThreadPoolConfigEntities.entrySet()) {
                String content = getNotifyMessage(entry);
                // Create request body
                String jsonPayload = String.format(
                        "{ \"msgtype\": \"text\", \"text\": { \"content\": \"%s\" } }",
                        content
                );

                // Create HttpEntity with headers and body
                HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);

                // Send POST request
                ResponseEntity<String> responseEntity = restTemplate.exchange(WEBHOOK_URL, HttpMethod.POST, requestEntity, String.class);

                // Print response
                HttpStatus statusCode = responseEntity.getStatusCode();
                logger.info("企业微信告警成功，Response Code:" + statusCode);
                logger.info("Response Body: " + responseEntity.getBody());
            }
        } catch (Exception e) {
            logger.error("企业微信告警失败" + e.getMessage());
        }

    }
}
