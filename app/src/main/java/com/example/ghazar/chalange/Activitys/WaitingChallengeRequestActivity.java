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
import com.example.ghazar.chalange.R;

public class WaitingChallengeRequestActivity extends AppCompatActivity {

    private String m_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_challenge_request);

        Intent intent = getIntent();
        m_id = intent.getStringExtra(Database.ID);

        Button cancelButton = (Button)findViewById(R.id.chalange_request_cancel_buttun);
        Button sendMessageButton = (Button)findViewById(R.id.message_send_button);
        final TextView  waitingText = (TextView)findViewById(R.id.waiting_text_view);
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
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        FirstActivity.m_database.deleteEvent(m_id, Events.CHALANGE_REQUEST_EVENT_KEY);
        finish();
    }
}
