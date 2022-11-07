package uz.mirzokhidkh.springbootthreads.config;

import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SpringConfig {
//    @Scheduled(fixedDelay = 3000)
//    @Scheduled(fixedRate = 3000)
//    @Scheduled()
//    public void scheduleFixedDelayTask() {
//        System.out.println(
//                "Fixed delay task - " + System.currentTimeMillis() / 1000);
//    }

}
