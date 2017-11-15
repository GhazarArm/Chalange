package com.example.ghazar.chalange;

import android.app.Activity;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ghazar on 11/8/17.
 */

public class CustomListAdapter extends ArrayAdapter<RowItem>{
    private final Activity m_context;
    private String m_text;
    private String m_extraText;
    private int m_imageName;
    private int m_itemId;

    public ViewHolderForAccountItem m_accountItemWidgets;
    public ViewHolderForFrendRequestAccountItem  m_frendRequestAccountItemWidgets;

    public CustomListAdapter(Activity context, int itemId) {
        super(context, itemId);

        m_itemId = itemId;
        m_context = context;
        if(m_itemId == R.layout.account_item)
            m_accountItemWidgets = new ViewHolderForAccountItem();
        else if(m_itemId == R.layout.frend_request_account_item)
            m_frendRequestAccountItemWidgets = new ViewHolderForFrendRequestAccountItem();

    }

    private class ViewHolderForAccountItem {
        ImageView imageView = null;
        TextView txtTitle = null;
        TextView txtDesc = null;
    }

    private class ViewHolderForFrendRequestAccountItem {
        ImageView imageView = null;
        TextView txtTitle = null;
        TextView txtDesc = null;
        Button   AddFrendButton = null;
        Button   DeleteButton = null;
    }

    public View getView(int position, View view, ViewGroup parent) {
        RowItem rowItem = getItem(position);
        LayoutInflater inflater=m_context.getLayoutInflater();
        if(view == null) {
            if(m_itemId == R.layout.account_item) {
                view = inflater.inflate(R.layout.account_item, null,true);
                m_accountItemWidgets.txtDesc = (TextView) view.findViewById(R.id.Desc);
                m_accountItemWidgets.txtTitle = (TextView) view.findViewById(R.id.title);
                m_accountItemWidgets.imageView = (ImageView) view.findViewById(R.id.icon);
                view.setTag(m_accountItemWidgets);
            }
            else if(m_itemId == R.layout.frend_request_account_item) {
                view = inflater.inflate(R.layout.frend_request_account_item, null,true);
                m_frendRequestAccountItemWidgets.txtDesc = (TextView) view.findViewById(R.id.Desc);
                m_frendRequestAccountItemWidgets.txtTitle = (TextView) view.findViewById(R.id.title);
                m_frendRequestAccountItemWidgets.imageView = (ImageView) view.findViewById(R.id.icon);
                m_frendRequestAccountItemWidgets.AddFrendButton = (Button)view.findViewById(R.id.AddFrend);
                m_frendRequestAccountItemWidgets.DeleteButton = (Button)view.findViewById(R.id.Delete);

                m_frendRequestAccountItemWidgets.AddFrendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.v("asd","asd");
                    }
                });
                view.setTag(m_accountItemWidgets);
            }

        }
        else {
            if(m_itemId == R.layout.account_item) {
                m_accountItemWidgets = (ViewHolderForAccountItem)view.getTag();
            }
            else if(m_itemId == R.layout.frend_request_account_item) {
               m_frendRequestAccountItemWidgets = (ViewHolderForFrendRequestAccountItem) view.getTag();
            }
        }

        if(m_itemId == R.layout.account_item) {
            m_accountItemWidgets.txtDesc.setText(rowItem.getDesc());
            m_accountItemWidgets.txtTitle.setText(rowItem.getTitle());
            m_accountItemWidgets.imageView.setImageResource(rowItem.getImageId());
        }
        else if(m_itemId == R.layout.frend_request_account_item) {
            m_frendRequestAccountItemWidgets.txtDesc.setText(rowItem.getDesc());
            m_frendRequestAccountItemWidgets.txtTitle.setText(rowItem.getTitle());
            m_frendRequestAccountItemWidgets.imageView.setImageResource(rowItem.getImageId());
        }

        return view;
    };
}
