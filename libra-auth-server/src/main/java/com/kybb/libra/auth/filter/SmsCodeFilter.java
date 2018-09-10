package com.kybb.libra.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kybb.common.cloud.integration.SmsCodeLogin;
import com.kybb.common.cloud.util.HttpUtil;
import com.kybb.libra.service.SmsCodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static com.kybb.common.cloud.constants.AuthorizationServerConstants.SMS_CODE_LOGIN_URL;

/**
 * @Auther: vicykie
 * @Date: 2018/8/22 23:08
 * @Description: 继承OncePerRequestFilter：spring提供的工具，保证过滤器每次只会被调用一次
 * 实现 InitializingBean接口的目的：
 * 在其他参数都组装完毕的时候，初始化需要拦截的urls的值
 */
@Slf4j
public class SmsCodeFilter extends OncePerRequestFilter implements InitializingBean {

    //需要拦截的url集合
    private Set<String> urls = new HashSet<>();
    //spring工具类
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SmsCodeService smsCodeService;
    //认证失败处理器
    private final AuthenticationFailureHandler authenticationFailureHandler;

    public SmsCodeFilter(SmsCodeService smsCodeService, AuthenticationFailureHandler authenticationFailureHandler) {
        this.smsCodeService = smsCodeService;
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (antPathMatcher.match(SMS_CODE_LOGIN_URL, request.getRequestURI()) && request.getMethod().equalsIgnoreCase("post")) {
            //检查请求信息
            SmsCodeLogin loginMsg = HttpUtil.getLoginMsg(request);
            boolean b = checkParameter(loginMsg, request, response);
            if (!b) {
                //不进行下一步SmsCodeAuthenticationFilter
                return;
            }

            String encode = smsCodeService.getCode(loginMsg);
            if (StringUtils.isEmpty(encode) || !smsCodeService.match(loginMsg.getSmsCode(), encode)) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, new AuthenticationServiceException("验证码错误"));
                return;
            }
            if (log.isDebugEnabled()) {
                log.debug("=======验证码通过=====");
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean checkParameter(SmsCodeLogin loginMsg, HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader("Authorization");
        response.setContentType(ContentType.APPLICATION_JSON.toString());
        //没有client信息
        if (header == null || !header.startsWith("Basic ")) {
            HttpUtil.writeResponse(objectMapper, "请求头没有client 信息", response);
            return false;
        }
        if (loginMsg == null) {
            HttpUtil.writeResponse(objectMapper, "缺少登录信息", response);
            return false;
        }
        if (StringUtils.isEmpty(loginMsg.getDeviceId())) {
            HttpUtil.writeResponse(objectMapper, "缺少deviceId", response);
            return false;
        }
        if (StringUtils.isEmpty(loginMsg.getMobile())) {
            HttpUtil.writeResponse(objectMapper, "mobile信息错误", response);
            return false;
        }
        if (StringUtils.isEmpty(loginMsg.getSmsCode())) {
            HttpUtil.writeResponse(objectMapper, "smsCode 为空", response);
            return false;
        }
        return true;
    }


}
