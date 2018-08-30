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

    public IntegrationUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public IntegrationUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }


}
