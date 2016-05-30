package at.ac.univie.frog;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tamara on 13.05.16.
 */


public class SimpleListAdapter extends ArrayAdapter<String> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<String> list = null;

    public SimpleListAdapter(Context context, int layoutResourceId, ArrayList<String> list) {
        super(context, layoutResourceId, list);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View simpleView = view;

        if(simpleView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            simpleView = inflater.inflate(layoutResourceId, null);
        }

        TextView textView = (TextView) simpleView.findViewById(R.id.simpleText);

        textView.setTextColor(Color.BLACK);
        textView.setText(list.get(position));
        //textView.setBackgroundColor(Color.WHITE);

        return simpleView;
    }

}