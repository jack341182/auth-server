package com.kybb.common.cloud.integration;

import com.kybb.common.enums.UserTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.User;

import java.io.Serializable;
import java.util.Collection;

/**
 * @Auther: vicykie
 * @Date: 2018/8/16 20:33
 * @Description:
 */
@Getter
@Setter
public class IntegrationUser extends User implements Serializable {
    private static final long serialVersionUID = 600L;
    private Long id;
    private String wxOpenId;
    private String email;
    private String telephone;
    private UserTypeEnum userType;
    private String token;


    private String appType;

    public IntegrationUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public IntegrationUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, Long id, String wxOpenId, String email, String telephone, UserTypeEnum userType, String token, String appType) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.wxOpenId = wxOpenId;
        this.email = email;
        this.telephone = telephone;
        this.userType = userType;
        this.token = token;
        this.appType = appType;
    }
}
