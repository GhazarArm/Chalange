package com.example.ghazar.chalange.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;

import com.example.ghazar.chalange.Activitys.MainActivity;
import com.example.ghazar.chalange.FirstPage.FirstActivity;
import com.example.ghazar.chalange.HelperClases.CustomListAdapter;
import com.example.ghazar.chalange.Objects.Account;
import com.example.ghazar.chalange.Objects.Events;
import com.example.ghazar.chalange.Objects.RowItem;
import com.example.ghazar.chalange.R;
import com.example.ghazar.chalange.Tabs.Tag2;

import java.util.Vector;

/**
 * Created by ghazar on 11/9/17.
 */

public class ChallangeRequestDialog extends Dialog implements View.OnClickListener {

    private ListView m_listView;
    private CustomListAdapter m_adapter;
    private Context m_context;

    private Vector<String> m_ids;

    public ChallangeRequestDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.challange_request_dialog);

        Button CancelAll = (Button)findViewById(R.id.cancel_all);
        CancelAll.setOnClickListener(this);
        if(m_ids == null){
            CancelAll.setClickable(false);
        }else if(m_ids.size() <= 0){
            CancelAll.setClickable(false);
        }

        m_listView = (ListView)findViewById(R.id.requests_list_view);
        m_adapter = new CustomListAdapter(MainActivity.m_mainActivity, R.layout.chalange_request_account_item);
        m_listView.setAdapter(m_adapter);

//        m_listView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.cancel_all)
        {
            for(String str : m_ids){
                FirstActivity.m_database.deleteMyEvents(str, Events.CHALANGE_REQUEST_EVENT_KEY);
            }
        }
    }

    public void initListView(Vector<String> ids){
        m_adapter.clear();
        m_ids = ids;
        for(String str : ids){
            Account acc = FirstActivity.m_database.getAccount(str);
            AddItem(MainActivity.m_mainActivity.getIconId(acc.get_name()),
                    acc.get_name() + "  " + acc.get_lastName(),
                    Integer.toString(acc.get_age()),
                    acc.get_id());
        }
    }

    public void AddItem(ImageView imageName, String text, String extraText, String id)
    {
        RowItem rowItem = new RowItem(imageName, text, extraText, id);
        m_adapter.add(rowItem);
    }
}
