package ru.mgap.infosearchui.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import ru.mgap.infosearchui.entity.SearchHistory;

import java.util.Date;
import java.util.List;

public interface SearchHistoryRepository extends PagingAndSortingRepository<SearchHistory, Long> {

    List<SearchHistory> findByLogin(String login);

    @Query("select h from SearchHistory h " +
            "where (:userLogin is null or h.login = :userLogin) " +
            "and (:startDate is null or h.searchDate>:startDate) " +
            "and (:endDate is null or h.searchDate<:endDate)")
    Page<SearchHistory> findByLoginBetweenStartAndEndDate(
            @Param("userLogin") String userLogin,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            Pageable pageRequest);
}
