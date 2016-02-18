package com.gotra.kbdt.core.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gotra
 */

public class DataJournal {
    Map<Integer, SessionData> data;

    public DataJournal() {
        data = new HashMap<Integer, SessionData>();
    }

    public SessionData getSessionData(int id) {
        return data.get(id);
    }

    public void setSessionData(int id, SessionData sessionData) {
        data.put(id, sessionData);
    }
}
