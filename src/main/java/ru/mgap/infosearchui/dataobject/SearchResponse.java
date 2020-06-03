package ru.mgap.infosearchui.dataobject;

import com.pipl.api.search.SearchAPIResponse;

import java.util.Date;

public class SearchResponse extends SearchAPIResponse {
    private long personId;
    private String name;
    private String response;
    private Date searchDate;

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }
}
