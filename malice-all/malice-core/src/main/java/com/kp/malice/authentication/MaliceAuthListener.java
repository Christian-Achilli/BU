package com.kp.malice.authentication;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;

public class MaliceAuthListener implements ApplicationListener<AbstractAuthenticationEvent> {

    private static final Log logger = LogFactory.getLog(MaliceAuthListener.class);

    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent event) {

        final StringBuilder builder = new StringBuilder();
        builder.append("USERNAME <");
        builder.append(event.getAuthentication().getPrincipal());
        builder.append(">: ");
        builder.append(event.getAuthentication().getName());
        builder.append("; DETAILS: ");
        builder.append(event.getAuthentication().getDetails());
        if (event instanceof AbstractAuthenticationFailureEvent) {
            builder.append("; exception: ");
            builder.append(((AbstractAuthenticationFailureEvent) event).getException().getMessage());
        }
        logger.warn(builder.toString());

    }
}
