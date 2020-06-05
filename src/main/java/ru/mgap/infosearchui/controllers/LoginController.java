package ru.mgap.infosearchui.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.mgap.infosearchui.dataobject.AuthContext;
import ru.mgap.infosearchui.dataobject.AuthRequest;
import ru.mgap.infosearchui.entity.User;
import ru.mgap.infosearchui.repositories.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
public class LoginController {

    private final static Logger logger = LoggerFactory.getLogger(LoginController.class);

    private UserRepository userRepository;

    @Autowired
    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/signin")
    public String signin(@RequestBody AuthRequest authRequest,
                         HttpServletRequest request) {

        final HttpSession session = request.getSession(true);

        if ("admin".equals(authRequest.getLogin())) {
            session.setAttribute("authContext", new AuthContext(authRequest.getLogin()));
            return "ok";
        } else {
            Optional<User> user = userRepository.findById(authRequest.getLogin());

            if (!user.isPresent()) {
                logger.warn("User {} not found", authRequest.getLogin());
                return "auth_error";
            }

            if (!user.get().getPassword().equals(authRequest.getPassword())) {
                logger.warn("User {} incorrect password", authRequest.getLogin());
                return "auth_error";
            }

            session.setAttribute("authContext", new AuthContext(authRequest.getLogin()));
            return "ok";
        }
    }

    @GetMapping(value = "/signout")
    public String signout(HttpServletRequest request) {
        final HttpSession session = request.getSession(true);
        session.setAttribute("authContext", null);
        return "ok";
    }
}
