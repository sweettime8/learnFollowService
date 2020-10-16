package com.elcom.follow.messaging.rabbitmq;

import com.elcom.follow.bussiness.FollowBusiness;
import com.elcom.follow.constant.Constant;
import com.elcom.gateway.message.RequestMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author anhdv
 */
public class WorkerServer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerServer.class);
    
    @Autowired
    private FollowBusiness followBusiness;
    
    @RabbitListener(queues = "${learn.follow.worker.queue}${tech.env}")
    public void workerReceive(String json) throws InterruptedException, IOException {
        try {
            LOGGER.info(" [-->] Worker server received request for " + json);
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            RequestMessage request = mapper.readValue(json, RequestMessage.class);
            
            //Process here
            if (request != null) {
                String requestPath = request.getRequestPath();
                String urlParam = request.getUrlParam();
                //String pathParam = request.getPathParam();
                Map<String, Object> bodyParam = request.getBodyParam();
                Map<String, String> headerParam = request.getHeaderParam();
                
                /*LOGGER.info("requestPath: " + requestPath + ", urlParam: " + urlParam + ", pathParam: " + pathParam);
                if (bodyParam != null && !bodyParam.isEmpty()) {
                    LOGGER.info("bodyParam");
                    Iterator<Map.Entry<String, Object>> iterator = bodyParam.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, Object> entry = iterator.next();
                        LOGGER.info(entry.getKey() + " => " + entry.getValue());
                    }
                }
                if (headerParam != null && !headerParam.isEmpty()) {
                    LOGGER.info("headerParam");
                    Iterator<Map.Entry<String, String>> iterator = headerParam.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, String> entry = iterator.next();
                        LOGGER.info(entry.getKey() + " => " + entry.getValue());
                    }
                }*/

                switch (request.getRequestMethod()) {
                    case "GET":
                        break;
                    case "POST":
                        if ( (Constant.SRV_VER + "/score-management/action-progress").equalsIgnoreCase(requestPath) )
                        ;    //this.followBusiness.actionProgress(bodyParam, headerParam);
                        else if ( (Constant.SRV_VER + "/score-management/init-user-score").equalsIgnoreCase(requestPath) )
                        ;    //this.followBusiness.initUserScore(bodyParam, headerParam);
                        break;
                    case "DELETE":
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }
}
