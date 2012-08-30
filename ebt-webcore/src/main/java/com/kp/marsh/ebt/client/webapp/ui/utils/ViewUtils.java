package com.kp.marsh.ebt.client.webapp.ui.utils;

import com.google.gwt.regexp.shared.RegExp;

public class ViewUtils {

    /**
     * @param val
     * @return il valore val in migliaia di euro approssimato ad una cifra decimale per le centinaia di euro
     */
    public static String format(int val) {
        double num = val / 1000d;
        return "" + num;
    }

    /**
     * @param value
     * @return  il valore val in migliaia di euro approssimato ad una cifra decimale per le centinaia di euro se value è una stringa numerica, altrimenti ritorna value
     */
    public static String format(String value) {
        if (RegExp.compile("[1-9]\\d{0,}").test(value)) {
            return format(Integer.valueOf(value));
        } else
            return value;
    }

    /**
     * @param kEuro
     * @return la stringa in euro da quella in keuro. La stringa in keuro può avere la virgola (punto). Per diventre euro deve essere moltiplicata per 1000.
     */
    public static String decodeToEuro(String kEuro) {
        if (null != kEuro && !"".equals(kEuro)) {
            int k = 100;
            if (!kEuro.contains(".")) {
                k = 1000;
            } else {
                kEuro = kEuro.replace(".", "");
            }
            int kInt = Integer.valueOf(kEuro) * k;
            return "" + kInt;
        }
        return "";
    }

    //	
    //	public static void main(String[] args) {
    //		System.out.println(decodeToEuro("999.3"));
    //	}
    //	
}
