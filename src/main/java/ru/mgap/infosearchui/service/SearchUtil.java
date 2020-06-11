package ru.mgap.infosearchui.service;

import com.pipl.api.data.fields.Email;

public class SearchUtil {
    public static boolean isPhone(String query) {
        String phone = query.replaceAll("[\\D]", "");
        return phone.length() >= 9;
    }

    public static boolean isEmail(String query) {
        Email email = new Email.Builder().address(query).build();
        return email.isValidEmail();
    }
}
