package com.gotra.kbdt.core.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author gotra
 */

public class SessionData {
    private int clientId;
    private Date sessionStartTime;
    private Date sessionEndTime;
    private long sessionTimeMillis;
    private List<RequestResponsePair> conversationLog;

    public SessionData() {
    }

    public SessionData(int clientId, Date sessionStartTime) {
        this.clientId = clientId;
        this.sessionStartTime = sessionStartTime;
        conversationLog = new ArrayList<RequestResponsePair>();
    }

    public int getClientId() {
        return clientId;
    }

    public Date getSessionStartTime() {
        return sessionStartTime;
    }

    public Date getSessionEndTime() {
        return sessionEndTime;
    }

    public long getSessionTimeMillis() {
        if (sessionEndTime == null) {
            return System.currentTimeMillis() - sessionStartTime.getTime();
        }
        return sessionTimeMillis;
    }

    public void setSessionEndTime(Date sessionEndTime) {
        this.sessionEndTime = sessionEndTime;
        sessionTimeMillis = sessionEndTime.getTime() - sessionStartTime.getTime();
    }

    public List<RequestResponsePair> getConversationLog() {
        return conversationLog;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionData that = (SessionData) o;

        if (clientId != that.clientId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return clientId;
    }
}
