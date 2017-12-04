package com.example.ghazar.chalange.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ghazar.chalange.FirstPage.FirstActivity;
import com.example.ghazar.chalange.Objects.Account;
import com.example.ghazar.chalange.Objects.Database;
import com.example.ghazar.chalange.Objects.Frends;
import com.example.ghazar.chalange.R;
import com.google.firebase.database.DataSnapshot;

public class OtherProfileActivity extends AppCompatActivity{

    private Menu m_menu;
    private boolean m_isFriend;

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
        String id = intent.getStringExtra(Database.ID);
        FirstActivity.m_database.sendGuestRequest(id);


        Account acc = FirstActivity.m_database.getAccount(id);

        Button nameButton = (Button) findViewById(R.id.other_profile_name_button);
        nameButton.setText(acc.get_name());
        Button lastNameButton = (Button) findViewById(R.id.other_profile_last_name_button);
        lastNameButton.setText(acc.get_lastName());
        Button ageButton = (Button) findViewById(R.id.other_profile_age_button);
        ageButton.setText(Integer.toString(acc.get_age()));
        Button TeamButton = (Button) findViewById(R.id.other_profile_team_button);
        TeamButton.setText("red");
        Button genderButton = (Button) findViewById(R.id.other_profile_gender_button);
        genderButton.setText(acc.get_gender() ? "Male" : "Famel");

        ImageView imageView = (ImageView) findViewById(R.id.other_activity_image_view);
        imageView.setImageResource(MainActivity.m_mainActivity.getIconId(acc.get_name()));

        m_isFriend = false;
        for(DataSnapshot postSnapshot : FirstActivity.m_database.m_AccountFrendsDataSnapshot.child(Frends.FRENDS_VECTOR_KEY).getChildren())
        {
            if(postSnapshot.getValue(String.class).equals(acc.get_id())) {
                m_isFriend = true;
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.other_profile_menu, menu);
        m_menu = menu;
        if(m_isFriend) {
            m_menu.findItem(R.id.other_profile_addFriend).setIcon(R.drawable.delete_user);
        }else {
            m_menu.findItem(R.id.other_profile_addFriend).setIcon(R.drawable.add_user);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finishActivity();
        }else if(item.getItemId() == R.id.other_profile_addFriend)
        {
            m_isFriend = !m_isFriend;
            if(m_isFriend) {
                String id = getIntent().getStringExtra(Database.ID);
                FirstActivity.m_database.sendFrendRequest(id);
                FirstActivity.m_database.addFrend(id);
                m_menu.findItem(R.id.other_profile_addFriend).setIcon(R.drawable.delete_user);
            }else {
                String id = getIntent().getStringExtra(Database.ID);
                FirstActivity.m_database.deleteFrend(id);
                m_menu.findItem(R.id.other_profile_addFriend).setIcon(R.drawable.add_user);
            }
        }else if(item.getItemId() == R.id.other_profile_challengeRequest)
        {

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
}
