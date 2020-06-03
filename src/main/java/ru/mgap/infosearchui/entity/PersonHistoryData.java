package ru.mgap.infosearchui.entity;

import com.pipl.api.search.SearchAPIResponse;

public class PersonHistoryData {
    private long localPersonId;
    private SearchAPIResponse response;

    public long getLocalPersonId() {
        return localPersonId;
    }

    public void setLocalPersonId(long localPersonId) {
        this.localPersonId = localPersonId;
    }

    public SearchAPIResponse getResponse() {
        return response;
    }

    public void setResponse(SearchAPIResponse response) {
        this.response = response;
    }
}
