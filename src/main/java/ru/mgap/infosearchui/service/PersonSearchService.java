package ru.mgap.infosearchui.service;

import com.pipl.api.data.Utils;
import com.pipl.api.data.fields.Email;
import com.pipl.api.search.SearchAPIError;
import com.pipl.api.search.SearchAPIRequest;
import com.pipl.api.search.SearchAPIResponse;
import com.pipl.api.search.SearchConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import ru.mgap.infosearchui.dataobject.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mgap.infosearchui.entity.SearchHistory;
import ru.mgap.infosearchui.entity.User;
import ru.mgap.infosearchui.repositories.SearchHistoryRepository;

@Service
public class PersonSearchService {

    static SearchConfiguration configuration = new SearchConfiguration();

    private final static Logger logger = LoggerFactory.getLogger(PersonSearchService.class);

    private SearchHistoryRepository searchHistoryRepository;

    @Autowired
    public PersonSearchService(SearchHistoryRepository searchHistoryRepository) {
        this.searchHistoryRepository = searchHistoryRepository;
    }

    static {
        configuration.setProtocol("https");
        configuration.apiKey = "t3og2sc4hd8x9ayvjw1yt72g";
    }

    public SearchAPIResponse search(SearchRequest request, AuthContext authContext) {
        SearchAPIRequest searchAPIRequest = new SearchAPIRequest(request, configuration);

        SearchAPIResponse response = null;

        ArrayList<Email> emails = new ArrayList<>();
        emails.add(new Email.Builder().address("clark.kent@example.com").build());
        emails.addAll(searchAPIRequest.getPerson().getEmails());
        searchAPIRequest.getPerson().setEmails(emails);

        try {
            logger.info("request: {}", Utils.toJson(request));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            response = searchAPIRequest.send();
        } catch (SearchAPIError searchAPIError) {
            throw new RuntimeException(searchAPIError.getError());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            logger.info("response: {}", Utils.toJson(response));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response.getPerson() != null) {
            savePerson(response, authContext);
        }

        return response;
    }

    public SearchHistory getHistoryPerson(Long searchHistoryId) {
        Optional<SearchHistory> searchHistory = searchHistoryRepository.findById(searchHistoryId);
        return searchHistory.get();
    }

    private void savePerson(SearchAPIResponse response, AuthContext authContext) {
        SearchHistory searchHistory = new SearchHistory();
        String name = "none";
        if (!response.getPerson().getNames().isEmpty()) {
            name = response.getPerson().getNames().get(0).getDisplay();
        }
        searchHistory.setName(name);
        searchHistory.setLogin(authContext.getLogin());
        searchHistory.setSearchDate(new Date());
        searchHistory.setResponseRaw(response.getJson());
        searchHistoryRepository.save(searchHistory);
    }

    public List<SearchHistory> getHistory(String login) {
        List<SearchHistory> searchHistoryList = searchHistoryRepository.findByLogin(login);
        return searchHistoryList;
    }

    public HistoryResponse getHistory(HistoryRequest request) {
        int start = request.getPage() * request.getPageSize();
        int end = start + request.getPageSize();

//        List<SearchHistory> searchHistoryList = searchHistoryRepository.findByParams(
//                request.getStartDate(), request.getEndDate(), request.getUserLogin(), start, end);


        HistoryResponse historyResponse = new HistoryResponse();

        return historyResponse;
    }
}
