package com.elcom.follow.model.dto;

import java.util.Map;

/**
 *
 * @author Admin
 */
public class AuthorizationResponseDTO {

    private String uuid;
    private String userName;
    private String email;
    private String mobile;
    private String fullName;
    private String avatar;
    private Integer status;
    private String address;
    private String skype;
    private String facebook;
    private Integer signupType;
    private Long createdAt;
    private Long lastLogin;
    private Integer emailVerify;
    private Integer mobileVerify;
    private Long lastUpdate;
    private String loginIp;
    private Integer isDelete;
    private String birthDay;
    private Integer gender;
    private String fbId;
    private String ggId;
    private Integer setPassword;
    private Long profileUpdate;
    private Long avatarUpdate;

    public AuthorizationResponseDTO() {
    }

    public AuthorizationResponseDTO(Map<String, Object> map) {
        if (map != null && map.size() > 0) {
            if (map.containsKey("uuid")) {
                this.uuid = (String) map.get("uuid");
            }
            if (map.containsKey("userName")) {
                this.userName = (String) map.get("userName");
            }
            if (map.containsKey("email")) {
                this.email = (String) map.get("email");
            }
            if (map.containsKey("mobile")) {
                this.mobile = (String) map.get("mobile");
            }
            if (map.containsKey("fullName")) {
                this.fullName = (String) map.get("fullName");
            }
            if (map.containsKey("avatar")) {
                this.avatar = (String) map.get("avatar");
            }
            if (map.containsKey("status")) {
                this.status = (Integer) map.get("status");
            }
            if (map.containsKey("address")) {
                this.address = (String) map.get("address");
            }
            if (map.containsKey("skype")) {
                this.skype = (String) map.get("skype");
            }
            if (map.containsKey("facebook")) {
                this.facebook = (String) map.get("facebook");
            }
            if (map.containsKey("signupType")) {
                this.signupType = (Integer) map.get("signupType");
            }
            if (map.containsKey("createdAt")) {
                this.createdAt = (Long) map.get("createdAt");
            }
            if (map.containsKey("lastLogin")) {
                this.lastLogin = (Long) map.get("lastLogin");
            }
            if (map.containsKey("emailVerify")) {
                this.emailVerify = (Integer) map.get("emailVerify");
            }
            if (map.containsKey("mobileVerify")) {
                this.mobileVerify = (Integer) map.get("mobileVerify");
            }
            if (map.containsKey("lastUpdate")) {
                this.lastUpdate = (Long) map.get("lastUpdate");
            }
            if (map.containsKey("loginIp")) {
                this.loginIp = (String) map.get("loginIp");
            }
            if (map.containsKey("isDelete")) {
                this.isDelete = (Integer) map.get("isDelete");
            }
            if (map.containsKey("birthDay")) {
                this.birthDay = (String) map.get("birthDay");
            }
            if (map.containsKey("gender")) {
                this.gender = (Integer) map.get("gender");
            }
            if (map.containsKey("fbId")) {
                this.fbId = (String) map.get("fbId");
            }
            if (map.containsKey("ggId")) {
                this.ggId = (String) map.get("ggId");
            }
            if (map.containsKey("setPassword")) {
                this.setPassword = (Integer) map.get("setPassword");
            }
            if (map.containsKey("profileUpdate")) {
                this.profileUpdate = (Long) map.get("profileUpdate");
            }
            if (map.containsKey("avatarUpdate")) {
                this.avatarUpdate = (Long) map.get("avatarUpdate");
            }
        }
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public Integer getSignupType() {
        return signupType;
    }

    public void setSignupType(Integer signupType) {
        this.signupType = signupType;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Integer getEmailVerify() {
        return emailVerify;
    }

    public void setEmailVerify(Integer emailVerify) {
        this.emailVerify = emailVerify;
    }

    public Integer getMobileVerify() {
        return mobileVerify;
    }

    public void setMobileVerify(Integer mobileVerify) {
        this.mobileVerify = mobileVerify;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getGgId() {
        return ggId;
    }

    public void setGgId(String ggId) {
        this.ggId = ggId;
    }

    /**
     * @return the setPassword
     */
    public Integer getSetPassword() {
        return setPassword;
    }

    /**
     * @param setPassword the setPassword to set
     */
    public void setSetPassword(Integer setPassword) {
        this.setPassword = setPassword;
    }

    /**
     * @return the profileUpdate
     */
    public Long getProfileUpdate() {
        return profileUpdate;
    }

    /**
     * @param profileUpdate the profileUpdate to set
     */
    public void setProfileUpdate(Long profileUpdate) {
        this.profileUpdate = profileUpdate;
    }

    /**
     * @return the avatarUpdate
     */
    public Long getAvatarUpdate() {
        return avatarUpdate;
    }

    /**
     * @param avatarUpdate the avatarUpdate to set
     */
    public void setAvatarUpdate(Long avatarUpdate) {
        this.avatarUpdate = avatarUpdate;
    }
}
