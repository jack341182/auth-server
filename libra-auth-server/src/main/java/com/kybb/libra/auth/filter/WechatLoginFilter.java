package com.kybb.libra.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kybb.common.cloud.constants.AuthorizationServerConstants;
import com.kybb.common.cloud.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.kybb.common.cloud.constants.AuthorizationServerConstants.WECHAT_LOGIN_URL;

/**
 * 微信
 */
@Component
@Slf4j
public class WechatLoginFilter extends OncePerRequestFilter implements InitializingBean {
    //spring工具类
    private AntPathMatcher antPathMatcher = new AntPathMatcher();
    private ObjectMapper objectMapper = new ObjectMapper();

    //认证失败处理器
    private final AuthenticationFailureHandler authenticationFailureHandler;

    public WechatLoginFilter(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (antPathMatcher.match(WECHAT_LOGIN_URL, request.getRequestURI()) && request.getMethod().equalsIgnoreCase("post")) {
            if (log.isDebugEnabled()) {
                log.debug("================微信小程序或者公众号登录拦截============");
            }
            String header = request.getHeader("Authorization");
            response.setContentType(ContentType.APPLICATION_JSON.toString());
            //没有client信息
            if (header == null || !header.startsWith("Basic ")) {
                HttpUtil.writeResponse(objectMapper, "请求头没有client 信息", response);
                return;
            }

            if (StringUtils.isEmpty(request.getParameter(AuthorizationServerConstants.WECHAT_LOGIN_URL_PARAME))) {
                HttpUtil.writeResponse(objectMapper, AuthorizationServerConstants.WECHAT_LOGIN_URL_PARAME+" 为空", response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

}
