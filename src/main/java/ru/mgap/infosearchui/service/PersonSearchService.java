package ru.mgap.infosearchui.service;

import com.pipl.api.data.Utils;
import com.pipl.api.data.fields.Email;
import com.pipl.api.search.SearchAPIError;
import com.pipl.api.search.SearchAPIRequest;
import com.pipl.api.search.SearchAPIResponse;
import com.pipl.api.search.SearchConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import ru.mgap.infosearchui.dataobject.SearchRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mgap.infosearchui.dataobject.SearchResponse;

@Service
public class PersonSearchService {

    static SearchConfiguration configuration = new SearchConfiguration();

    private final static Logger logger = LoggerFactory.getLogger(PersonSearchService.class);

    static {
        configuration.setProtocol("https");
        configuration.apiKey = "t3og2sc4hd8x9ayvjw1yt72g";
    }

    public SearchAPIResponse search(SearchRequest request) {
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
            savePerson(response);
        }

        return response;
    }

    public SearchResponse getHistoryPerson(long personId) {
        //TODO
        return null;
    }

    private void savePerson(SearchAPIResponse response) {
        //todo
    }

    private void updatePerson(SearchAPIResponse response) {
        //todo
    }
}
