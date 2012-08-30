package com.kp.malice.businessRules;

public enum PvUser {
    PV_OWNER, PV_DELEGATE, PV_BOTH;

    public static PvUser fromString(String s) {
        for (PvUser enu : values()) {
            if (enu.name().equals(s))
                return enu;
        }
        return null;
    }

}
