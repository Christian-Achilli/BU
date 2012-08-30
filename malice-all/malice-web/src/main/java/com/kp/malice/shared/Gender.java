package com.kp.malice.shared;

public enum Gender {
    M, F, C;

    public static Gender fromString(String s) {
        for (Gender g : values()) {
            if (g.name().equals(s)) {
                return g;
            }
        }
        return null;
    }

}