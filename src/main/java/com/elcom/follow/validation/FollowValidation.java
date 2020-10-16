package com.elcom.follow.validation;

import com.elcom.follow.model.Follow;
import com.elcom.follow.utils.StringUtil;

public class FollowValidation extends AbstractValidation {

    public String validateInsert(Follow follow) {

        if ( StringUtil.isNullOrEmpty(follow.getObjectType()) )
            getMessageDes().add("objectType không được để trống");
        
        if ( StringUtil.isNullOrEmpty(follow.getObjectId()) )
            getMessageDes().add("objectId không được để trống");
        else if( !StringUtil.validUuid(follow.getObjectId()) )
            getMessageDes().add("objectId sai định dạng");
        
        if( follow.getLevel() == null )
            getMessageDes().add("level không được để trống");
        
        if ( StringUtil.isNullOrEmpty(follow.getUserId()) )
            getMessageDes().add("userId không được để trống");
        else if( !StringUtil.validUuid(follow.getUserId()) )
            getMessageDes().add("userId sai định dạng");

        return !isValid() ? this.buildValidationMessage() : null;
    }
    
    public String validateFindLst(String objectType, String objectId, Integer level) {

        if ( StringUtil.isNullOrEmpty(objectType) )
            getMessageDes().add("objectType không được để trống");
        
        if ( StringUtil.isNullOrEmpty(objectId) )
            getMessageDes().add("objectId không được để trống");
        else if( !StringUtil.validUuid(objectId) )
            getMessageDes().add("objectId sai định dạng");
        
        if( level == null )
            getMessageDes().add("level không được để trống");
        
        return !isValid() ? this.buildValidationMessage() : null;
    }
    
    public String validateFindStatus(String objectId, Integer level, String userId) {
        
        if ( StringUtil.isNullOrEmpty(objectId) )
            getMessageDes().add("objectId không được để trống");
        else if( !StringUtil.validUuid(objectId) )
            getMessageDes().add("objectId sai định dạng");
        
        if( level == null )
            getMessageDes().add("level không được để trống");
        
        if ( StringUtil.isNullOrEmpty(userId) )
            getMessageDes().add("userId không được để trống");
        else if( !StringUtil.validUuid(userId) )
            getMessageDes().add("userId sai định dạng");
        
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
