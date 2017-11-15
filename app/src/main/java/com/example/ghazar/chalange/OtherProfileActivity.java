package com.example.ghazar.chalange;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.ghazar.chalange.Objects.Account;

public class OtherProfileActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        String email = intent.getStringExtra(MainActivity.m_mainActivity.EMAIL);
        MainActivity.m_mainActivity.sendGuestRequest(email);

        Account acc = MainActivity.m_mainActivity.getAccount(email);

        Button nameButton = (Button) findViewById(R.id.other_profile_name_button);
        nameButton.setText(acc.get_name());
        Button lastNameButton = (Button) findViewById(R.id.other_profile_last_name_button);
        lastNameButton.setText(acc.get_lastName());
        Button ageButton = (Button) findViewById(R.id.other_profile_age_button);
        ageButton.setText(Integer.toString(acc.get_age()));
        Button phoneButton = (Button) findViewById(R.id.other_profile_phone_button);
        phoneButton.setText(acc.get_phone());
        Button emailButton = (Button) findViewById(R.id.other_profile_email_button);
        emailButton.setText(acc.get_email());
        Button TeamButton = (Button) findViewById(R.id.other_profile_team_button);
        TeamButton.setText("red");
        Button genderButton = (Button) findViewById(R.id.other_profile_gender_button);
        genderButton.setText(acc.get_gender() ? "Male" : "Famel");

        Button FrendRequestButton = (Button)findViewById(R.id.other_profile_frend_request_button);
        Button ChalangeRequestButton = (Button)findViewById(R.id.other_profile_chalange_request_button);
        FrendRequestButton.setOnClickListener(this);
        ChalangeRequestButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.other_activity_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finishActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    public void finishActivity() {
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.other_profile_frend_request_button)
        {
            String email = getIntent().getStringExtra(MainActivity.m_mainActivity.EMAIL);
            MainActivity.m_mainActivity.sendFrendRequest(email);
            MainActivity.m_mainActivity.addFrend(email);
        }
    }
}
