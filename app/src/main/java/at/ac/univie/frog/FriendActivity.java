package at.ac.univie.frog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class FriendActivity extends AppCompatActivity {

    ListView friendsView;
    ArrayAdapter adapter;
    ArrayList<String> friends;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_friend);

        friends = new ArrayList();

        friends.addAll(Arrays.asList("Markus", "Andy", "Daniel", "Freund1", "Freund2", "Freund3", "Freund4", "Freund5", "Freund6", "Freund7", "Freund8", "Freund9", "Freund10"));

        friendsView = (ListView) findViewById(R.id.friendsView);
        adapter = new FancyListAdapter(this, R.layout.fancy_list, friends);

        friendsView.setAdapter(adapter);
    }
}
