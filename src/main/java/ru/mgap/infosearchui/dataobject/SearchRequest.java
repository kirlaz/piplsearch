package ru.mgap.infosearchui.dataobject;

import com.pipl.api.data.containers.Person;

public class SearchRequest extends Person {
    private boolean testMode = true;

    public boolean isTestMode() {
        return testMode;
    }

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }


}
