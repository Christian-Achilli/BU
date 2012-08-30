package com.kp.marsh.ebt.client.webapp.activities;

import java.util.Date;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.kp.marsh.ebt.client.webapp.AuthenticationServiceAsync;
import com.kp.marsh.ebt.client.webapp.place.AuthenticationPlace;
import com.kp.marsh.ebt.client.webapp.place.LoginCEPlace;
import com.kp.marsh.ebt.client.webapp.place.LoginManagerPlace;
import com.kp.marsh.ebt.client.webapp.place.LoginManagerWithClientsPlace;
import com.kp.marsh.ebt.client.webapp.place.LogoutPlace;
import com.kp.marsh.ebt.client.webapp.ui.activityintf.ILoginViewDisplay;
import com.kp.marsh.ebt.shared.dto.LoggedInUser;

public class AuthenticationActivity extends AbstractActivity implements ILoginViewDisplay.Listener {

    private final AuthenticationServiceAsync rpcService;
    private final ILoginViewDisplay display;

    private PlaceController placeController;

    private String sessionID; // sessionId is stored in case the user request for the login page but the last session is still valid

    private void registerSessionCookie(String sessionId) {
        String sessionID = sessionId/*(Get sessionID from server's response to your login request.)*/;
        final long DURATION = 1000 * 60 * 60 * 24 * 14; //duration remembering login. 2 weeks in this example.
        Date expires = new Date(System.currentTimeMillis() + DURATION);
        Cookies.setCookie("sid", sessionID, expires, null, "/", false);
    }

    private void checkWithServerIfSessionIdIsStillLegal() {

        rpcService.isSessionAuthenticated(new AsyncCallback<Boolean>() {

            public void onSuccess(Boolean result) {

                if (Boolean.TRUE.equals(result)) {

                    rpcService.getLoggedInUser(new AsyncCallback<LoggedInUser>() {

                        @Override
                        public void onSuccess(LoggedInUser result) {
                            Place userPlace = null;
                            if (result.isManager()) {
                                if (result.hasClients()) {
                                    userPlace = new LoginManagerWithClientsPlace(result.getDescription(), ""
                                            + result.getId(), result.getParentId().toString(), result.getOfficeName());
                                } else
                                    userPlace = new LoginManagerPlace(result.getDescription(), "" + result.getId(),
                                            result.getParentId().toString(), result.getOfficeName());
                            } else {
                                userPlace = new LoginCEPlace(result.getDescription(), "" + result.getId());
                            }
                            placeController.goTo(userPlace);

                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            placeController.goTo(new AuthenticationPlace());

                        }
                    }); // getLoggedInUser

                } // if

            } // isSessionAuth success

            public void onFailure(Throwable caught) {
                placeController.goTo(new AuthenticationPlace());
            }

        });

    }

    @Inject
    public AuthenticationActivity(ILoginViewDisplay display, AuthenticationServiceAsync rpcService,
            PlaceController placeController) {
        this.rpcService = rpcService;
        this.display = display;
        this.placeController = placeController;

    }

    public void start(AcceptsOneWidget container, EventBus eventBus) {

        display.setListener(this);
        display.setLoginErrorMessage("");
        sessionID = Cookies.getCookie("sid");

        container.setWidget(display.asWidget());
    }

    @Override
    public void login(String username, String password) {

        display.setLoginErrorMessage("");
        rpcService.login(username, password, new AsyncCallback<String>() {
            public void onSuccess(String result) {
                if (null != result) {
                    registerSessionCookie(result);

                    rpcService.getLoggedInUser(new AsyncCallback<LoggedInUser>() {

                        @Override
                        public void onSuccess(LoggedInUser result) {
                            Place userPlace = null;
                            if (result.isManager()) {
                                if (result.hasClients()) {
                                    userPlace = new LoginManagerWithClientsPlace(result.getDescription(), ""
                                            + result.getId(), result.getParentId().toString(), result.getOfficeName());
                                } else
                                    userPlace = new LoginManagerPlace(result.getDescription(), "" + result.getId(),
                                            result.getParentId().toString(), result.getOfficeName());
                            } else {
                                userPlace = new LoginCEPlace(result.getDescription(), "" + result.getId());
                            }
                            display.clear();
                            placeController.goTo(userPlace);

                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            display.setLoginErrorMessage("Errore imprevisto, per cortesia ripetere il login");
                            display.clear();
                            placeController.goTo(new AuthenticationPlace());

                        }
                    }); // getLoggedInUser

                } else {
                    display.setLoginErrorMessage("Username e/o Password non validi");
                    display.clear();
                }

            }

            public void onFailure(Throwable caught) {
                display.setLoginErrorMessage("Il servizio non è al momento disponibile. Riprovare più tardi. Ci scusiamo per il disagio.");
                display.clear();
            }
        });

    }

    public Activity withPlace(AuthenticationPlace place) {
        if (sessionID != null)
            checkWithServerIfSessionIdIsStillLegal();
        return this;
    }

    public Activity withPlace(LogoutPlace place) {
        return this;
    }

}