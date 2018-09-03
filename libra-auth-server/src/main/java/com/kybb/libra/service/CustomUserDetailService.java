package com.kybb.libra.service;

import com.kybb.common.cloud.constants.AuthorizationServerConstants;
import com.kybb.common.cloud.integration.IntegrationUser;
import com.kybb.common.cloud.util.HttpUtil;
import com.kybb.common.http.Body;
import com.kybb.libra.feign.UserAuthFeignClient;
import com.kybb.libra.feign.UserInfoFeignClient;
import com.kybb.solar.user.enums.ApplicationTypeEnum;
import com.kybb.solar.user.request.AccountRequest;
import com.kybb.solar.user.vo.AccountVO;
import com.kybb.solar.user.vo.ModuleVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.kybb.common.cloud.constants.AuthorizationServerConstants.URL_SPLIT;

/**
 * @Auther: vicykie
 * @Date: 2018/8/22 23:06
 * <p>
 * 读取用户信息
 */
@Component
@Slf4j
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserInfoFeignClient userFeignClient;
    @Autowired
    private UserAuthFeignClient userAuthFeignClient;
//    @Autowired
//    private ModuleFeignClient moduleFeignClient;

    @Autowired
    private HttpServletRequest request;
    //spring工具类
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Override
    public IntegrationUser loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && isRefreshTokenRequest(request)) {//已经授权
            String refresh_token = request.getParameter("refresh_token");
            OAuth2Authentication oAuth2Authentication = tokenStore.readAuthenticationForRefreshToken(tokenStore.readRefreshToken(refresh_token));
            IntegrationUser principal = (IntegrationUser) oAuth2Authentication.getPrincipal();
            AccountRequest a = new AccountRequest();
            a.setUserId(principal.getId());
            a.setAppType(ApplicationTypeEnum.valueOf(principal.getAppType()));
            principal = getUser(a);//重新获取用户信息。
            OAuth2Authentication auth2Authentication = new OAuth2Authentication(oAuth2Authentication.getOAuth2Request(), new UsernamePasswordAuthenticationToken(principal, authentication.getCredentials(), authentication.getAuthorities()));
//            tokenStore.storeAccessToken(oAuth2RefreshToken);
            context.setAuthentication(auth2Authentication);
            return principal;
        }
        AccountRequest a = new AccountRequest();
        if (username.startsWith(AuthorizationServerConstants.WECHAT_PREFIX)) {
            a.setWxOpenId(username.replace(AuthorizationServerConstants.WECHAT_PREFIX, ""));
        } else if (username.startsWith(AuthorizationServerConstants.SMS_CODE_PREFIX)) {
            a.setTelephone(username.replace(AuthorizationServerConstants.SMS_CODE_PREFIX, ""));
        } else {
            a.setUsername(username);
        }

        String header = request.getHeader("Authorization");
        try {
            String[] tokens = HttpUtil.extractAndDecodeHeader(header, request);
            assert tokens.length == 2;
            String clientId = tokens[0];
            a.setAppType(ApplicationTypeEnum.valueOf(clientId));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getUser(a);
    }

    private IntegrationUser getUser(AccountRequest accountRequest) {
        ResponseEntity<Body<AccountVO>> responseEntity = userFeignClient.accounts(accountRequest);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            AccountVO accountVO = responseEntity.getBody().getData();
            if (Objects.isNull(accountVO)) {
                throw new UsernameNotFoundException("用户不存在");
            }
            List<GrantedAuthority> authorities = new ArrayList<>();
            List<Long> idList = accountVO.getRoleIds();
            ResponseEntity<Body<List<ModuleVO>>> bodyResponseEntity = null;
            if (CollectionUtils.isEmpty(idList)) {//非后台用户没有角色
                bodyResponseEntity = userAuthFeignClient.listByUserType(accountVO.getUserType().getValue());
            } else {
                Long[] ids = new Long[]{};
                bodyResponseEntity = userAuthFeignClient.listByRoleIds(idList.toArray(ids));
            }
            if (bodyResponseEntity != null && bodyResponseEntity.getStatusCode() != HttpStatus.OK) {
                authorities.add(new SimpleGrantedAuthority("no_authorities" + URL_SPLIT + "get"));
            } else {
                List<ModuleVO> data = bodyResponseEntity.getBody().getData();
                if (CollectionUtils.isEmpty(data)) {
                    authorities.add(new SimpleGrantedAuthority("no_authorities" + URL_SPLIT + "get"));
                } else {
                    data.forEach(moduleVO -> authorities.add(
                            new SimpleGrantedAuthority(moduleVO.getUrl() + URL_SPLIT + moduleVO.getMethod())));
                }
            }
            if (StringUtils.isEmpty(accountVO.getPassword())) {
                accountVO.setPassword("123456");
            }
            if (StringUtils.isEmpty(accountVO.getUsername())) {
                accountVO.setUsername("wx_no_username");//微信用户没有username
            }
            IntegrationUser integrationUser = new IntegrationUser(accountVO.getUsername(), accountVO.getPassword(),
                    authorities);
            integrationUser.setTelephone(accountVO.getTelephone());
            integrationUser.setId(accountVO.getId());
            integrationUser.setWxOpenId(accountVO.getWxOpenId());
            integrationUser.setUserType(accountVO.getUserType());
            integrationUser.setAppType(accountRequest.getAppType().name());
            return integrationUser;
        } else {
            log.error("服务器异常=== user-center-api");
            throw new InternalAuthenticationServiceException("服务异常。user-center-api");
        }

    }

    private boolean isRefreshTokenRequest(HttpServletRequest request) {
        return "refresh_token".equals(request.getParameter("grant_type")) && request.getParameter("refresh_token") != null;
    }
}
