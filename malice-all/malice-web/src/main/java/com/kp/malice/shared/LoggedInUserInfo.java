package com.kp.malice.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LoggedInUserInfo implements IsSerializable {

    private String displayName;
    private String role;

    private LoggedInUserInfo() {
    }

    public LoggedInUserInfo(String displayName, String role) {
        this.role = role;
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getRole() {
        return role;
    }

}
