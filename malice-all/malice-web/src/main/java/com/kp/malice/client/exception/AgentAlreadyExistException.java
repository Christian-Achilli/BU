package com.kp.malice.client.exception;

public class AgentAlreadyExistException extends Exception {
    private static final long serialVersionUID = 4956247373934135317L;
    private String username;

    public AgentAlreadyExistException(String username) {
        super("Agent " + username + " already exist.");
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}