package com.kp.marsh.ebt.client.webapp.ui.validators;

import com.google.gwt.regexp.shared.RegExp;

/**
 * @author christianachilli
 * Check the insert potential pop up input to check whether it's value is well formatted
 */
public final class InsertPopUpInputVerifier {

    public static String VALID_INPUT_STRING = "((^[1-9]{1}[0-9]{0,4}$)|(^[1-9][0-9]{0,2}\\.[0-9]$)|(^0\\.[1-9]$))";

    public static boolean isValidInput(String input) {
        if (input.isEmpty() || RegExp.compile(VALID_INPUT_STRING).test(input)) {
            return true;
        }
        return false;
    }

    public static boolean isValidChar(String input) {
        if (RegExp.compile("[0-9]+\\.?[0-9]?").test(input)) {
            return true;
        }
        return false;
    }

}
