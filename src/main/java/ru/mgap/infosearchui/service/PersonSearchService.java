package ru.mgap.infosearchui.service;

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

@Service
public class PersonSearchService {

    static SearchConfiguration configuration = new SearchConfiguration();

    private final static Logger logger = LoggerFactory.getLogger(PersonSearchService.class);

    static {
        configuration.setProtocol("https");
        configuration.apiKey = "t3og2sc4hd8x9ayvjw1yt72g";
    }

    @GetMapping("/")
    public String index() {
        logger.trace("A TRACE Message");
        logger.debug("A DEBUG Message");
        logger.info("An INFO Message");
        logger.warn("A WARN Message");
        logger.error("An ERROR Message");

        return "Howdy! Check out the Logs to see the output...";
    }

    public SearchAPIResponse search(SearchRequest request) {
        SearchAPIRequest searchAPIRequest = new SearchAPIRequest(request, configuration);

        SearchAPIResponse response = null;

        ArrayList<Email> emails = new ArrayList<>();
        emails.add(new Email.Builder().address("clark.kent@example.com").build());
        emails.addAll(searchAPIRequest.getPerson().getEmails());
        searchAPIRequest.getPerson().setEmails(emails);

        try {
            response = searchAPIRequest.send();
        } catch (SearchAPIError searchAPIError) {
            throw new RuntimeException(searchAPIError.getError());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (response.getPerson() != null) {
            savePerson(response);
        }

        return response;
    }

    public SearchAPIResponse getHistoryPerson(long personId) {
        //TODO
        return null;
    }

    public SearchAPIResponse refreshPerson(long personId) {
        SearchAPIResponse historyData = getHistoryPerson(personId);

        SearchRequest request = new SearchRequest();
        request.setSearchPointer(historyData.getPerson().getSearchPointer());
        SearchAPIResponse response = search(request);

        return response;
    }

    private void savePerson(SearchAPIResponse response) {
        //todo
    }

    private void updatePerson(SearchAPIResponse response) {
        //todo
    }
}
