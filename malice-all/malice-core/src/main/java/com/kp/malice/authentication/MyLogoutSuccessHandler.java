package com.kp.malice.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class MyLogoutSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler implements
        LogoutSuccessHandler {

    Logger log = Logger.getLogger(MyLogoutSuccessHandler.class);

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        super.handle(request, response, authentication);
        log.info("\t" + authentication.getPrincipal() + " logged out");
    }
}