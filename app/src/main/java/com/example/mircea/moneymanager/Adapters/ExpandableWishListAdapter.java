package com.example.mircea.moneymanager.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.mircea.moneymanager.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ExpandableWishListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<String> wishHeaders;
    private HashMap<String, List<String>> wishDescription;

    public ExpandableWishListAdapter(Context mContext, ArrayList<String> wishHeaders,
                                     HashMap<String, List<String>> wishDescription){
        this.mContext = mContext;
        this.wishHeaders = wishHeaders;
        this.wishDescription = wishDescription;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater inflaInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflaInflater.inflate(R.layout.wish_item_header, null);
        }

        TextView lblListHeader = convertView
                .findViewById(R.id.list_header_name);

        //lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.wish_item_description, null);
        }

        TextView descChild = convertView
                .findViewById(R.id.item_content_desc);

        descChild.setText(childText);
        return convertView;

    }

    @Override
    public int getGroupCount() {
        return this.wishHeaders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.wishDescription.get(this.wishHeaders.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.wishHeaders.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return this.wishDescription.get(this.wishHeaders.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
