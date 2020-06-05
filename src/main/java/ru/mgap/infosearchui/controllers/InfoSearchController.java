package ru.mgap.infosearchui.controllers;

import com.pipl.api.data.Utils;
import com.pipl.api.search.SearchAPIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.mgap.infosearchui.dataobject.AuthContext;
import ru.mgap.infosearchui.dataobject.HistoryRequest;
import ru.mgap.infosearchui.dataobject.SearchRequest;
import ru.mgap.infosearchui.dataobject.SearchResponsePreview;
import ru.mgap.infosearchui.entity.SearchHistory;
import ru.mgap.infosearchui.exception.AuthException;
import ru.mgap.infosearchui.service.PersonSearchService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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

        AuthContext authContext = checkAuth(request);

        SearchAPIResponse response = personSearchService.search(searchRequest, authContext);
        return response;
    }

    @GetMapping("/getFromHistory")
    public SearchAPIResponse getSavedPerson(@RequestParam long searchHistoryId,
                                        HttpServletRequest request) throws IOException {
        checkAuth(request);

        SearchHistory historyPerson = personSearchService.getHistoryPerson(searchHistoryId);
        SearchAPIResponse searchAPIResponse = (SearchAPIResponse) Utils.fromJson(historyPerson.getResponseRaw(), SearchAPIResponse.class);

        return searchAPIResponse;
    }

    @GetMapping("/topHistory")
    public List<SearchResponsePreview> getHistory(HttpServletRequest request) {
        AuthContext authContext = checkAuth(request);

        List<SearchHistory> searchHistoryList = personSearchService.getHistory(authContext.getLogin());
        List<SearchResponsePreview> searchResponsePreviews = searchHistoryList.stream()
                .map(h -> new SearchResponsePreview(h))
                .limit(5)
                .collect(Collectors.toList());
        return searchResponsePreviews;
    }

    @PostMapping(value = "/history", consumes = "application/json", produces = "application/json")
    public List<SearchResponsePreview> getHistory(
            @RequestBody HistoryRequest historyRequest,
            HttpServletRequest request) {

        AuthContext authContext = checkAuth(request);

        List<SearchHistory> searchHistoryList = personSearchService.getHistory(authContext.getLogin());
        List<SearchResponsePreview> searchResponsePreviews = searchHistoryList.stream()
                .map(h -> new SearchResponsePreview(h))
                .collect(Collectors.toList());
        return searchResponsePreviews;
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
