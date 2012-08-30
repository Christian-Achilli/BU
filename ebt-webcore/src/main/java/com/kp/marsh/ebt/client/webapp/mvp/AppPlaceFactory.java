package com.kp.marsh.ebt.client.webapp.mvp;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.kp.marsh.ebt.client.webapp.place.LogoutPlace;

/**
 * 
 * A place factory which knows about all the tokenizers in the app
 * 
 *
 */
public class AppPlaceFactory {

    //	@Inject
    //	LoginPlace.Tokenizer loginPlaceTokenizer;

    //	@Inject
    //	InsertPotentialPlace.Tokenizer insertPotentialPlaceTokenizer;

    @Inject
    LogoutPlace.Tokenizer logoutPlaceTokenizer;

    //	@Inject
    //	Provider<LoginPlace> loginProvider;

    //	@Inject
    //	Provider<InsertPotentialPlace> insertPotentialProvider;

    @Inject
    Provider<LogoutPlace> logoutProvider;

    //	public LoginPlace getLoginPlace() {
    //		return loginProvider.get();
    //	}

    public LogoutPlace getLogoutPlace() {
        return logoutProvider.get();
    }

    //	public LoginPlace.Tokenizer getLoginPlaceTokenizer() {
    //		return loginPlaceTokenizer;
    //	}

    //	public InsertPotentialPlace.Tokenizer getInsertPotetntialPlaceTokenizer() {
    //		return insertPotentialPlaceTokenizer;
    //	}

    public LogoutPlace.Tokenizer getLogoutPlaceTokenizer() {
        return logoutPlaceTokenizer;
    }

}
