package at.ac.univie.frog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import at.ac.univie.SplitDAO.Friend;

/**
 * Created by Tamara on 13.05.16.
 */

public class FriendActivity extends AppCompatActivity {

    ListView friendsView;
    ArrayAdapter adapter;
    ArrayList<Friend> friends = new ArrayList();
    ArrayList<String> friendsToString = new ArrayList();
    ArrayList<String> friendsInitials = new ArrayList();
    ArrayList<String> iconColors = new ArrayList();

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_friend);
        getSupportActionBar().setTitle("Friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setHomeAsUpIndicator(R.drawable.katze); // if different icon is desired


        friends.add(new Friend(1, "Weinbahn", "Andy", "ich@du.com"));
        friends.add(new Friend(2, "Bader", "Markus",  "ich@du.com"));
        friends.add(new Friend(3, "Bubla", "Daniel",  "ich@du.com"));
        friends.add(new Friend(4, "Eins", "Freund",  "ich@du.com"));
        friends.add(new Friend(5, "Zwei", "Freund",  "ich@du.com"));
        friends.add(new Friend(6, "Drei", "Freund",  "ich@du.com"));
        friends.add(new Friend(7, "Vier", "Freund",  "ich@du.com"));
        friends.add(new Friend(8, "Fuenf", "Freund",  "ich@du.com"));
        friends.add(new Friend(9, "Sechs", "Freund",  "ich@du.com"));
        friends.add(new Friend(10, "Sieben", "Freund",  "ich@du.com"));

        friendsToString.add("Add Friend");
        friendsInitials.add("+");
        iconColors.add("#6E6E6E");

        for (Friend temp : friends) {
            friendsToString.add(temp.getName() + " " + temp.getSurname());
            friendsInitials.add(temp.getInitials());
            iconColors.add(temp.getIconColor());
        }

        friendsView = (ListView) findViewById(R.id.friendsView);
        adapter = new FancyListAdapter(this, R.layout.fancy_list, friendsToString, friendsInitials, iconColors);

        friendsView.setAdapter(adapter);

        friendsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {

                if (position == 0) {
                    Intent addFriend = new Intent(FriendActivity.this, AddFriendActivity.class);
                    startActivity(addFriend);
                }
                else {
                    Intent detailFriend = new Intent(FriendActivity.this, FriendDetailActivity.class);
                    startActivity(detailFriend);
                }

            }

        });

    }
}
