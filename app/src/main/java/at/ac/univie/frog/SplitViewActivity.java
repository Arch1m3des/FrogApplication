package at.ac.univie.frog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

import at.ac.univie.SplitDAO.Friend;
import at.ac.univie.SplitDAO.FriendManager;

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
