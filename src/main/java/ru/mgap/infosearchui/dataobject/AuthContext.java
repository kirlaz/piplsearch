package ru.mgap.infosearchui.dataobject;

public class AuthContext {
    private String login;

    public AuthContext(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
