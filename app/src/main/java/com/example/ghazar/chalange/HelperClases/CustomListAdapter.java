package com.example.ghazar.chalange.HelperClases;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ghazar.chalange.Activitys.FrendRequestActivity;
import com.example.ghazar.chalange.Activitys.MainActivity;
import com.example.ghazar.chalange.FirstPage.FirstActivity;
import com.example.ghazar.chalange.Objects.Events;
import com.example.ghazar.chalange.Objects.Frends;
import com.example.ghazar.chalange.Objects.RowItem;
import com.example.ghazar.chalange.R;
import com.google.firebase.database.DataSnapshot;

/**
 * Created by ghazar on 11/8/17.
 */

public class CustomListAdapter extends ArrayAdapter<RowItem>{
    private final Activity m_context;
    private int m_itemId;

    public ViewHolderForAccountItem m_accountItemWidgets;

    public CustomListAdapter(Activity context, int itemId) {
        super(context, itemId);

        m_itemId = itemId;
        m_context = context;
        m_accountItemWidgets = new ViewHolderForAccountItem();
    }

    private class ViewHolderForAccountItem {

        //For all
        ImageView imageView = null;
        TextView  txtTitle = null;
        TextView  txtDesc = null;

        //For frend request activity
        ImageView AddFrendButton = null;
        ImageView DeleteButton = null;
        TextView  FrendMessage = null;

        //For challange resuest dialog
        ImageView AcceptChallange = null;
        ImageView CaneclChallange = null;

        TextView  EventTimeText = null;

    }

    public View getView(final int position, View view, ViewGroup parent) {
        final RowItem rowItem = getItem(position);
        LayoutInflater inflater=m_context.getLayoutInflater();
        if(view == null) {
            if(m_itemId == R.layout.account_item) {
                view = inflater.inflate(R.layout.account_item, null,true);
                m_accountItemWidgets.txtDesc = (TextView) view.findViewById(R.id.Desc);
                m_accountItemWidgets.txtTitle = (TextView) view.findViewById(R.id.title);
                m_accountItemWidgets.imageView = (ImageView) view.findViewById(R.id.icon);

                m_accountItemWidgets.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                            MainActivity activity = (MainActivity) CustomListAdapter.this.m_context;
                            activity.goGuest(rowItem.getID());
                        }catch (ClassCastException ex){
                            Log.e("MY ERROR", ex.toString());
                        }catch (NullPointerException ex) {
                            Log.e("MY ERROR", ex.toString());
                        }
                    }
                });
            }
            else if(m_itemId == R.layout.frend_request_account_item) {
                view = inflater.inflate(R.layout.frend_request_account_item, null,true);
                m_accountItemWidgets.txtDesc = (TextView) view.findViewById(R.id.Desc);
                m_accountItemWidgets.txtTitle = (TextView) view.findViewById(R.id.title);
                m_accountItemWidgets.FrendMessage = (TextView) view.findViewById(R.id.message);
                m_accountItemWidgets.imageView = (ImageView) view.findViewById(R.id.icon);
                m_accountItemWidgets.AddFrendButton = (ImageView)view.findViewById(R.id.AddFrend);
                m_accountItemWidgets.DeleteButton = (ImageView)view.findViewById(R.id.Delete);
                if(rowItem.getTitle().endsWith("+")) {
                    m_accountItemWidgets.AddFrendButton.setVisibility(View.GONE);
                    m_accountItemWidgets.FrendMessage.setText(R.string.frend_added_you_in_her_frend_list);
                    rowItem.setTitle(rowItem.getTitle().substring(0, rowItem.getTitle().length() - 1));
                }
                else{
                    m_accountItemWidgets.FrendMessage.setVisibility(View.GONE);
                }

                m_accountItemWidgets.AddFrendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String a = rowItem.getID();
                        for(DataSnapshot postSnapshot : FirstActivity.m_database.m_AccountEventsDataSnapshot.getChildren())
                        {
                            if(postSnapshot.child(Events.EVENT_KEY).getValue(String.class).equals(Events.FREND_REQUEST_EVENT_KEY)
                            && postSnapshot.child(Events.EVENT_TEXT).getValue(String.class).equals(rowItem.getID())) {
                                postSnapshot.getRef().removeValue();
                                remove(rowItem);
                                break;
                            }
                        }
                    }
                });
                m_accountItemWidgets.DeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(DataSnapshot postSnapshot : FirstActivity.m_database.m_AccountEventsDataSnapshot.getChildren())
                        {
                            if(postSnapshot.child(Events.EVENT_KEY).getValue(String.class).equals(Events.FREND_REQUEST_EVENT_KEY)
                            && postSnapshot.child(Events.EVENT_TEXT).getValue(String.class).equals(rowItem.getID())) {
                                postSnapshot.getRef().removeValue();
                                remove(rowItem);
                                break;
                            }
                        }
                    }
                });
            }
            else if(m_itemId == R.layout.account_item_for_guest_activity)
            {
                view = inflater.inflate(R.layout.account_item_for_guest_activity, null,true);
                m_accountItemWidgets.txtDesc = (TextView) view.findViewById(R.id.Desc);
                m_accountItemWidgets.txtTitle = (TextView) view.findViewById(R.id.title);
                m_accountItemWidgets.imageView = (ImageView) view.findViewById(R.id.icon);
                m_accountItemWidgets.EventTimeText = (TextView)view.findViewById(R.id.Time);
                m_accountItemWidgets.EventTimeText.setText(rowItem.getTime());
            }
            else if(m_itemId == R.layout.chalange_request_account_item)
            {
                view = inflater.inflate(R.layout.chalange_request_account_item, null, true);
                m_accountItemWidgets.txtDesc = (TextView) view.findViewById(R.id.Desc);
                m_accountItemWidgets.txtTitle = (TextView) view.findViewById(R.id.title);
                m_accountItemWidgets.imageView = (ImageView) view.findViewById(R.id.icon);
                m_accountItemWidgets.AcceptChallange = (ImageView) view.findViewById(R.id.accept_challange_request_button);
                m_accountItemWidgets.CaneclChallange = (ImageView) view.findViewById(R.id.cancel_challange_request_button);

                m_accountItemWidgets.AcceptChallange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.m_mainActivity.acceptChallangeWith(rowItem.getID());
                    }
                });

                m_accountItemWidgets.CaneclChallange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.m_mainActivity.cancelChallangeWith(rowItem.getID());
                    }
                });
            }
            view.setTag(m_accountItemWidgets);
        }
        else {
                m_accountItemWidgets = (ViewHolderForAccountItem)view.getTag();
        }

        m_accountItemWidgets.txtDesc.setText(rowItem.getDesc());
        m_accountItemWidgets.txtTitle.setText(rowItem.getTitle());
        ImageView image = rowItem.getImageId();
        Drawable d = image.getDrawable();
        m_accountItemWidgets.imageView.setImageDrawable(d);

        return view;
    };
}
