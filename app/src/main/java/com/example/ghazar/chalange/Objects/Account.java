package com.example.ghazar.chalange.Objects;

import java.util.Vector;

/**
 * Created by ghazar on 11/8/17.
 */

public class Account {
    private String m_name;
    private String m_lastName;
    private int    m_age;
    private String m_email;
    private String m_phone;
    private String m_password;
    private boolean m_gender;

    public Account(){
        m_name = "-";
        m_lastName = "-";
        m_email = "-";
        m_password = "-";
        m_phone = "-";
        m_age = -1;
        m_gender = true;
    };

    public Account(String m_name, String m_lastName, int m_age, String m_email, String m_phone, String password, boolean gender) {
        this.m_name = m_name;
        this.m_lastName = m_lastName;
        this.m_age = m_age;
        this.m_email = m_email;
        this.m_phone = m_phone;
        this.m_password = password;
        this.m_gender = gender;
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

    public String get_email() {
        return m_email;
    }

    public String get_phone() {
        return m_phone;
    }

    public boolean get_gender() {
        return m_gender;
    }

    public String get_password() {
        return m_password;
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

    public void set_email(String m_email) {
        this.m_email = m_email;
    }

    public void set_phone(String m_phone) {
        this.m_phone = m_phone;
    }

    public void set_gender(boolean m_gender) {
        this.m_gender = m_gender;
    }

    public void set_password(String password) {
        this.m_password = password;
    }
}
