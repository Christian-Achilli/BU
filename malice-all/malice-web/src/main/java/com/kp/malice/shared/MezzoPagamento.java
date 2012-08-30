package com.kp.malice.shared;

public enum MezzoPagamento {
    CONTANTI, BONIFICO, ASSEGNO, BANCOMAT;

    public static MezzoPagamento fromString(String s) {
        for (MezzoPagamento mp : MezzoPagamento.values()) {
            if (mp.name().equals(s)) {
                return mp;
            }
        }
        return null;
    }

}