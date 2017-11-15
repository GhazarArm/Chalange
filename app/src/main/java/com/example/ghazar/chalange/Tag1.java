package com.example.ghazar.chalange;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
import android.widget.AdapterView;
import android.widget.ListView;

public class Tag1 extends Fragment {
    private CustomListAdapter m_adapter;
    private View m_view;
    private ListView m_listViewFrends;
    private static MainActivity m_mainActivity;

    public final int REQUEST_CODE_OF_CHAT_ACTIVITY = 1;


    public static void setContext(MainActivity mContext) {
        m_mainActivity = mContext;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        m_view = inflater.inflate(R.layout.tab1, container, false);
        m_listViewFrends = (ListView) m_view.findViewById(R.id.ListViewFrends);
        m_adapter = new CustomListAdapter(getActivity(), R.layout.account_item);
        m_listViewFrends.setAdapter(m_adapter);
        m_listViewFrends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
//                Intent chatIntent = new Intent(getActivity(),  ChatActivity.class);
//                startActivityForResult(chatIntent, REQUEST_CODE_OF_CHAT_ACTIVITY);
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
}
