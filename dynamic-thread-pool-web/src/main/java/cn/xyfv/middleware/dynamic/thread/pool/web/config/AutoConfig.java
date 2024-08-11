package cn.xyfv.middleware.dynamic.thread.pool.web.config;

import cn.xyfv.middleware.dynamic.thread.pool.sdk.config.DynamicThreadPoolAutoProperties;
import cn.xyfv.middleware.dynamic.thread.pool.web.trigger.DynamicThreadPoolController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p>2024-08-08 15:58</p>
 *
 * @author cnzhangxy
 * @since 1.0.0
 **/
@Configuration
@EnableConfigurationProperties(DynamicThreadPoolAutoProperties.class)
@ComponentScan("cn.xyfv.middleware.dynamic.thread.pool.web.trigger")
public class AutoConfig {
    @Bean
    @ConditionalOnBean(DynamicThreadPoolController.class)
    public ServerConfig serverConfig() {
        return new ServerConfig();
    }
}
