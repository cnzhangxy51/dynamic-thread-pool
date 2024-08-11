package cn.xyfv.middleware.dynamic.thread.pool.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;

/**
 * 内置管理页面
 * <p>2024-08-08 20:47</p>
 *
 * @author cnzhangxy
 * @since 1.0.0
 **/
@Slf4j
public class ServerConfig implements ApplicationListener<WebServerInitializedEvent> {
    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        int serverPort = event.getWebServer().getPort();
        log.info("可查看内置管理页面 http://localhost:{}/index.html", serverPort);
    }
}
