package at.ac.univie.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import at.ac.univie.frog.R;
import at.ac.univie.main.MapView;

/**
 * Created by Daniel on 11.06.16.
 */


public class SearchListAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<String> filterList = null;
    private ArrayList<String> arraylist;

    public SearchListAdapter(Context context, List<String> list) {
        mContext = context;
        this.filterList = list;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<String>();
        this.arraylist.addAll(filterList);
    }

    public class ViewHolder {
        TextView groupName;
    }

    @Override
    public int getCount() {
        return filterList.size();
    }

    @Override
    public String getItem(int position) {
        return filterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.search_list, null);
            // Locate the TextViews in listview_item.xml
            holder.groupName = (TextView) view.findViewById(R.id.groupName);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.groupName.setTextColor(Color.BLACK);
        holder.groupName.setBackgroundColor(Color.WHITE);
        holder.groupName.setText(filterList.get(position));


        // Listen for ListView Item Click
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Send single item click data to SingleItemView Class
                Intent intent = new Intent(mContext, MapView.class);
                intent.putExtra("GroupPosition",(filterList.get(position)));
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        filterList.clear();
        if (charText.length() == 0) {
            filterList.addAll(arraylist);
        }
        else
        {
            for (String text : arraylist)
            {
                if (text.toLowerCase(Locale.getDefault()).contains(charText))
                {
                    filterList.add(text);
                }
            }
        }
        notifyDataSetChanged();
    }

}