package com.elcom.follow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableCaching
@EnableScheduling
//public class CoLearnFollowApplication implements CommandLineRunner {
public class CoLearnFollowApplication {

//    private static final Logger LOGGER = LoggerFactory.getLogger(CoLearnFollowApplication.class);

//    @Autowired
//    private JobQueueServiceTmp jobQueueService;

    public static void main(String[] args) {
        // Fix lỗi "UDP failed setting ip_ttl | Method not implemented" khi start app trên Windows
//        System.setProperty("java.net.preferIPv4Stack", "true");

        SpringApplication.run(CoLearnFollowApplication.class, args);
    }

    /*@Override
    public void run(String... args) throws Exception {

        // register the queue
        jobQueueService.registerJobQueue(Constant.REDIS_QUEUE_FOLLOW_LST);

        // add job to the queue
        for (int i = 1; i <= 5; i++) {
            LoggingJob jobInput = new LoggingJob(i);
            jobQueueService.insertToWaitingQueue(Constant.REDIS_QUEUE_FOLLOW_LST, jobInput);
            LOGGER.info("jobInput.set: {}", jobInput.getNumLog());
        }
    }*/
}
