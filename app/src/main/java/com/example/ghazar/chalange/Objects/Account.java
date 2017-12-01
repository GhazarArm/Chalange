package com.example.ghazar.chalange.Objects;

import java.security.PublicKey;


public class Account {

    public final static String FACEBOOK = "Facebook";
    public final static String GOOGLE = "Google";
    public final static String TWITTER = "Twitter";


    private String m_name;
    private String m_lastName;
    private int    m_age;
    private boolean m_gender;
    private String m_id;


    public Account(){
        m_name = "-";
        m_lastName = "-";
        m_age = -1;
        m_gender = true;
    };

    public Account(String m_name, String m_lastName, int m_age, boolean gender, String id) {
        this.m_name = m_name;
        this.m_lastName = m_lastName;
        this.m_age = m_age;
        this.m_gender = gender;
        m_id = id;
    }

    public void set_name(String m_name) {
        this.m_name = m_name;
    }

    public void set_lastName(String m_lastName) {
        this.m_lastName = m_lastName;
    }

    public void set_age(int m_age) {
        this.m_age = m_age;
    }

    public void set_gender(boolean m_gender) {
        this.m_gender = m_gender;
    }

    public void set_id(String m_id) {
        this.m_id = m_id;
    }

    public String get_name() {
        return m_name;
    }

    public String get_lastName() {
        return m_lastName;
    }

    public int get_age() {
        return m_age;
    }

    public boolean get_gender() {
        return m_gender;
    }

    public String get_id() {
        return m_id;
    }
}
