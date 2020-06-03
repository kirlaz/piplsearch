package ru.mgap.infosearchui;

import com.pipl.api.data.Utils;
import com.pipl.api.search.SearchAPIRequest;

import java.io.IOException;

public class Json {

    public static void main(String[] args) throws IOException {

        SearchAPIRequest request = new SearchAPIRequest.Builder()
                .email("clark.kent@example.com")
                .firstName("Kirill")
                .lastName("Lazarenko")
                //.phone("+79265786260")
                .rawAddress("Moscow, Russia")
                .build();

        String reqJson = Utils.toJson(request.getPerson());
        System.out.println(reqJson);
    }
}
