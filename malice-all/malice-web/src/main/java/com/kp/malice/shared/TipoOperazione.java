package com.kp.malice.shared;



public enum TipoOperazione {
    INCASSO, STORNO;

    public static TipoOperazione fromString(String s) {
        for (TipoOperazione tp : TipoOperazione.values()) {
            if (tp.name().equals(s)) {
                return tp;
            }
        }
        return null;
    }
    
}
