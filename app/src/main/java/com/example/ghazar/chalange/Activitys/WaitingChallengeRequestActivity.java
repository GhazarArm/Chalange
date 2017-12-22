package com.example.ghazar.chalange.Activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ghazar.chalange.FirstPage.FirstActivity;
import com.example.ghazar.chalange.Objects.Account;
import com.example.ghazar.chalange.Objects.Database;
import com.example.ghazar.chalange.Objects.Events;
import com.example.ghazar.chalange.Objects.GameObject;
import com.example.ghazar.chalange.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class WaitingChallengeRequestActivity extends AppCompatActivity {

    private String m_id;

    private int m_time60 = 60;
    private int m_time5 = 7;

    public static GameObject m_game;

    public static WaitingChallengeRequestActivity m_this;

    private boolean m_isCanceled;
    private boolean m_isAccepted;

    public static DatabaseReference m_gameDatabase = null;
    public static DataSnapshot m_gameData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_challenge_request);
        m_this = this;
        m_isCanceled = false;
        m_isAccepted = false;

        Intent intent = getIntent();
        m_id = intent.getStringExtra(Database.ID);

        m_isAccepted = intent.getBooleanExtra("Accept", false);//if m_isAccepted is true that menas that you second player
        if(m_isAccepted){
            m_game = new GameObject(m_id, MainActivity.m_mainActivity.m_id);
            connectDatabase();
        }

        //Init widgets
        final Button cancelButton = (Button)findViewById(R.id.chalange_request_cancel_buttun);
        final Button sendMessageButton = (Button)findViewById(R.id.message_send_button);
        final TextView  waitingText = (TextView)findViewById(R.id.waiting_text_view);
        final TextView timeTextView = (TextView)findViewById(R.id.time_text_view);
        final EditText sendTextEditText = (EditText)findViewById(R.id.message_send_edit_text);

        sendMessageButton.setEnabled(false);
        sendTextEditText.setEnabled(false);

        if(cancelButton != null){
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(m_isAccepted)
                    {
                        if(m_game.getM_player1().equals(MainActivity.m_mainActivity.m_id))
                            m_gameDatabase.child(GameObject.PLAYER1_ACCEPT_KEY).setValue(GameObject.CANCELED);
                        else if(m_game.getM_player2().equals(MainActivity.m_mainActivity.m_id))
                            m_gameDatabase.child(GameObject.PLAYER2_ACCEPT_KEY).setValue(GameObject.CANCELED);

                        m_isCanceled = true;
                        m_isAccepted = false;
                    }else {
                        Finish();
                    }
                }
            });
        }

        if(sendMessageButton != null){
            sendMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = sendTextEditText.getText().toString();
                    int witchPlayer = 0;
                    if(m_game.getM_player1().equals(MainActivity.m_mainActivity.m_id))
                        witchPlayer = 2;
                    else
                        witchPlayer = 1;

                    FirstActivity.m_database.sendMessage(m_game.m_gameID, text, witchPlayer);
                    sendTextEditText.setText("");
                }
            });
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(m_isCanceled) {
                    waitingText.setText("Canceled!!!");
                    cancelButton.setText("Go back");
                    sendMessageButton.setEnabled(false);
                    sendTextEditText.setEnabled(false);
                    return;
                }
                if(m_isAccepted){
                    if(m_gameData == null)
                        return;
                    if(m_time5 <= 0){
                        if(m_game.getM_player1().equals(MainActivity.m_mainActivity.m_id))
                            m_gameDatabase.child(GameObject.PLAYER1_ACCEPT_KEY).setValue(GameObject.ACCEPTED);
                        else if(m_game.getM_player2().equals(MainActivity.m_mainActivity.m_id))
                            m_gameDatabase.child(GameObject.PLAYER2_ACCEPT_KEY).setValue(GameObject.ACCEPTED);

                        cancelButton.setClickable(false);

                        return;
                    }
                    --m_time5;

                    waitingText.setText("Challange start in ");
                    timeTextView.setText(String.valueOf(m_time5));
                    sendMessageButton.setEnabled(true);
                    sendTextEditText.setEnabled(true);

                    handler.postDelayed(this, 1000);
                    return;
                }
                String curentText = waitingText.getText().toString();
                switch (curentText){
                    case "Waiting" :
                        waitingText.setText("Waiting.");
                        break;
                    case "Waiting." :
                        waitingText.setText("Waiting..");
                        break;
                    case "Waiting.." :
                        waitingText.setText("Waiting...");
                        break;
                    case "Waiting..." :
                        waitingText.setText("Waiting");
                        break;
                }
                handler.postDelayed(this, 1000);
                --m_time60;
                if(m_time60<= 0){
                    FirstActivity.m_database.deleteEvent(m_id, Events.CHALANGE_REQUEST_EVENT_KEY);
                    Finish();
                }
                timeTextView.setText(String.valueOf(m_time60));
            }
        }, 1000);
    }

    public void connectDatabase(){
        m_gameDatabase = FirstActivity.m_database.m_GamesDB.child(m_game.m_gameID);
        m_gameDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                m_gameData = dataSnapshot;
                checkingAcceptEvent();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference messageDatabase;
        if(MainActivity.m_mainActivity.m_id.equals(m_game.getM_player1()))
            messageDatabase = m_gameDatabase.child(GameObject.PLAYER1_MESSAGE_KEY);
        else
            messageDatabase = m_gameDatabase.child(GameObject.PLAYER2_MESSAGE_KEY);

        messageDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showMessage(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void checkingAcceptEvent(){
        int player1State = -1;
        int player2State = -1;
        try {
            player1State = m_gameData.child(GameObject.PLAYER1_ACCEPT_KEY).getValue(Integer.class);
            player2State = m_gameData.child(GameObject.PLAYER2_ACCEPT_KEY).getValue(Integer.class);
        }catch(Exception e){
            Log.e("my error", e.toString());
        }
        if(player1State == GameObject.ACCEPTED && player2State == GameObject.ACCEPTED){
            startGame();
        }else if(player2State == GameObject.CANCELED || player1State == GameObject.CANCELED){
            FirstActivity.m_database.deleteGame(m_game);
            m_isCanceled = true;
            m_isAccepted = false;
        }
    }

    public void challangeAcceptedBy(String id){
        FirstActivity.m_database.deleteMyEvents(id, Events.CHALANGE_ACCEPT_REQUEST_EVENT_KEY);
        TextView  waitingText = (TextView)findViewById(R.id.waiting_text_view);
        m_isAccepted = true;
        makeGame();
    }

    public void challangeCanceledBy(String id){
        m_isCanceled = true;
    }

    public void makeGame(){
        m_game = new GameObject(MainActivity.m_mainActivity.m_id, m_id);
        FirstActivity.m_database.addGame(m_game);
        connectDatabase();
    }

    public void startGame(){
        showMessage("Start  game!!!!!!!!!!!!!!!!!!");
    }

    public void showMessage(String text){
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 200);
        TextView textView = new TextView(this);

        textView.setBackgroundColor(Color.GRAY);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(20);
        textView.setText(text);
        toast.setView(textView);
        toast.show();
    }

    public void Finish(){
        FirstActivity.m_database.deleteEvent(m_id, Events.CHALANGE_REQUEST_EVENT_KEY);
        FirstActivity.m_database.deleteMyEvents(m_id, Events.CHALANGE_CANCEL_REQUEST_EVENT_KEY);
        finish();
    }
}
