package ru.mgap.infosearchui.controllers;

import com.pipl.api.data.Utils;
import com.pipl.api.search.SearchAPIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.mgap.infosearchui.dataobject.AuthContext;
import ru.mgap.infosearchui.dataobject.HistoryRequest;
import ru.mgap.infosearchui.dataobject.HistoryResponse;
import ru.mgap.infosearchui.dataobject.SearchRequest;
import ru.mgap.infosearchui.entity.SearchHistory;
import ru.mgap.infosearchui.service.PersonSearchService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@CrossOrigin(allowCredentials = "true")
@RestController
public class InfoSearchController {

    private final static Logger logger = LoggerFactory.getLogger(InfoSearchController.class);

    private final PersonSearchService personSearchService;

    public InfoSearchController(PersonSearchService personSearchService) {
        this.personSearchService = personSearchService;
    }

    @GetMapping(value = "/oneLineSearch")
    public SearchAPIResponse oneLineSearch(@RequestParam String query,
                                           @RequestParam(required = false) String location,
                                           HttpServletRequest request) {
        AuthContext authContext = SecUtils.checkAuth(request);
        logger.info("User {} oneLineSearch", authContext.getLogin());
        return personSearchService.searchOneLine(query, location, authContext);
    }

    @PostMapping(value = "/search", consumes = "application/json", produces = "application/json")
    public SearchAPIResponse search(@RequestBody SearchRequest searchRequest,
                                    HttpServletRequest request) {
        AuthContext authContext = SecUtils.checkAuth(request);
        logger.info("User {} search", authContext.getLogin());
        SearchAPIResponse response = personSearchService.search(searchRequest, authContext);
        return response;
    }

    @GetMapping("/getFromHistory")
    public SearchAPIResponse getSavedPerson(@RequestParam long searchHistoryId,
                                            HttpServletRequest request) throws IOException {
        AuthContext authContext = SecUtils.checkAuth(request);
        logger.info("User {} getSavedPerson {}", authContext.getLogin(), searchHistoryId);

        SearchHistory historyPerson = personSearchService.getHistoryPerson(searchHistoryId);
        SearchAPIResponse searchAPIResponse = (SearchAPIResponse) Utils.fromJson(
                historyPerson.getResponseRaw(),
                SearchAPIResponse.class);

        return searchAPIResponse;
    }

    @GetMapping("/topHistory")
    public List<SearchHistory> topHistory(HttpServletRequest request) {
        AuthContext authContext = SecUtils.checkAuth(request);
        logger.info("User {} topHistory", authContext.getLogin());

        HistoryRequest historyRequest = new HistoryRequest();
        historyRequest.setPageSize(5);
        historyRequest.setUserLogin(authContext.getLogin());
        Page<SearchHistory> searchHistoryPage = personSearchService.getHistoryPage(historyRequest);
        searchHistoryPage.getContent().forEach(h -> h.prepareForFrontend(false));
        return searchHistoryPage.getContent();
    }

    @PostMapping(value = "/history", consumes = "application/json", produces = "application/json")
    public HistoryResponse getHistory(
            @RequestBody HistoryRequest historyRequest,
            HttpServletRequest request) {

        AuthContext authContext = SecUtils.checkAuth(request);
        logger.info("User {} getHistory", authContext.getLogin());

        Page<SearchHistory> searchHistoryPage = personSearchService.getHistoryPage(historyRequest);
        searchHistoryPage.getContent().forEach(h -> h.prepareForFrontend(true));

        HistoryResponse historyResponse = new HistoryResponse();
        historyResponse.setRecords(searchHistoryPage.getContent());
        historyResponse.setCurrentPage(searchHistoryPage.getNumber());
        historyResponse.setPageCount(searchHistoryPage.getTotalPages());
        historyResponse.setPageSize(historyRequest.getPageSize());
        return historyResponse;
    }


}
