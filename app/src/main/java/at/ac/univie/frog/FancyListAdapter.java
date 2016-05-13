package at.ac.univie.frog;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This class is an extension of the ArrayAdapter class in order to be
 * able to customize the list view in the MainActivity class.
 * @see ArrayAdapter
 */

public class FancyListAdapter extends ArrayAdapter<String> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<String> list = null;

    public FancyListAdapter(Context context, int layoutResourceId, ArrayList<String> list) {
        super(context, layoutResourceId, list);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View fancyView = view;

        if(fancyView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            fancyView = inflater.inflate(layoutResourceId, null);
        }

        TextView textView = (TextView) fancyView.findViewById(R.id.textView);

        textView.setTextColor(Color.BLACK);
        textView.setText(list.get(position));
        textView.setBackgroundColor(Color.WHITE);

        return fancyView;
    }

}