package com.kp.malice.server;

import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.kp.malice.client.AuthService;
import com.kp.malice.entities.business.MaliceUserAuthenticated;
import com.kp.malice.entities.persisted.HibernateSessionFactoryUtil;
import com.kp.malice.shared.LoggedInUserInfo;

@SuppressWarnings("serial")
public class AuthServiceImpl extends RemoteServiceServlet implements AuthService {

    private static Logger log = Logger.getLogger(AuthServiceImpl.class);

    @Override
    public LoggedInUserInfo retrieveLoggedInUserInfo() {
        MaliceUserAuthenticated authentication = (MaliceUserAuthenticated) SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null) {
            log.info("Not logged in");
            return null;
        } else {
            log.info("Logged in user is: " + authentication.getPrincipal());
            log.info("Logged in user authority is: "
                    + ((GrantedAuthority) authentication.getAuthorities().toArray()[0]).getAuthority());
            String nomeAge = "";
            if (null != authentication.getAgenzia()) {
                if ("admin".equals(authentication.getPrincipal()))
                    nomeAge = "All Agencies";
                else
                    nomeAge = authentication.getAgenzia().getDescription();
            }

            return new LoggedInUserInfo(nomeAge + " - OMC: " + authentication.getAgenzia().getOmcCode(),
                    ((GrantedAuthority) authentication.getAuthorities().toArray()[0]).getAuthority());
        }
    }

    @Override
    public void pingToKeepAliveServerSession() {
        MaliceUserAuthenticated authentication = (MaliceUserAuthenticated) SecurityContextHolder.getContext()
                .getAuthentication();
        log.info("Ping to keep alive session for user: " + (String) authentication.getPrincipal());
    }

    @Override
    public void destroy() {
        HibernateSessionFactoryUtil.closeSessionFactory();
        super.destroy();
    }

}
