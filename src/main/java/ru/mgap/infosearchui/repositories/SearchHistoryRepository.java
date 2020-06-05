package ru.mgap.infosearchui.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.mgap.infosearchui.entity.SearchHistory;

import java.util.Date;
import java.util.List;

public interface SearchHistoryRepository extends CrudRepository<SearchHistory, Long> {

    List<SearchHistory> findByLogin(String login);

    /*
    @Query("select h from SearchHistory h where h.login = ?1 and h.searchDate>?2 and h.searchDate<?3 limit ")
    List<SearchHistory> findByParams(String userLogin, Date startDate, Date endDate, int start, int end);
     */
}
