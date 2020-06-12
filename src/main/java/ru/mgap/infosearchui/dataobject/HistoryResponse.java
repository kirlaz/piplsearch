package ru.mgap.infosearchui.dataobject;

import ru.mgap.infosearchui.entity.SearchHistory;

import java.util.List;

public class HistoryResponse {
    private List<SearchHistory> records;
    private int currentPage;
    private int pageCount;
    private int pageSize;

    public List<SearchHistory> getRecords() {
        return records;
    }

    public void setRecords(List<SearchHistory> records) {
        this.records = records;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
