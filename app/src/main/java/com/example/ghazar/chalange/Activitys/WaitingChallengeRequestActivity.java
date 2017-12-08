package com.example.ghazar.chalange.Activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
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

import org.w3c.dom.Text;

public class WaitingChallengeRequestActivity extends AppCompatActivity {

    private String m_id;
    private int m_time = 60;
    public static GameObject m_game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_challenge_request);

        Intent intent = getIntent();
        m_id = intent.getStringExtra(Database.ID);

        m_game = new GameObject(Database.m_id, m_id);
        FirstActivity.m_database.addGame(m_game);

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
                    finish();
                }
            });
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
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
                --m_time;
                if(m_time<= 0){
                    FirstActivity.m_database.deleteEvent(m_id, Events.CHALANGE_REQUEST_EVENT_KEY);
                    Finish();
                }
                timeTextView.setText(String.valueOf(m_time));
            }
        }, 1000);
    }

    public void Finish(){
        FirstActivity.m_database.deleteEvent(m_id, Events.CHALANGE_REQUEST_EVENT_KEY);
        FirstActivity.m_database.deleteGame(m_game);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Finish();
    }
}
