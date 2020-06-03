package ru.mgap.infosearchui.controllers;

import org.springframework.web.bind.annotation.RestController;
import ru.mgap.infosearchui.dataobject.PersonHistoryInfo;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SearchHistoryController {

    public List<PersonHistoryInfo> getHistory() {
        //todo: get user id
        return new ArrayList<>();
    }
}
