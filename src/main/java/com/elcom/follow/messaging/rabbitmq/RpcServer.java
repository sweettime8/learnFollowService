package com.elcom.follow.messaging.rabbitmq;

import com.elcom.follow.bussiness.FollowBusiness;
import com.elcom.follow.constant.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.elcom.gateway.message.RequestMessage;
import com.elcom.gateway.message.ResponseMessage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
 *
 * @author anhdv
 */
public class RpcServer {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);
    
    @Autowired
    private FollowBusiness followBusiness;
    
    @RabbitListener(queues = "${learn.follow.rpc.queue}${tech.env}")
    public String rpcReceive(String json) {
//        long startTime = System.currentTimeMillis();
        try {
            LOGGER.info(" [-->] Rpc Server received request for {}", json);
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            RequestMessage request = mapper.readValue(json, RequestMessage.class);
            
            ResponseMessage response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null);
            if (request != null) {
                String requestPath = request.getRequestPath();
                String urlParam = request.getUrlParam();
                //String pathParam = request.getPathParam();
                Map<String, Object> bodyParam = request.getBodyParam();
                Map<String, String> headerParam = request.getHeaderParam();
                //GatewayDebugUtil.debug(requestPath, urlParam, pathParam, bodyParam, headerParam);
                
                switch (request.getRequestMethod()) {
                    case "GET":
                        if( urlParam != null && urlParam.length() > 0 ) {
                            if ( (Constant.SRV_VER + "/follow").equalsIgnoreCase(requestPath) )
                                response = this.followBusiness.findLstFollow(urlParam, headerParam);
                            else if ( (Constant.SRV_VER + "/follow/lst-internal").equalsIgnoreCase(requestPath) )
                                response = this.followBusiness.findLstFollowInternal(urlParam, headerParam);
                            else if ( (Constant.SRV_VER + "/follow/status").equalsIgnoreCase(requestPath) )
                                response = this.followBusiness.findFollowStatus(urlParam, headerParam);
                        }
                        break;
                    case "POST":
                        if ( (Constant.SRV_VER + "/follow").equalsIgnoreCase(requestPath) )
                            response = this.followBusiness.save(bodyParam, headerParam);
                        else if ( (Constant.SRV_VER + "/follow/follow-internal").equalsIgnoreCase(requestPath) )
                            response = this.followBusiness.saveInternal(bodyParam, headerParam);
                        break;
                    case "PUT":
                        break;
                    case "PATCH":
                        if ( (Constant.SRV_VER + "/follow").equalsIgnoreCase(requestPath) )
                            response = this.followBusiness.unSubscribeObject(bodyParam, headerParam);
                        break;
                    case "DELETE":
                        break;
                    default:
                        break;
                }
            }
            LOGGER.info(" [<--] Server returned " + (response!=null ? response.toJsonString() : null));
//            LOGGER.info("Elapsed [{}] for request: [{}]"
//                    , getElapsedTime(System.currentTimeMillis() - startTime), request!=null?request.toJsonString():null);
            return response!=null ? response.toJsonString() : null;
        } catch (Exception ex) {
            LOGGER.error("Error to parse json request >>> " + ex.toString());
        }
        return null;
    }
    
//    public String getElapsedTime(long miliseconds) {
//        //return (miliseconds / 1000.0) + "(s)";
//        return miliseconds + " (ms)";
//    }
}

