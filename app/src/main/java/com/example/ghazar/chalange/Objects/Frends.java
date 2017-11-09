package com.example.ghazar.chalange.Objects;

import java.util.Vector;

/**
 * Created by ghazar on 11/9/17.
 */

public class Frends {
    private String m_email;
    private Vector<String> m_frends;

    public Frends(String email){m_email = email;}

    public String getM_email() {
        return m_email;
    }

    public Vector<String> getM_frends() {
        return m_frends;
    }

    public void setM_email(String m_email) {
        this.m_email = m_email;
    }

    public void setM_frends(Vector<String> m_frends) {
        this.m_frends = m_frends;
    }

    public void AddFrend(String email)
    {
        m_frends.add(email);
    }

    public void removeFrend(String email)
    {
        m_frends.remove(email);
    }

    public int getFrendsCount()
    {
        return m_frends.size();
    }
}
