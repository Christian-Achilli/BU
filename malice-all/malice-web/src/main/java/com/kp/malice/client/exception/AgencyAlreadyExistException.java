package com.kp.malice.client.exception;

public class AgencyAlreadyExistException extends Exception {
    private static final long serialVersionUID = -6493837668905256843L;

    public AgencyAlreadyExistException(String maliceCode, String lloydsCode, String rmaCode) {
        super("Agency with maliceCode " + maliceCode + " and/or lloydsCode "+ lloydsCode+ " and/or rmaCode "+ rmaCode+" already exist. \n(At least one of this code is already associated to another agency)");
    }
}
    