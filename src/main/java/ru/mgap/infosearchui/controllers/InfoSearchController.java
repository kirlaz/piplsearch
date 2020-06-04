package ru.mgap.infosearchui.controllers;

import com.pipl.api.search.SearchAPIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.mgap.infosearchui.dataobject.AuthContext;
import ru.mgap.infosearchui.dataobject.SearchRequest;
import ru.mgap.infosearchui.dataobject.SearchResponsePreview;
import ru.mgap.infosearchui.exception.AuthException;
import ru.mgap.infosearchui.service.PersonSearchService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
public class InfoSearchController {

    private final static Logger logger = LoggerFactory.getLogger(InfoSearchController.class);

    private final PersonSearchService personSearchService;

    public InfoSearchController(PersonSearchService personSearchService) {
        this.personSearchService = personSearchService;
    }

    @PostMapping(value = "/search", consumes = "application/json", produces = "application/json")
    public SearchAPIResponse search(@RequestBody SearchRequest searchRequest,
                                    HttpServletRequest request) {

        checkAuth(request);

        SearchAPIResponse response = personSearchService.search(searchRequest);
        return response;
    }

    @GetMapping("/getSavedPerson")
    public SearchAPIResponse getSavedPerson(@RequestParam long personId,
                                            HttpServletRequest request) {

        checkAuth(request);

        SearchAPIResponse response = personSearchService.getHistoryPerson(personId);
        return response;
    }

    @GetMapping("/history")
    public List<SearchResponsePreview> getHistory(HttpServletRequest request) {
        checkAuth(request);

        //todo
        return new ArrayList<>();
    }

    private AuthContext checkAuth(HttpServletRequest request) {
        final HttpSession session = request.getSession(true);
        AuthContext authContext = (AuthContext) session.getAttribute("authContext");
        if (authContext == null) {
            throw new AuthException();
        }
        return authContext;
    }

}
