package com.example.ghazar.chalange.Objects;

import java.util.Vector;

/**
 * Created by ghazar on 11/13/17.
 */

public class AccountDB {
    private Account m_account;
    private Vector<Events> m_events;
    private Frends m_frends;

    public AccountDB(Account acc, Events events, Frends frends)
    {
        m_events = new Vector<Events>();
        m_account = acc;
        m_events.add(events);
        m_frends = frends;
    }

    public Account getM_account() {
        return m_account;
    }

    public Vector<Events> getM_events() {
        return m_events;
    }

    public Frends getM_frends() {
        return m_frends;
    }

    public void setM_account(Account m_account) {
        this.m_account = m_account;
    }

    public void setM_events(Vector<Events> m_events) {
        this.m_events = m_events;
    }

    public void setM_frends(Frends m_frends) {
        this.m_frends = m_frends;
    }
}
