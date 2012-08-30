package com.kp.marsh.ebt.server.webapp;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kp.marsh.ebt.client.webapp.AuthenticationService;
import com.kp.marsh.ebt.server.webapp.data.domain.InformationOwners;
import com.kp.marsh.ebt.server.webapp.data.domain.MarshPeopleCredentials;
import com.kp.marsh.ebt.server.webapp.data.domain.dao.DomainDrillerService;
import com.kp.marsh.ebt.server.webapp.data.domain.dao.impl.AuthenticationDAO;
import com.kp.marsh.ebt.server.webapp.utils.HttpSessionUtils;
import com.kp.marsh.ebt.shared.dto.LoggedInUser;

@Singleton
public class AuthenticationServiceImpl extends GuiceRemoteServiceServlet implements AuthenticationService {

    private static Logger log = Logger.getLogger(AuthenticationServiceImpl.class);
    private static final long serialVersionUID = 1L;

    @Inject
    private AuthenticationDAO authService;

    @Inject
    private DomainDrillerService domainDao;

    @Inject
    private IMarshAuthService activeDirectoryService;

    public LoggedInUser getLoggedInUser() {
        return (LoggedInUser) getHttpSession().getAttribute("ebtuser");
    }

    public String login(String username, String password) {

        String sessionId = null;
        log.debug("Checking username is valid: " + username);
        String user = activeDirectoryService.authenticate(username, password);
        MarshPeopleCredentials marshUser;
        try {
            marshUser = authService.getMarshPeopleCredentials(user, "");
        } catch (Exception e) {
            e.printStackTrace();
            marshUser = null;
        }
        boolean valid = marshUser != null ? true : false;
        HttpSession session = getHttpSession();

        if (valid) {
            sessionId = session.getId();
            HttpSessionUtils.addStringToAppSessionValue(session, "login", "true");
            HttpSessionUtils.addStringToAppSessionValue(session, "userName", username);
            InformationOwners io = marshUser.getInformationOwners();
            LoggedInUser loggedInUser = new LoggedInUser();
            loggedInUser.setDescription(io.getDescription());
            loggedInUser.setId(io.getId());
            loggedInUser.setOwnerType(io.getOwnerType());
            loggedInUser.setParentId(io.getParentId());
            loggedInUser.setManager("manager".equals(marshUser.getHashPassword()));

            if (null != io.getParentId()) {
                InformationOwners office = domainDao.findInformationOwnerById(io.getParentId());
                loggedInUser.setOfficeName(office.getDescription());
            }

            List<Integer> gc = domainDao.findGruppoCommerciale(io.getId());
            loggedInUser.setHasClients(!gc.isEmpty());
            session.setAttribute("ebtuser", loggedInUser);
        } else
            HttpSessionUtils.addStringToAppSessionValue(session, "login", "false");

        return sessionId;
    }

    public boolean isSessionAuthenticated() {
        String loginValue = HttpSessionUtils.getStringFromAppSessionValueMap(getHttpSession(), "login");
        return Boolean.parseBoolean(loginValue) && getHttpSession().getAttribute("ebtuser") != null;
    }

    public void logoutSession() {
        HttpSession session = getHttpSession();
        HttpSessionUtils.addStringToAppSessionValue(session, "login", "false");
        session.removeAttribute("ebtuser");
        session.removeAttribute("login");
        session.removeAttribute("userName");
        session.invalidate();
    }

    private HttpSession getHttpSession() {
        HttpServletRequest request = this.getThreadLocalRequest();
        HttpSession session = request.getSession();
        return session;
    }
}
