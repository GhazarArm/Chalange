package com.example.ghazar.chalange.Activitys;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ghazar.chalange.FirstPage.FirstActivity;
import com.example.ghazar.chalange.HelperClases.CustomListAdapter;
import com.example.ghazar.chalange.Objects.Account;
import com.example.ghazar.chalange.Objects.Database;
import com.example.ghazar.chalange.Objects.Events;
import com.example.ghazar.chalange.Objects.RowItem;
import com.example.ghazar.chalange.R;
import com.google.firebase.database.DataSnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

public class GuestActivity extends AppCompatActivity {

    private CustomListAdapter m_adapter;
    private ListView m_listViewFrends;

    public static GuestActivity m_guestActivity;

    private Vector<Account> m_accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        m_accounts = new Vector<Account>();

        m_guestActivity = this;

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        m_listViewFrends = (ListView) findViewById(R.id.guests_list_view);
        m_adapter = new CustomListAdapter(this, R.layout.account_item_for_guest_activity);
        m_listViewFrends.setAdapter(m_adapter);

        initList();

        m_listViewFrends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(m_guestActivity,  OtherProfileActivity.class);
                intent.putExtra(Database.ID, m_accounts.elementAt(position).get_id());
                startActivityForResult(intent, 0);
            }
        });
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
        for(DataSnapshot postSnapshot : FirstActivity.m_database.m_AccountEventsDataSnapshot.getChildren())
        {
            if(postSnapshot.child(Events.EVENT_KEY).getValue(String.class).equals(Events.ACCOUNT_GUEST_KEY))
            {
                Account acc = FirstActivity.m_database.getAccount(postSnapshot.child(Events.EVENT_TEXT).getValue(String.class));
                postSnapshot.getRef().removeValue();
                m_accounts.add(acc);
                String dateString = postSnapshot.child(Events.EVENT_DATE).getValue(String.class);

                AddItem(MainActivity.m_mainActivity.getIconId(acc.get_name()),
                        acc.get_name() + "  " + acc.get_lastName(),
                        Integer.toString(acc.get_age()),
                        dateString,
                        acc.get_id());
            }
        }
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    public void AddItem(int imageName, String text, String extraText, String time, String id)
    {
        RowItem rowItem = new RowItem(imageName, text, extraText, time, id);
        m_adapter.add(rowItem);
    }
}
