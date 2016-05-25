package at.ac.univie.frog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Tamara on 13.05.16.
 */

public class AddFriendActivity extends AppCompatActivity {

    ListView searchView;
    ArrayAdapter adapter;
    ArrayList<String> friends = new ArrayList();
    ArrayList<String> sign = new ArrayList();
    ArrayList<String> color = new ArrayList();

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_add_friend);
        getSupportActionBar().setTitle("Add Friend");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_button);

        searchView = (ListView) findViewById(R.id.searchView);
        adapter = new FancyListAdapter(this, R.layout.fancy_list, friends, sign, color);

        searchView.setAdapter(adapter);

        friends.add("Add Dummy");
        sign.add("+");
        color.add("#6E6E6E");

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {

                if (position == 0) {
                    Intent addDummy = new Intent(AddFriendActivity.this, AddDummyActivity.class);
                    startActivity(addDummy);
                }
                else {

                }

            }

        });

        // add friend via QRCode? (name, surname & email address)
    }
}
