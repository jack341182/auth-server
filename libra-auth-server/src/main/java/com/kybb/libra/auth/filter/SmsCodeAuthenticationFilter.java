package com.kybb.libra.auth.filter;

import com.kybb.common.cloud.constants.AuthorizationServerConstants;
import com.kybb.common.cloud.token.SmsCodeAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.kybb.common.cloud.constants.AuthorizationServerConstants.SMS_CODE_LOGIN_URL;

/**
 * 模仿UsernamePasswordAuthenticationFilter 写的短信验证码过滤器
 * ClassName: SmsCodeAuthenticationFilter
 */
@Slf4j
public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String PARAM_MOBILE = "mobile";


    private boolean postOnly = true;//只处理post请求


    public SmsCodeAuthenticationFilter() {
        //过滤的请求url，登录表单的url
        super(new AntPathRequestMatcher(SMS_CODE_LOGIN_URL, "POST"));
    }

    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        //获取手机号
        String mobile = obtainMobile(request);

        if (mobile == null) {
            mobile = "";
        }

        mobile = AuthorizationServerConstants.SMS_CODE_PREFIX + mobile.trim();
        //到这里认证还没通过，SmsCodeAuthenticationToken一个参数的构造，是没有认证通过的
        SmsCodeAuthenticationToken authRequest = new SmsCodeAuthenticationToken(mobile);

        //把请求里一些信息如ip等set给SmsCodeAuthenticationToken，此时SmsCodeAuthenticationToken还没认证
        setDetails(request, authRequest);
        /**
         * 认证，在这里把SmsCodeAuthenticationToken交给AuthenticationManager，
         * 找到SmsCodeAuthenticationProvider，调用其authenticate()方法认证
         */
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * 获取手机号
     */
    private String obtainMobile(HttpServletRequest request) {
        return request.getParameter(PARAM_MOBILE);
    }

    /**
     * Provided so that subclasses may configure what is put into the authentication
     * request's details property.
     *
     * @param request     that an authentication request is being created for
     * @param authRequest the authentication request object that should have its details
     *                    set
     */
    protected void setDetails(HttpServletRequest request,
                              SmsCodeAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }


    /**
     * Defines whether only HTTP POST requests will be allowed by this filter. If set to
     * true, and an authentication request is received which is not a POST request, an
     * exception will be raised immediately and authentication will not be attempted. The
     * <tt>unsuccessfulAuthentication()</tt> method will be called as if handling a failed
     * authentication.
     * <p>
     * Defaults to <tt>true</tt> but may be overridden by subclasses.
     */
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }


}
