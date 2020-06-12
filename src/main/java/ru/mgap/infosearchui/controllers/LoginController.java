package ru.mgap.infosearchui.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.mgap.infosearchui.dataobject.AuthContext;
import ru.mgap.infosearchui.dataobject.AuthRequest;
import ru.mgap.infosearchui.entity.User;
import ru.mgap.infosearchui.exception.AuthException;
import ru.mgap.infosearchui.repositories.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(allowCredentials = "true")
@RestController
public class LoginController {

    private final static Logger logger = LoggerFactory.getLogger(LoginController.class);

    private UserRepository userRepository;

    @Autowired
    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/signin")
    public User signin(@RequestBody AuthRequest authRequest,
                       HttpServletRequest request) {

        final HttpSession session = request.getSession(true);

        Optional<User> possibleUser = userRepository.findById(authRequest.getLogin());

        if (!possibleUser.isPresent()) {
            logger.warn("User {} not found", authRequest.getLogin());
            throw new AuthException("User not found");
        }

        if (!possibleUser.get().getPassword().equals(authRequest.getPassword())) {
            logger.warn("User {} incorrect password", authRequest.getLogin());
            throw new AuthException("Incorrect password");
        }

        logger.info("User {} auth success", authRequest.getLogin());
        session.setAttribute("authContext", new AuthContext(authRequest.getLogin()));

        User user = possibleUser.get();
        user.setPassword(null);
        return user;
    }

    @GetMapping(value = "/signout")
    public String signout(HttpServletRequest request) {
        AuthContext authContext = SecUtils.checkAuth(request);
        final HttpSession session = request.getSession(true);
        logger.info("User {} auth success", authContext.getLogin());
        session.setAttribute("authContext", null);
        return "ok";
    }

    @GetMapping(value = "/users")
    public List<User> users(HttpServletRequest request) {
        AuthContext authContext = SecUtils.checkAuth(request);

        Iterable<User> usersIterable = userRepository.findAll();
        usersIterable.forEach(u->u.setPassword(null));
        List<User> result = new ArrayList<>();
        usersIterable.forEach(result::add);
        return result;
    }
}
