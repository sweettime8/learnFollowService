package com.elcom.follow.bussiness;

import com.elcom.follow.constant.Constant;
import com.elcom.follow.controller.BaseController;
import com.elcom.follow.model.Follow;
import com.elcom.follow.model.dto.AuthorizationResponseDTO;
import com.elcom.follow.service.FollowService;
import com.elcom.follow.utils.StringUtil;
import com.elcom.follow.validation.FollowValidation;
import com.elcom.gateway.message.MessageContent;
import com.elcom.gateway.message.ResponseMessage;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

/**
 *
 * @author anhdv
 */
@Controller
public class FollowBusiness extends BaseController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(FollowBusiness.class);
    
//    @Autowired
//    private JobQueueService jobQueueService;
    
    @Autowired
    private FollowService followService;
    
    /** Theo dõi đối tượng.
     * Service public để RestClient gọi qua Gateway, có check Auth
     * @param bodyParam
     * @param headerMap
     * @return 200|400|500 */
    public ResponseMessage save(Map<String, Object> bodyParam, Map<String, String> headerMap) {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null)
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else {
            if (bodyParam == null || bodyParam.isEmpty())
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                           new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            else {
                String objectType = (String) bodyParam.get("objectType");
                String objectId = (String) bodyParam.get("objectId");
                Integer level = (Integer) bodyParam.get("level");
                String userId = (String) bodyParam.get("userId");
                Follow follow = new Follow(objectType, objectId, level, userId, Constant.FOLLOW_STATUS_ACTIVE);
                String validationMsg = new FollowValidation().validateInsert(follow);
                if( validationMsg != null )
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), validationMsg,
                               new MessageContent(HttpStatus.BAD_REQUEST.value(), validationMsg, null));
                else {
                    Integer flowStatus = this.followService.existsFollow(follow);
                    if( flowStatus!=null ) {
                        if( flowStatus.equals(1) ) {// Đã tồn tại và đang theo dõi thì báo raw existed
                            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), validationMsg,
                                   new MessageContent(HttpStatus.BAD_REQUEST.value(), validationMsg, "Raw existed!"));
                        }else { // Không thì set lại status = active (set = 1)
                            boolean updateStatus = this.followService.reSubscribeObject(objectType, objectId, level, userId);
                            if( !updateStatus )
                                response = new ResponseMessage(HttpStatus.NOT_MODIFIED.value(), HttpStatus.NOT_MODIFIED.toString(),
                                           new MessageContent(HttpStatus.NOT_MODIFIED.value(), HttpStatus.NOT_MODIFIED.toString(), updateStatus));
                            else
                                response = new ResponseMessage(HttpStatus.OK.value(), HttpStatus.OK.toString(), new MessageContent(updateStatus));
                        }
                    }else {
                        this.followService.save(follow);
                        if( follow.getId() == null )
                            response = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                                       new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.toString(), follow));
                        else
                            response = new ResponseMessage(HttpStatus.OK.value(), HttpStatus.OK.toString(), new MessageContent(follow));
                        
                        /** Insert vào RedisQueue /
                        /*jobQueueService.registerJobQueue(Constant.REDIS_QUEUE_FOLLOW_LST);
                        jobQueueService.insertToWaitingQueue(Constant.REDIS_QUEUE_FOLLOW_LST, follow);
                        LOGGER.info("jobInput.follow.userId: {}", follow.getUserId());
                        response = new ResponseMessage(HttpStatus.OK.value(), HttpStatus.OK.toString(), new MessageContent(follow));*/
                    }
                }
            }
        }
        return response;
    }
    
    /** Theo dõi đối tượng.
     * Service private để gọi chéo giữa các service, ko check Auth
     * @param bodyParam
     * @param headerMap
     * @return 200|400|500 */
    public ResponseMessage saveInternal(Map<String, Object> bodyParam, Map<String, String> headerMap) {
        ResponseMessage response;
        if (bodyParam == null || bodyParam.isEmpty())
            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                       new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
        else {
            String objectType = (String) bodyParam.get("objectType");
            String objectId = (String) bodyParam.get("objectId");
            Integer level = (Integer) bodyParam.get("level");
            String userId = (String) bodyParam.get("userId");
            Follow follow = new Follow(objectType, objectId, level, userId, Constant.FOLLOW_STATUS_ACTIVE);
            String validationMsg = new FollowValidation().validateInsert(follow);
            if( validationMsg != null )
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), validationMsg,
                           new MessageContent(HttpStatus.BAD_REQUEST.value(), validationMsg, null));
            else {
                Integer flowStatus = this.followService.existsFollow(follow);
                if( flowStatus!=null ) {
                    if( flowStatus.equals(1) ) {// Đã tồn tại và đang theo dõi thì báo raw existed
                        response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), validationMsg,
                               new MessageContent(HttpStatus.BAD_REQUEST.value(), validationMsg, "Raw existed!"));
                    }else { // Không thì set lại status = active (set = 1)
                        boolean updateStatus = this.followService.reSubscribeObject(objectType, objectId, level, userId);
                        if( !updateStatus )
                            response = new ResponseMessage(HttpStatus.NOT_MODIFIED.value(), HttpStatus.NOT_MODIFIED.toString(),
                                       new MessageContent(HttpStatus.NOT_MODIFIED.value(), HttpStatus.NOT_MODIFIED.toString(), updateStatus));
                        else
                            response = new ResponseMessage(HttpStatus.OK.value(), HttpStatus.OK.toString(), new MessageContent(updateStatus));
                    }
                }else {
                    this.followService.save(follow);
                    if( follow.getId() == null )
                        response = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                                   new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.toString(), follow));
                    else
                        response = new ResponseMessage(HttpStatus.OK.value(), HttpStatus.OK.toString(), new MessageContent(follow));
                }
            }
        }
        return response;
    }
    
    /** Bỏ theo dõi đối tượng.
     * Service public để RestClient gọi qua Gateway, có check Auth
     * @param bodyParam
     * @param headerMap
     * @return true|false */
    public ResponseMessage unSubscribeObject(Map<String, Object> bodyParam, Map<String, String> headerMap) {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null)
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else {
            if (bodyParam == null || bodyParam.isEmpty())
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                           new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            else {
                String objectType = (String) bodyParam.get("objectType");
                String objectId = (String) bodyParam.get("objectId");
                Integer level = (Integer) bodyParam.get("level");
                String userId = (String) bodyParam.get("userId");
                Follow follow = new Follow(objectType, objectId, level, userId, Constant.FOLLOW_STATUS_ACTIVE);
                String validationMsg = new FollowValidation().validateInsert(follow);
                if( validationMsg != null )
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), validationMsg,
                               new MessageContent(HttpStatus.BAD_REQUEST.value(), validationMsg, null));
                else {
                    boolean updateStatus = this.followService.unSubscribeObject(objectType, objectId, level, userId);
                    if( !updateStatus )
                        response = new ResponseMessage(HttpStatus.NOT_MODIFIED.value(), HttpStatus.NOT_MODIFIED.toString(),
                                   new MessageContent(HttpStatus.NOT_MODIFIED.value(), HttpStatus.NOT_MODIFIED.toString(), updateStatus));
                    else
                        response = new ResponseMessage(HttpStatus.OK.value(), HttpStatus.OK.toString(), new MessageContent(updateStatus));
                }
            }
        }
        return response;
    }
    
    /** Get danh sách user đang theo dõi theo đối tượng cần theo dõi.
     * Service public để RestClient gọi qua Gateway, có check Auth
     * @param urlParam
     * @param headerMap
     * @return list */
    public ResponseMessage findLstFollow(String urlParam, Map<String, String> headerMap) {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null)
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else {
            if ( StringUtil.isNullOrEmpty(urlParam) )
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                           new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            else {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                String objectType = (String) params.get("objectType");
                String objectId = (String) params.get("objectId");
                Integer level = null;
                try {
                    level = Integer.parseInt(params.get("level"));
                }catch(NumberFormatException ex) {
                    LOGGER.error(ex.toString());
                }

                String validationMsg = new FollowValidation().validateFindLst(objectType, objectId, level);
                if( validationMsg != null )
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), validationMsg,
                               new MessageContent(HttpStatus.BAD_REQUEST.value(), validationMsg, null));
                else {
                    List<String> lst = this.followService.findLstFollow(objectType, objectId, level);
                    if ( lst==null || lst.isEmpty() )
                        response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                                   new MessageContent(HttpStatus.NOT_FOUND.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
                    else
                        response = new ResponseMessage(new MessageContent(lst));
                }
            }
        }
        return response;
    }
    
    /** Get danh sách user đang theo dõi theo đối tượng cần theo dõi.
     * Service private để gọi chéo giữa các service, ko check Auth
     * @param urlParam
     * @param headerMap
     * @return list */
    public ResponseMessage findLstFollowInternal(String urlParam, Map<String, String> headerMap) {
        ResponseMessage response;
        if ( StringUtil.isNullOrEmpty(urlParam) )
            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                       new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
        else {
            Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
            String objectType = (String) params.get("objectType");
            String objectId = (String) params.get("objectId");
            Integer level = null;
            try {
                level = Integer.parseInt(params.get("level"));
            }catch(NumberFormatException ex) {
                LOGGER.error(ex.toString());
            }

            String validationMsg = new FollowValidation().validateFindLst(objectType, objectId, level);
            if( validationMsg != null )
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), validationMsg,
                           new MessageContent(HttpStatus.BAD_REQUEST.value(), validationMsg, null));
            else {
                List<String> lst = this.followService.findLstFollow(objectType, objectId, level);
                if ( lst==null || lst.isEmpty() )
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), Constant.VALIDATION_DATA_NOT_FOUND,
                               new MessageContent(HttpStatus.NOT_FOUND.value(), Constant.VALIDATION_DATA_NOT_FOUND, null));
                else
                    response = new ResponseMessage(new MessageContent(lst));
            }
        }
        return response;
    }
    
    /** Get trạng thái user đang theo dõi hay không, theo đối tượng cần theo dõi.
     * Service public để RestClient gọi qua Gateway, có check Auth
     * @param urlParam
     * @param headerMap
     * @return true|false */
    public ResponseMessage findFollowStatus(String urlParam, Map<String, String> headerMap) {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null)
            response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(),
                       new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "Bạn chưa đăng nhập"));
        else {
            if ( StringUtil.isNullOrEmpty(urlParam) )
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                           new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            else {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                String objectId = (String) params.get("objectId");
                Integer level = null;
                try {
                    level = Integer.parseInt(params.get("level"));
                }catch(NumberFormatException ex) {
                    LOGGER.error(ex.toString());
                }
                String userId = (String) params.get("userId");
                
                String validationMsg = new FollowValidation().validateFindStatus(objectId, level, userId);
                if( validationMsg != null )
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), validationMsg,
                               new MessageContent(HttpStatus.BAD_REQUEST.value(), validationMsg, null));
                else {
                    boolean followStatus = this.followService.findFollowStatus(objectId, level, userId);
                    response = new ResponseMessage(new MessageContent(followStatus));
                }
            }
        }
        return response;
    }
}
