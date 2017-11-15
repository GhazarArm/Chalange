package com.example.ghazar.chalange;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ghazar.chalange.Objects.Account;
import com.example.ghazar.chalange.Objects.Events;
import com.google.firebase.database.DataSnapshot;

import java.util.Vector;

public class GuestActivity extends AppCompatActivity {

    private CustomListAdapter m_adapter;
    private ListView m_listViewFrends;

    public static GuestActivity m_guestActivity;

    private Vector<Account> m_emails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        m_emails = new Vector<Account>();

        m_guestActivity = this;

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        m_listViewFrends = (ListView) findViewById(R.id.guests_list_view);
        m_adapter = new CustomListAdapter(this, R.layout.frend_request_account_item);
        m_listViewFrends.setAdapter(m_adapter);

        initList();

        m_listViewFrends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(m_guestActivity,  OtherProfileActivity.class);
                intent.putExtra(MainActivity.m_mainActivity.EMAIL, m_emails.elementAt(position).get_email());
                startActivityForResult(intent, 0);
            }
        });
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

    public void initList()
    {
        for(DataSnapshot postSnapshot : MainActivity.m_mainActivity.m_AccountEventsDataSnapshot.getChildren())
        {
            if(postSnapshot.child(Events.EVENT_KEY).getValue(String.class).equals(Events.ACCOUNT_GUEST_KEY))
            {
                Account acc = MainActivity.m_mainActivity.getAccount(postSnapshot.child(Events.EVENT_TEXT).getValue(String.class));
                m_emails.add(acc);
                AddItem(MainActivity.m_mainActivity.getIconId(acc.get_name()), acc.get_name() + "  " + acc.get_lastName(), Integer.toString(acc.get_age()));
            }
        }
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    public void AddItem(int imageName, String text, String extraText)
    {
        RowItem rowItem = new RowItem(imageName, text, extraText);
        m_adapter.add(rowItem);
    }
}
