package at.ac.univie.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import at.ac.univie.frog.Child;
import at.ac.univie.frog.Parent;
import at.ac.univie.frog.R;

/**
 * Created by tamara on 28.05.16.
 */

public class FancyExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Parent> parents = null;

    public FancyExpandableListAdapter(Context context, ArrayList<Parent> parents) {
        this.context = context;
        this.parents = parents;
    }

    @Override
    public Child getChild(int groupPosition, int childPosition) {
        ArrayList<Child> childList = parents.get(groupPosition).getItems();
        return childList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        Child child = getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fancy_category, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.fancyChild);

        textView.setText(child.getName());

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Child> childList = parents.get(groupPosition).getItems();
        return childList.size();
    }


    @Override
    public Object getGroup(int groupPosition) {
        return parents.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return parents.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Parent group = (Parent) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fancy_category, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.fancyCategory);
        textView.setText(group.getName());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}