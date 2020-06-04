package ru.mgap.infosearchui.controllers;

import org.springframework.web.bind.annotation.*;
import ru.mgap.infosearchui.dataobject.AuthContext;
import ru.mgap.infosearchui.dataobject.AuthRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class LoginController {

    @PostMapping(value = "/signin")
    public String signin(@RequestBody AuthRequest authRequest,
                         HttpServletRequest request) {

        final HttpSession session = request.getSession(true);

        if ("admin".equals(authRequest.getLogin())) {
            session.setAttribute("authContext", new AuthContext(authRequest.getLogin()));
            return "ok";
        }

        return "auth error";
    }

    @GetMapping(value = "/signout")
    public String signout(HttpServletRequest request) {
        final HttpSession session = request.getSession(true);
        session.setAttribute("authContext", null);
        return "ok";
    }
}
