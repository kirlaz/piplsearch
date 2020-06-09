package ru.mgap.infosearchui.controllers;

import com.pipl.api.data.Utils;
import com.pipl.api.search.SearchAPIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.mgap.infosearchui.dataobject.*;
import ru.mgap.infosearchui.entity.SearchHistory;
import ru.mgap.infosearchui.exception.AuthException;
import ru.mgap.infosearchui.service.PersonSearchService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

//@CrossOrigin
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

        HistoryRequest historyRequest = new HistoryRequest();
        historyRequest.setPageSize(5);
        historyRequest.setUserLogin(authContext.getLogin());
        Page<SearchHistory> searchHistoryPage = personSearchService.getHistoryPage(historyRequest);
        List<SearchResponsePreview> searchResponsePreviews = searchHistoryPage.getContent().stream()
                .map(SearchResponsePreview::new)
                .collect(Collectors.toList());
        return searchResponsePreviews;
    }

    @PostMapping(value = "/history", consumes = "application/json", produces = "application/json")
    public HistoryResponse getHistory(
            @RequestBody HistoryRequest historyRequest,
            HttpServletRequest request) {

        AuthContext authContext = checkAuth(request);

        Page<SearchHistory> searchHistoryPage = personSearchService.getHistoryPage(historyRequest);
        List<SearchResponsePreview> searchResponsePreviews = searchHistoryPage.stream()
                .map(SearchResponsePreview::new)
                .collect(Collectors.toList());

        HistoryResponse historyResponse = new HistoryResponse();
        historyResponse.setRecords(searchResponsePreviews);
        historyResponse.setCurrentPage(searchHistoryPage.getNumber());
        historyResponse.setPageCount(searchHistoryPage.getTotalPages());
        historyResponse.setPageSize(historyRequest.getPageSize());

        return historyResponse;
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
