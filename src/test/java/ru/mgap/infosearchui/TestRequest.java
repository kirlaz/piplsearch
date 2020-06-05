package ru.mgap.infosearchui;

import com.pipl.api.data.Utils;
import com.pipl.api.search.SearchAPIError;
import com.pipl.api.search.SearchAPIRequest;
import com.pipl.api.search.SearchAPIResponse;
import com.pipl.api.search.SearchConfiguration;

import java.io.IOException;
import java.net.URLEncoder;

public class TestRequest {

    public static void main(String[] args) throws IOException {

        SearchConfiguration configuration = new SearchConfiguration();
        configuration.setProtocol("https");
        configuration.apiKey = "t3og2sc4hd8x9ayvjw1yt72g";

        SearchAPIRequest request = new SearchAPIRequest.Builder()
                .email("clark.kent@example.com")
                .firstName("Kirill")
                .lastName("Lazarenko")
                //.phone("+79265786260")
                .rawAddress("Moscow, Russia")
                .configuration(configuration).build();

        System.out.println(request.getPerson().getJobs());

        //SearchAPIRequest request = new SearchAPIRequest.Builder().phone("+79265786260").configuration(configuration).build();

        SearchAPIResponse response = null;
        try {
            response = request.send();
        } catch (SearchAPIError | IOException e) {
            System.out.println(e.getMessage());
        }

        String responseJson = Utils.toJson(response);
        System.out.println(responseJson);


        System.out.println(response.name());
        // Output: "Clark Joseph Kent"

        System.out.println(response.gender());
        // Output: "Male"

        System.out.println(response.address());
        // Output: "10-1 Hickory Lane, Smallville, Kansas"

        System.out.println(response.job());
        // Output: "Field Reporter at The Daily Planet (2000-2012)"

    }
}
