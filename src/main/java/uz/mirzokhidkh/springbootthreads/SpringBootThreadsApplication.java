package uz.mirzokhidkh.springbootthreads;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBootThreadsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootThreadsApplication.class, args);
    }

}
