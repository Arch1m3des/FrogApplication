package at.ac.univie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

import at.ac.univie.SplitDAO.Friend;
import at.ac.univie.SplitDAO.FriendManager;
import at.ac.univie.adapter.SimpleListAdapter;
import at.ac.univie.frog.R;

public class SplitViewActivity extends AppCompatActivity {

    ListView listView;
    ListAdapter adapter;
    ArrayList<Friend> friends = new ArrayList();
    ArrayList<String> friendsToString = new ArrayList();

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_done:
                //Intent goToSplitOptions = new Intent(AddExpenseActivity.this, SplitOptionActivity.class);
                Intent goToGroupDetail = new Intent(SplitViewActivity.this, GroupDetailActivity.class);

                finish();
                startActivity(goToGroupDetail);
                return true;
            default: return  false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_split_view);

        String splitOption = getIntent().getStringExtra("option");

        getSupportActionBar().setTitle("Split" + splitOption);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        listView = (ListView) findViewById(R.id.splitListView);

        FriendManager frienddao = new FriendManager();

        try {
            frienddao.loadFriendData(getApplicationContext(), "Friends");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        friends =  frienddao.getFriendList();

        for (Friend temp : friends)
            friendsToString.add(temp.getName() + " " + temp.getSurname());

        adapter = new SimpleListAdapter(this, R.layout.simple_edit_list, friendsToString);

        listView.setAdapter(adapter);

    }
}
