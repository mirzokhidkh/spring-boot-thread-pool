package uz.mirzokhidkh.springbootthreads.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ThreadConfig {

    @Bean
//    @Primary
    public TaskExecutor threadPoolTaskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
//        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(30);
        executor.setThreadNamePrefix("mkh-pool-");
        executor.setWaitForTasksToCompleteOnShutdown ( true ) ;
//        executor.setAwaitTerminationSeconds (7) ;
        executor.initialize();

        return executor;
    }



}
