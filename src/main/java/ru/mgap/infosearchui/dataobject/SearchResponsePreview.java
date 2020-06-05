package ru.mgap.infosearchui.dataobject;

import ru.mgap.infosearchui.entity.SearchHistory;

public class SearchResponsePreview {
    private long searchHistoryId;
    private String name;

    public SearchResponsePreview(SearchHistory searchHistory) {
        this(searchHistory.getSearchHistoryId(), searchHistory.getName());
    }

    public SearchResponsePreview(long personId, String name) {
        this.searchHistoryId = personId;
        this.name = name;
    }

    public long getSearchHistoryId() {
        return searchHistoryId;
    }

    public void setSearchHistoryId(long searchHistoryId) {
        this.searchHistoryId = searchHistoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
