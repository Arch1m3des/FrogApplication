package at.ac.univie.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import at.ac.univie.frog.R;

/**
 * Created by Tamara on 13.05.16.
 */


public class SimpleListAdapter extends ArrayAdapter<String> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<String> list = null;
    String splitSign;

    public SimpleListAdapter(Context context, int layoutResourceId, ArrayList<String> list, String splitSign) {
        super(context, layoutResourceId, list);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.list = list;
        this.splitSign = splitSign;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View simpleView = view;

        if (simpleView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            simpleView = inflater.inflate(layoutResourceId, null);
        }

        TextView textView = (TextView) simpleView.findViewById(R.id.simpleText);
        TextView splitSymbol = (TextView) simpleView.findViewById(R.id.splitSymbol);

        textView.setText(list.get(position));
        if (splitSign != "")
            splitSymbol.setText(splitSign);

        return simpleView;
    }

}