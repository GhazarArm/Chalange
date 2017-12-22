package com.example.ghazar.chalange.Objects;

public class GameObject {
    public static String PLAYER1_KEY = "m_player1";
    public static String PLAYER2_KEY = "m_player2";
    public static String PLAYER1_ACCEPT_KEY = "m_acceptPlayer1";
    public static String PLAYER2_ACCEPT_KEY = "m_acceptPlayer2";
    public static String PLAYER1_MESSAGE_KEY = "m_player1Message";
    public static String PLAYER2_MESSAGE_KEY = "m_player2Message";

    public static int WAITING = 0;
    public static int ACCEPTED = 1;
    public static int CANCELED = 2;

    private String m_player1;
    private String m_player2;
    public String m_gameID;
    private int m_acceptPlayer1;
    private int m_acceptPlayer2;
    private String m_player1Message;
    private String m_player2Message;

    public GameObject(String p1, String p2){
        m_player1 = p1;
        m_player2 = p2;
        m_acceptPlayer1 = WAITING;
        m_acceptPlayer2 = WAITING;
        m_gameID = m_player1 + "VS" + m_player2;
        m_player1Message = "";
        m_player2Message = "";
    }

    public void setM_acceptPlayer1(int m_acceptPlayer1) {
        this.m_acceptPlayer1 = m_acceptPlayer1;
    }

    public void setM_acceptPlayer2(int m_acceptPlayer2) {
        this.m_acceptPlayer2 = m_acceptPlayer2;
    }

    public int isM_acceptPlayer1() {

        return m_acceptPlayer1;
    }

    public int isM_acceptPlayer2() {
        return m_acceptPlayer2;
    }

    public String getM_player1() {
        return m_player1;
    }

    public String getM_player2() {
        return m_player2;
    }

    public void setM_player1(String m_player1) {
        this.m_player1 = m_player1;
    }

    public void setM_player2(String m_player2) {
        this.m_player2 = m_player2;
    }

    public void setM_player1Message(String m_player1Message) {
        this.m_player1Message = m_player1Message;
    }

    public void setM_player2Message(String m_player2Message) {
        this.m_player2Message = m_player2Message;
    }

    public String getM_player1Message() {
        return m_player1Message;
    }

    public String getM_player2Message() {
        return m_player2Message;
    }
}
