package com.example.ghazar.chalange.Tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ghazar.chalange.HelperClases.CustomListAdapter;
import com.example.ghazar.chalange.Activitys.MainActivity;
import com.example.ghazar.chalange.Objects.Account;
import com.example.ghazar.chalange.Activitys.OtherProfileActivity;
import com.example.ghazar.chalange.R;
import com.example.ghazar.chalange.Objects.RowItem;

import java.util.Vector;

/**
 * Created by ghazar on 11/8/17.
 */

public class Tag2 extends Fragment{
    private CustomListAdapter m_adapter;
    private View m_view;
    private ListView m_listViewFrends;
    private Vector<Account> m_accounts;

    public final int REQUEST_CODE_OF_CHAT_ACTIVITY = 1;

    public static Tag2 m_tab2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        m_tab2 = this;
        View m_view = inflater.inflate(R.layout.tab1, container, false);
        m_listViewFrends = (ListView) m_view.findViewById(R.id.ListViewChallanges);
        m_adapter = new CustomListAdapter(getActivity(), R.layout.account_item);
        m_listViewFrends.setAdapter(m_adapter);
        m_listViewFrends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Account accountTo = m_accounts.elementAt(position);
                Intent chatIntent = new Intent(getActivity(),  OtherProfileActivity.class);
                chatIntent.putExtra(MainActivity.m_mainActivity.ID, accountTo.get_id());
                startActivityForResult(chatIntent, REQUEST_CODE_OF_CHAT_ACTIVITY);
            }
        });
        return m_view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            String result = data.getStringExtra("accountName");
        }
    }

    public void initListView(Vector<Account> accounts)
    {
        for(Account acc : accounts)
        {
            AddItem(MainActivity.m_mainActivity.getIconId(acc.get_name()),
                    acc.get_name() + "  " + acc.get_lastName(),
                    Integer.toString(acc.get_age()),
                    acc.get_id());
        }
    }

    public void AddItem(int imageName, String text, String extraText, String id)
    {
        RowItem rowItem = new RowItem(imageName, text, extraText, id);
        m_adapter.add(rowItem);
    }


    public void SearchAccount(String name, int maxAge, int minAge){
        m_accounts = MainActivity.m_mainActivity.SearchAccount(name, maxAge, minAge);
        m_adapter.clear();
        initListView(m_accounts);
    }
}
