package ru.mgap.infosearchui.dataobject;

import java.util.List;

public class HistoryResponse {
    private List<SearchResponsePreview> records;
    private int currentPage;
    private int pageCount;
    private int pageSize;

    public List<SearchResponsePreview> getRecords() {
        return records;
    }

    public void setRecords(List<SearchResponsePreview> records) {
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
