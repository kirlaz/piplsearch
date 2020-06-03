package ru.mgap.infosearchui.controllers;

import com.pipl.api.search.SearchAPIResponse;
import org.springframework.web.bind.annotation.*;
import ru.mgap.infosearchui.dataobject.SearchRequest;
import ru.mgap.infosearchui.service.PersonSearchService;

@RestController
public class InfoSearchController {

    private final PersonSearchService personSearchService;

    public InfoSearchController(PersonSearchService personSearchService) {
        this.personSearchService = personSearchService;
    }

    @PostMapping(value = "/search", consumes = "application/json", produces = "application/json")
    public SearchAPIResponse search(@RequestBody SearchRequest searchRequest) {
        SearchAPIResponse response = personSearchService.search(searchRequest);
        //SearchAPIResponse response = new SearchAPIResponse();
        return response;
    }

    @GetMapping("/getPerson")
    public SearchAPIResponse getPerson(@RequestParam long personId) {
        SearchAPIResponse response = personSearchService.getHistoryPerson(personId);
        return response;
    }

    @GetMapping("/refreshPerson")
    public SearchAPIResponse refreshPerson(@RequestParam long personId) {
        SearchAPIResponse response = personSearchService.refreshPerson(personId);
        return response;
    }


}
