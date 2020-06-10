package ru.mgap.infosearchui.entity;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.Date;

@Entity
public class SearchHistory {

    @Id @GeneratedValue
    private Long searchHistoryId;

    @Column(nullable = false)
    private String name;

    @Expose(serialize = false, deserialize = false)
    @Lob
    @Column(nullable = false)
    private String responseRaw;

    @Column(nullable = false)
    private Date searchDate;

    @Column(nullable = false)
    private String login;

    @Lob
    @Column(length=1024)
    private String imgUrl;

    public Long getSearchHistoryId() {
        return searchHistoryId;
    }

    public void setSearchHistoryId(Long searchHistoryId) {
        this.searchHistoryId = searchHistoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResponseRaw() {
        return responseRaw;
    }

    public void setResponseRaw(String responseRaw) {
        this.responseRaw = responseRaw;
    }

    public Date getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(Date searchDate) {
        this.searchDate = searchDate;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
