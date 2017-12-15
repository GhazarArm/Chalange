package com.example.ghazar.chalange.Activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    private int m_time5 = 5;
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

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        m_id = intent.getStringExtra(Database.ID);
        m_isAccepted = intent.getBooleanExtra("Accept", false);

        Button cancelButton = (Button)findViewById(R.id.chalange_request_cancel_buttun);
        Button sendMessageButton = (Button)findViewById(R.id.message_send_button);
        final TextView  waitingText = (TextView)findViewById(R.id.waiting_text_view);
        final TextView timeTextView = (TextView)findViewById(R.id.time_text_view);
        EditText sendTextEditText = (EditText)findViewById(R.id.message_send_edit_text);

        if(cancelButton != null){
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirstActivity.m_database.deleteEvent(m_id, Events.CHALANGE_REQUEST_EVENT_KEY);
                    FirstActivity.m_database.deleteMyEvents(m_id, Events.CHALANGE_CANCEL_REQUEST_EVENT_KEY);
                    finish();
                }
            });
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(m_isCanceled)
                    return;
                if(m_isAccepted){
                    waitingText.setText("Challange start in ");
                    timeTextView.setText(String.valueOf(m_time5));
                    --m_time5;
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

    public void challangeAcceptedBy(String id){
        FirstActivity.m_database.deleteMyEvents(id, Events.CHALANGE_ACCEPT_REQUEST_EVENT_KEY);
        TextView  waitingText = (TextView)findViewById(R.id.waiting_text_view);
        waitingText.setText("Challange start in ");
        m_isAccepted = true;
    }

    public void challangeCanceledBy(String id){
        TextView  waitingText = (TextView)findViewById(R.id.waiting_text_view);
        waitingText.setText("Canceled!!!");
        Button cancelButton = (Button)findViewById(R.id.chalange_request_cancel_buttun);
        cancelButton.setText("Go back");
        m_isCanceled = true;
    }

    public void makeGame(){
        m_game = new GameObject(MainActivity.m_mainActivity.m_id, m_id);
        FirstActivity.m_database.addGame(m_game);
        m_gameDatabase = FirstActivity.m_database.m_GamesDB.child(m_game.m_gameID);
        m_gameDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                m_gameData = dataSnapshot;
                if(m_gameData.child(GameObject.PLAYER1_ACCEPT_KEY).getValue(Integer.class) == GameObject.ACCEPTED
                        && m_gameData.child(GameObject.PLAYER2_ACCEPT_KEY).getValue(Integer.class) == GameObject.ACCEPTED){
                    startGame();
                }else if(m_gameData.child(GameObject.PLAYER1_ACCEPT_KEY).getValue(Integer.class) == GameObject.CANCELED
                        || m_gameData.child(GameObject.PLAYER2_ACCEPT_KEY).getValue(Integer.class) == GameObject.CANCELED){
                    FirstActivity.m_database.deleteGame(m_game);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void startGame(){

    }

    public void Finish(){
        FirstActivity.m_database.deleteEvent(m_id, Events.CHALANGE_REQUEST_EVENT_KEY);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.defoult_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Finish();
    }
}
