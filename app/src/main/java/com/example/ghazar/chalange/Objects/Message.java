package com.example.ghazar.chalange.Objects;

import java.sql.Time;
import java.util.Date;

public class Message {
    private String m_fromUser;
    private String m_text;
    private String   m_time;

    Message(String fUser, String text)
    {
        m_text = text;
        m_fromUser = fUser;
        Date d = new Date();
        m_time = d.toString();
    }

    public String getM_fromUser() {
        return m_fromUser;
    }

    public String getM_text() {
        return m_text;
    }

    public String getM_time() {
        return m_time;
    }

    public void setM_fromUser(String m_fromUser) {
        this.m_fromUser = m_fromUser;
    }

    public void setM_text(String m_text) {
        this.m_text = m_text;
    }

    public void setM_time(String m_time) {
        this.m_time = m_time;
    }
}
