package com.kybb.libra.auth;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;

public class IntegrationMessageResourceBundle extends ResourceBundleMessageSource {
    public IntegrationMessageResourceBundle() {
        setBasename("classpath:messages");
//        setBasename("com.kybb.libra.messages");
    }

    // ~ Methods
    // ========================================================================================================

    public static MessageSourceAccessor getAccessor() {
        return new MessageSourceAccessor(new IntegrationMessageResourceBundle());
    }
}
