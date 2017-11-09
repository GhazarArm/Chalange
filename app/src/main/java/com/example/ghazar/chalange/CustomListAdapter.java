package com.example.ghazar.chalange;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.ArrayAdapter;
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

    public CustomListAdapter(Activity context) {
        super(context, R.layout.account_item);
        m_context = context;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtDesc;
    }

    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);

        LayoutInflater inflater=m_context.getLayoutInflater();
        if(view == null) {
            view = inflater.inflate(R.layout.account_item, null,true);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) view.findViewById(R.id.Desc);
            holder.txtTitle = (TextView) view.findViewById(R.id.title);
            holder.imageView = (ImageView) view.findViewById(R.id.icon);
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        holder.txtDesc.setText(rowItem.getDesc());
        holder.txtTitle.setText(rowItem.getTitle());
        holder.imageView.setImageResource(rowItem.getImageId());

        return view;
    };
}
