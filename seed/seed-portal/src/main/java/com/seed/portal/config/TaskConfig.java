package com.seed.portal.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * This configuration is used to define tasks
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/6 14:26
 */
@Slf4j
@EnableAsync
@EnableScheduling
@Configuration
public class TaskConfig {

    @Async
    @Scheduled(cron = "0 */3 * * * ?")
    public void timelyTask() {
        log.debug("Timely task invoked!");
    }

}
