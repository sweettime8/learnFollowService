package com.elcom.follow.schedulers;

import com.elcom.follow.config.PropertiesConfig;
import com.elcom.follow.constant.Constant;
import com.elcom.follow.model.Follow;
import com.elcom.follow.service.FollowService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

/**
 *
 * @author anhdv
 */
@Service
public class Scheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);

    private final FollowService followService;

//    private JobQueueService jobQueueService;

    @Autowired
    public Scheduler(FollowService followService) {
        this.followService = followService;
//        if (PropertiesConfig.APP_MASTER) {
//            this.jobQueueService = jobQueueService;
//            this.jobQueueService.registerJobQueue(Constant.REDIS_QUEUE_FOLLOW_LST);
//        }
    }

    @Scheduled(cron = Constant.ADD_PARTITION_NEXT_MONTH)
//    @Scheduled(cron = "0 45 23 * * *")
    public void addDataPartitionByMonth() throws InterruptedException {
        if (PropertiesConfig.APP_MASTER) {
            final Calendar c = Calendar.getInstance();
            // Nếu là ngày cuối cùng của tháng (28 ==> 31) thì xử lý
            if (c.get(Calendar.DATE) == c.getActualMaximum(Calendar.DATE)) {
                String[] tables = {"follow"};
                this.followService.addPartitionsByMonth(tables);
                LOGGER.info("Add partition success!");
            }
        }
    }

    /*@Scheduled(cron = Constant.SCAN_TIME_FOLLOW_LST_DATA)
    public void scanFollowDataFromRedisQueue() throws InterruptedException {
        if (PropertiesConfig.APP_MASTER) {
            List<Follow> followLst = null;
            while (true) {
                Follow follow = (Follow) this.jobQueueService.getJobFollowToProcess(Constant.REDIS_QUEUE_FOLLOW_LST);
                if (follow == null)
                    break;
                
                if( followLst == null )
                    followLst = new ArrayList<>();
                followLst.add(follow);
                
                this.jobQueueService.clearProcessedJob(Constant.REDIS_QUEUE_FOLLOW_LST, follow);
                LOGGER.info("jobExecute.follow.userId: {}", follow.getUserId());
            }
            if( followLst!=null ) {
                LOGGER.info("jobExecute.followLst.size: " +  followLst.size());
                this.followService.saveAll(followLst);
                LOGGER.info("jobExecute.followLst.saveAll Success!");
            }
        }
    }*/

    @Bean
    public TaskScheduler taskScheduler() {
        final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        return scheduler;
    }
}
