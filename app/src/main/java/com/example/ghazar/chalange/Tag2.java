package com.example.ghazar.chalange;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.ghazar.chalange.Objects.Account;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
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
        m_listViewFrends = (ListView) m_view.findViewById(R.id.ListViewFrends);
        m_adapter = new CustomListAdapter(getActivity());
        m_listViewFrends.setAdapter(m_adapter);
        m_listViewFrends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Account accountTo = m_accounts.elementAt(position);
                Intent chatIntent = new Intent(getActivity(),  OtherProfileActivity.class);
                chatIntent.putExtra(MainActivity.m_mainActivity.EMAIL, accountTo.get_email());
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

    public void AddItem(int imageName, String text, String extraText)
    {
        RowItem rowItem = new RowItem(imageName, text, extraText);
        m_adapter.add(rowItem);
    }


    public void SearchAccount(String name, int maxAge, int minAge){
        m_accounts = MainActivity.m_mainActivity.SearchAccount(name, maxAge, minAge);
        m_adapter.clear();
        for(Account acc : m_accounts)
        {
            AddItem(MainActivity.m_mainActivity.getIconId(name), acc.get_name() + "  " + acc.get_lastName(), Integer.toString(acc.get_age()));
        }
    }
}
