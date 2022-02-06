package com.progetto.backendserver.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {
    private static final Logger log = LoggerFactory.getLogger(SecurityInitializer.class);

    public SecurityInitializer() {
        super(SecurityConfiguration.class);
    }
}
