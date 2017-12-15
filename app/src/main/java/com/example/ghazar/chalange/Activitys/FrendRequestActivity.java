package com.example.ghazar.chalange.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.Vector;

public class FrendRequestActivity extends AppCompatActivity {

    private CustomListAdapter m_adapter;
    private ListView m_listViewFrends;

    public static FrendRequestActivity m_FrendRequestActivity;

    private Vector<Account> m_accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frend_request);

        m_accounts = new Vector<Account>();

        m_FrendRequestActivity = this;

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        m_listViewFrends = (ListView) findViewById(R.id.frend_request_list_view);
        m_adapter = new CustomListAdapter(this, R.layout.frend_request_account_item);
        m_listViewFrends.setAdapter(m_adapter);

        initList();

        m_listViewFrends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(m_FrendRequestActivity,  OtherProfileActivity.class);
                intent.putExtra(Database.ID, m_accounts.elementAt(position).get_id());
                startActivityForResult(intent, 0);
            }
        });
    }

    public void goGuest(String id){
        Intent intent = new Intent(m_FrendRequestActivity,  OtherProfileActivity.class);
        intent.putExtra(Database.ID, id);
        startActivityForResult(intent, 0);
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
        m_adapter.clear();
        Vector<String> frends = FirstActivity.m_database.getAllFrends();
        for(DataSnapshot postSnapshot : FirstActivity.m_database.m_AccountEventsDataSnapshot.getChildren())
        {
            if(postSnapshot.child(Events.EVENT_KEY).getValue(String.class).equals(Events.FREND_REQUEST_EVENT_KEY))
            {
                int i = 0;
                for(i = 0; i < frends.size(); ++i)
                {
                    if(frends.get(i).equals(postSnapshot.child(Events.EVENT_TEXT).getValue(String.class)))
                        continue;
                }
                if(i < frends.size()) {
                    Account acc = FirstActivity.m_database.getAccount(postSnapshot.child(Events.EVENT_TEXT).getValue(String.class));
                    m_accounts.add(acc);
                    AddItem(MainActivity.m_mainActivity.getIconId(acc.get_name()), acc.get_name() + "  " + acc.get_lastName() + "+", Integer.toString(acc.get_age()), acc.get_id());
                }
                else{
                    Account acc = FirstActivity.m_database.getAccount(postSnapshot.child(Events.EVENT_TEXT).getValue(String.class));
                    m_accounts.add(acc);
                    AddItem(MainActivity.m_mainActivity.getIconId(acc.get_name()), acc.get_name() + "  " + acc.get_lastName(), Integer.toString(acc.get_age()), acc.get_id());
                }
            }
        }
    }

    public void AddItem(int imageName, String text, String extraText, String id)
    {
        RowItem rowItem = new RowItem(imageName, text, extraText, id);
        m_adapter.add(rowItem);
    }
}
