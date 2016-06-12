package at.ac.univie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import at.ac.univie.adapter.SimpleListAdapter;
import at.ac.univie.frog.R;
import at.ac.univie.main.GroupActivity;

public class SplitOptionActivity extends AppCompatActivity {

    ListView splitView;
    ListAdapter adapter;
    ArrayList<String> options = new ArrayList();
    String simpleString = "";

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_split_option);
        getSupportActionBar().setTitle("Split Options");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        splitView = (ListView) findViewById(R.id.viewSplit);

        options.add("Split equally");
        options.add("Split manually");
        options.add("Split in %");
        options.add("Split in parts");

        adapter = new SimpleListAdapter(this, R.layout.simple_list, options, simpleString);

        splitView.setAdapter(adapter);

        splitView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {

                if (position == 0) {
                    Intent done = new Intent(SplitOptionActivity.this, GroupActivity.class);
                    done.putExtra("check", "true");
                    startActivity(done);
                }
                else {
                    Intent goToSplitView = new Intent(SplitOptionActivity.this, SplitViewActivity.class);
                    String option;
                    switch (position) {
                        case 1 : option = " manually";
                            break;
                        case 2 : option = " in %";
                            break;
                        case 3 : option = " in parts";
                            break;
                        default : option = "";
                    }
                    goToSplitView.putExtra("option", option);
                    startActivity(goToSplitView);
                }

            }

        });
    }
}
