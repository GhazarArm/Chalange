package com.example.ghazar.chalange.Objects;

import java.util.Vector;

/**
 * Created by ghazar on 11/9/17.
 */

public class Frends {
    private String m_id;
    private Vector<String> m_frends;

    public final static String FRENDS_VECTOR_KEY = "m_frends";

    public Frends(String id){m_id = id; m_frends = new Vector<String>();}

    public String getM_id() {
        return m_id;
    }

    public Vector<String> getM_frends() {
        return m_frends;
    }

    public void setM_email(String id) {
        this.m_id = id;
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
