package ru.mgap.infosearchui.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mgap.infosearchui.dataobject.SearchResponsePreview;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SearchHistoryController {

    @GetMapping("/history")
    public List<SearchResponsePreview> getHistory() {
        //todo: get user id
        return new ArrayList<>();
    }
}
