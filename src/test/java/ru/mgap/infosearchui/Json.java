package ru.mgap.infosearchui;

import com.pipl.api.data.Utils;
import com.pipl.api.data.containers.Person;
import com.pipl.api.data.fields.Address;
import com.pipl.api.data.fields.Field;
import com.pipl.api.data.fields.Job;
import com.pipl.api.data.fields.Name;
import com.pipl.api.search.SearchAPIRequest;
import ru.mgap.infosearchui.dataobject.AuthRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Json {

    public static void main(String[] args) throws IOException {

        SearchAPIRequest request = new SearchAPIRequest.Builder()
                .email("clark.kent@example.com")
                .firstName("Kirill")
                .lastName("Lazarenko")
                .phone("+79265786260")
                .rawAddress("Moscow, Russia")
                .build();

        String reqJson = Utils.toJson(request.getPerson());
        System.out.println(reqJson);

        AuthRequest authRequest = new AuthRequest();
        authRequest.setLogin("admin");
        authRequest.setPassword("password");

        String authRequestJson = Utils.toJson(authRequest);
        System.out.println(authRequestJson);

        List<Field> fields = new ArrayList<Field>();
        fields.add(new Name.Builder().first("Clark").last("Kent").build());
        fields.add(new Address.Builder().country("US").state("KS").city("Smallville").build());
        fields.add(new Address.Builder().country("US").state("KS").city("Metropolis").build());
        fields.add(new Job.Builder().title("Field Reporter").build());
        Person person = new Person(fields);
    }
}
