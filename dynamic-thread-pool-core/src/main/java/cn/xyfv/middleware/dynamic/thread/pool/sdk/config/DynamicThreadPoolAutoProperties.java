package cn.xyfv.middleware.dynamic.thread.pool.sdk.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.message.Message;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 动态线程池上报中心配置(redis)
 *
 * @author cnzhangxy
 * @since 2024-05-12 16:23
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "dynamic.thread.pool", ignoreInvalidFields = true)
public class DynamicThreadPoolAutoProperties {

    private Web web;
    private Config config;

    @Getter
    @Setter
    public static class Web {
        private boolean enabled;
    }

    @Getter
    @Setter
    public static class Config {
        private Monitor monitor;
        private Notifier notifier;
        private Refresher refresher;
        private Redis redis;

        @Getter
        @Setter
        public static class Monitor {
            private boolean enabled;
            private Channel channel;

            @Getter
            @Setter
            public static class Channel {
                private String platform;
                private String cron;
            }
        }

        @Getter
        @Setter
        public static class Notifier {
            private boolean enabled;
            private Channel channel;

            @Getter
            @Setter
            public static class Channel {
                private String platform;
                private String CUSTOM_ROBOT_TOKEN;
                private String USER_ID;
                private String SECRET;
                private String key;
                private String cron;
                private Message messages;

                @Getter
                @Setter
                public static class Message {
                    private String user;
                    private String extend;

                }
            }
        }

        @Getter
        @Setter
        public static class Refresher {
            private boolean enabled;
            private Channel channel;

            @Getter
            @Setter
            public static class Channel {
                private String platform;
            }
        }

        @Getter
        @Setter
        public static class Redis {

            private boolean enable = false;

            private String host;

            private String port;

            private String password;

            private int poolSize = 64;

            private int minIdleSize = 10;

            private int idleTimeout = 10000;

            private int connectTimeout = 10000;

            private int retryAttempts = 3;

            private int retryInterval = 1000;

            private int pingInterval = 0;

            private boolean keepAlive = true;

        }

    }

}
