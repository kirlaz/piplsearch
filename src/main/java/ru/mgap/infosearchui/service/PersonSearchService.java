package ru.mgap.infosearchui.service;

import com.pipl.api.data.Utils;
import com.pipl.api.data.fields.Address;
import com.pipl.api.data.fields.Email;
import com.pipl.api.data.fields.Name;
import com.pipl.api.data.fields.Phone;
import com.pipl.api.search.SearchAPIError;
import com.pipl.api.search.SearchAPIRequest;
import com.pipl.api.search.SearchAPIResponse;
import com.pipl.api.search.SearchConfiguration;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.mgap.infosearchui.dataobject.AuthContext;
import ru.mgap.infosearchui.dataobject.HistoryRequest;
import ru.mgap.infosearchui.dataobject.SearchRequest;
import ru.mgap.infosearchui.entity.SearchHistory;
import ru.mgap.infosearchui.exception.ServerError;
import ru.mgap.infosearchui.repositories.SearchHistoryRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

        if (request.isTestMode()) {
            ArrayList<Email> emails = new ArrayList<>();
            emails.add(new Email.Builder().address("clark.kent@example.com").build());
            emails.addAll(searchAPIRequest.getPerson().getEmails());
            searchAPIRequest.getPerson().setEmails(emails);
        }

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

        savePerson(response, authContext);

        return response;
    }

    public SearchAPIResponse searchOneLine(String query, String location, AuthContext authContext) {
        SearchRequest request = new SearchRequest();
        if (SearchUtil.isPhone(query)) {
            request.phones.add(new Phone(query));
        } else if (SearchUtil.isEmail(query)) {
            request.emails.add(new Email.Builder().address(query).build());
        } else {
            request.names.add(new Name(query));
        }

        if (location != null) {
            request.addresses.add(new Address(location));
        }

        request.setTestMode(true);
        return search(request, authContext);
    }

    public SearchHistory getHistoryPerson(Long searchHistoryId) {
        Optional<SearchHistory> searchHistory = searchHistoryRepository.findById(searchHistoryId);

        if (!searchHistory.isPresent())
            throw new ServerError("Value for searchHistoryId not found: " + searchHistoryId);

        return searchHistory.get();
    }

    private void savePerson(SearchAPIResponse response, AuthContext authContext) {
        SearchHistory searchHistory = new SearchHistory();
        String name = "none";

        if (response.getPerson() == null) {
            name = "List of possible persons";
        } else if (!response.getPerson().getNames().isEmpty()) {
            name = response.getPerson().getNames().get(0).getDisplay();
        }

        searchHistory.setName(name);
        searchHistory.setLogin(authContext.getLogin());
        searchHistory.setSearchDate(new Date());
        searchHistory.setResponseRaw(response.getJson());

        if (response.getPerson() != null && !response.getPerson().images.isEmpty()) {
            String imgUrl = response.getPerson().images.get(0).getThumbnailUrl(180, 180, false, true, false);
            try {
                searchHistory.setImgUrl(imgUrl);
                searchHistory.setImg(getBase64EncodedImage(imgUrl));
            } catch (IOException e) {
                throw new ServerError(e);
            }
        }

        searchHistoryRepository.save(searchHistory);
    }

    public List<SearchHistory> getHistory(String login) {
        List<SearchHistory> searchHistoryList = searchHistoryRepository.findByLoginOrderBySearchHistoryIdDesc(login);
        return searchHistoryList;
    }

    public Page<SearchHistory> getHistoryPage(HistoryRequest request) {
        Page<SearchHistory> searchHistoryPage = searchHistoryRepository.findByLoginBetweenStartAndEndDateOrderBySearchHistoryIdDesc(
                request.getUserLogin(), request.getStartDate(), request.getEndDate(),
                PageRequest.of(request.getCurrentPage(), request.getPageSize(), Sort.by(Sort.Direction.DESC, "searchHistoryId")));
        return searchHistoryPage;
    }

    public String getBase64EncodedImage(String imageURL) throws IOException {
        java.net.URL url = new java.net.URL(imageURL);
        InputStream is = url.openStream();
        byte[] bytes = org.apache.commons.io.IOUtils.toByteArray(is);
        return Base64.encodeBase64String(bytes);
    }


}
