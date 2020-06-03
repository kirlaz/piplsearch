package ru.mgap.infosearchui.entity;

import java.util.Date;

public class PersonHistoryData {
    private long localPersonId;
    private String name;
    private String responseRaw;
    private Date searchDate;

    public long getLocalPersonId() {
        return localPersonId;
    }

    public void setLocalPersonId(long localPersonId) {
        this.localPersonId = localPersonId;
    }

}
