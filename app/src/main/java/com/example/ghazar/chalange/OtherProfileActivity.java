package com.example.ghazar.chalange;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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
//        Intent returnIntent = new Intent();
//        returnIntent.putExtra("accountName", m_accountName);
//        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.other_profile_frend_request_button)
        {
            String email = getIntent().getStringExtra(MainActivity.m_mainActivity.EMAIL);
            MainActivity.m_mainActivity.sendFrendRequest(email);
        }
    }
}
