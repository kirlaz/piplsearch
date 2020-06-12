package ru.mgap.infosearchui.controllers;

import ru.mgap.infosearchui.dataobject.AuthContext;
import ru.mgap.infosearchui.exception.AuthException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SecUtils {

    public static AuthContext checkAuth(HttpServletRequest request) {
        final HttpSession session = request.getSession(true);
        AuthContext authContext = (AuthContext) session.getAttribute("authContext");
        if (authContext == null) {
            throw new AuthException("not authenticated");
        }
        return authContext;
    }
}
