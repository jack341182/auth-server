package com.kybb.common.cloud.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

/**
 * 模仿UsernamePasswordAuthenticationToken写的短信登录token
 */
public class SmsCodeAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;


    private final Object principal;

    private final String smsCode;
    private final String deviceId;

    /**
     * 一个参数的构造，new的对象是没有认证的
     * <p>Description: </p>
     */
    public SmsCodeAuthenticationToken(String mobile, String smsCode, String deviceId) {
        super(null);
        this.principal = mobile;//未登陆前
        this.smsCode = smsCode;//未登陆前
        this.deviceId = deviceId;//未登陆前
        setAuthenticated(false);//还没认证通过
    }

    /**
     * 2个参数的构造，new的对象是认证通过的
     * <p>Description: </p>
     *
     * @param principal
     * @param authorities
     */
    public SmsCodeAuthenticationToken(Object principal,
                                      Collection<? extends GrantedAuthority> authorities) {
        super(authorities); //authorities放用户权限
        this.principal = principal; //放用户信息
        this.smsCode = null; //放用户信息
        this.deviceId = null; //放用户信息
        super.setAuthenticated(true); //认证通过
    }

    // ~ Methods
    // ========================================================================================================


    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public String getDeviceId() {
        return deviceId;
    }
}




