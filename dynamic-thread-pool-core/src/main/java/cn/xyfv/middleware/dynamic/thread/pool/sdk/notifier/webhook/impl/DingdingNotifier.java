package cn.xyfv.middleware.dynamic.thread.pool.sdk.notifier.webhook.impl;

import cn.xyfv.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import cn.xyfv.middleware.dynamic.thread.pool.sdk.notifier.message.NotifyMessageTemplate;
import cn.xyfv.middleware.dynamic.thread.pool.sdk.notifier.webhook.AbstractWebhookNotifier;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * 钉钉告警
 * <p>2024-08-06 17:58</p>
 *
 * @author cnzhangxy
 * @since 1.0.0
 **/

public class DingdingNotifier extends AbstractWebhookNotifier {
    private final Logger logger = LoggerFactory.getLogger(DingdingNotifier.class);

    @Scheduled(cron = "${dynamic.thread.pool.config.notifier.channel.cron}")
//    @Scheduled(cron = "*/20 * * * * ?")
    @Override
    public void notifier() {
        Map<ThreadPoolConfigEntity, Double> alertThreadPoolConfigEntities = checkCondition();
        if (alertThreadPoolConfigEntities == null || alertThreadPoolConfigEntities.size() == 0) return;
        String CUSTOM_ROBOT_TOKEN = dynamicThreadPoolAutoProperties.getConfig().getNotifier().getChannel().getCUSTOM_ROBOT_TOKEN();
        String SECRET = dynamicThreadPoolAutoProperties.getConfig().getNotifier().getChannel().getSECRET();
        String USER_ID = "<you need @ group user's userId>";
        try {
            Long timestamp = System.currentTimeMillis();

            Date date = new Date(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = sdf.format(date);
            logger.info("告警时间 " + formattedDate);
            String secret = SECRET;
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
//            System.out.println(sign);

            //sign字段和timestamp字段必须拼接到请求URL上，否则会出现 310000 的错误信息
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?sign=" + sign + "&timestamp=" + timestamp);
            OapiRobotSendRequest req = new OapiRobotSendRequest();
            /**
             * 发送文本消息
             */
            for (Map.Entry<ThreadPoolConfigEntity, Double> entry : alertThreadPoolConfigEntities.entrySet()) {
                String message = getNotifyMessage(entry);
                //定义文本内容
                OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
                text.setContent(message);
                //定义 @ 对象
                OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
                at.setAtUserIds(Arrays.asList(USER_ID));
                //设置消息类型
                req.setMsgtype("text");
                req.setText(text);
                req.setAt(at);
                OapiRobotSendResponse rsp = client.execute(req, CUSTOM_ROBOT_TOKEN);
                logger.info("告警成功 " + rsp.getBody());
            }
        } catch (ApiException | UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            logger.error("告警失败 " + e.getMessage());
        }
    }

}
