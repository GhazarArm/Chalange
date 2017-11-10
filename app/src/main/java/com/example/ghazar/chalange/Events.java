package com.example.ghazar.chalange;

/**
 * Created by ghazar on 11/10/17.
 */

public class Events {
    private String m_eventKey;
    private String m_eventText;

    public final static String FREND_REQUEST_EVENT_KEY = "FrendRequest";
    public final static String FREND_REQUEST_EVENT_TEXT = "Hi))... Do you want to be my frend?";

    public final static String CHALANGE_REQUEST_EVENT_KEY = "ChalangeRequest";
    public final static String CHALANGE_REQUEST_EVENT_TEXT = "Hi))... i want beat you ))";

    public Events(String eventKey, String eventText)
    {
        m_eventKey = eventKey;
        m_eventText = eventText;
    }

    public String getM_eventKey() {
        return m_eventKey;
    }

    public String getM_eventText() {
        return m_eventText;
    }

    public void setM_eventKey(String m_eventKey) {
        this.m_eventKey = m_eventKey;
    }

    public void setM_eventText(String m_eventText) {
        this.m_eventText = m_eventText;
    }
}
