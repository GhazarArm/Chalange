package com.example.ghazar.chalange.Objects;

public class GameObject {
    public static String PLAYER1_KEY = "m_player1";
    public static String PLAYER2_KEY = "m_player2";
    public static String GAME_FREE_KEY = "m_free";

    private String m_player1;
    private String m_player2;
    private boolean m_free;
    private boolean m_acceptPlayer1;
    private boolean m_acceptPlayer2;

    public GameObject(String p1, String p2){
        m_player1 = p1;
        m_player2 = p2;
        m_free = true;
        m_acceptPlayer1 = false;
        m_acceptPlayer2 = false;
    }

    public GameObject(String p1){
        m_player1 = p1;
        m_free = false;
        m_acceptPlayer1 = false;
        m_acceptPlayer2 = false;
    }

    public void setM_acceptPlayer1(boolean m_acceptPlayer1) {
        this.m_acceptPlayer1 = m_acceptPlayer1;
    }

    public void setM_acceptPlayer2(boolean m_acceptPlayer2) {
        this.m_acceptPlayer2 = m_acceptPlayer2;
    }

    public boolean isM_acceptPlayer1() {

        return m_acceptPlayer1;
    }

    public boolean isM_acceptPlayer2() {
        return m_acceptPlayer2;
    }

    public String getM_player1() {
        return m_player1;
    }

    public String getM_player2() {
        return m_player2;
    }

    public boolean isM_free() {
        return m_free;
    }

    public void setM_player1(String m_player1) {
        this.m_player1 = m_player1;
    }

    public void setM_player2(String m_player2) {
        this.m_player2 = m_player2;
    }

    public void setM_free(boolean m_free) {
        this.m_free = m_free;
    }
}
