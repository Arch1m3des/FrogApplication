package at.ac.univie.main;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import at.ac.univie.SplitDAO.Friend;
import at.ac.univie.SplitDAO.FriendManager;
import at.ac.univie.adapter.FancyListAdapter;
import at.ac.univie.AddDummyActivity;
import at.ac.univie.FriendDetailActivity;
import at.ac.univie.frog.R;

/**
 * Created by Tamara on 13.05.16.
 */

public class FriendActivity extends AppCompatActivity {

    ListView friendsView;
    ArrayAdapter adapter;
    ArrayList<Friend> friends = new ArrayList();
    ArrayList<String> friendsToString = new ArrayList();
    ArrayList<String> friendsInitials = new ArrayList();
    ArrayList<String> amount = new ArrayList();
    ArrayList<String> date = new ArrayList();
    ArrayList<String> iconColors = new ArrayList();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_add:
                Intent addFriend = new Intent(FriendActivity.this, AddDummyActivity.class);
                startActivity(addFriend);
                return true;
            default: return  false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_friend);
        getSupportActionBar().setTitle("Friends");
        getSupportActionBar().setElevation(0);
        // getSupportActionBar().setHomeAsUpIndicator(R.drawable.katze); // if different icon is desired

        TextView group=(TextView) findViewById(R.id.imageFriendsWithText);
        group.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_friends_clicked,0,0);
        group.setTextColor(Color.parseColor("#000000"));

        Intent intent = getIntent();

        //maybe needed, maybe not TODO delete later
        /*
        if(intent.getStringArrayExtra("values") != null) {
            String[] parts = intent.getStringArrayExtra("values");
            Toast.makeText(getApplicationContext(), "Dummy \"" + parts[1] + " " + parts[0] + "\" added", Toast.LENGTH_SHORT).show();
        }
        */

        FriendManager frienddao = new FriendManager();
        try {
            frienddao.loadFriendData(getApplicationContext(), "Friends");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Friend me = frienddao.getFriendList().get(0);

        friends =  frienddao.getFriendList();


        //TODO delete because shared prefs friends are not needed any longer
        /*
        //Wenn der intent ein StringArrayExtra mit dem Namen values hat wird dieser ausgelesen
        //Bevor der Intent ausgelesen werden kann, muss zuerst die Liste der Freunde geladen werden
        if(intent.getStringArrayExtra("values")!=null){
            String[] newFriend = intent.getStringArrayExtra("values");

            //Einen neuen Freund der Liste hinzufuegen, die Nummer wird durch die Anzahl der Elemente in der Liste +1 festgelegt.
            friends.add(new Friend(friends.size()+1,newFriend[0],newFriend[1],newFriend[2]));
        }
        */

        for (Friend temp : friends) {
            if (temp != me) { // in order to not see yourself as a friend
                friendsToString.add(temp.getName() + " " + temp.getSurname());
                amount.add("");
                date.add("");
                friendsInitials.add(temp.getInitials());
                iconColors.add(temp.getIconColor());
            }
        }

        friendsView = (ListView) findViewById(R.id.friendsView);
        adapter = new FancyListAdapter(this, R.layout.fancy_list, friendsToString, friendsInitials, amount, date, iconColors);

        friendsView.setAdapter(adapter);

        friendsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                Intent detailFriend = new Intent(FriendActivity.this, FriendDetailActivity.class);
                detailFriend.putExtra("name", friendsToString.get(position));
                detailFriend.putExtra("friendposition", position+1);
                startActivity(detailFriend);
            }

        });

    }

    public void gotToFriendsActivity(View v){
        //Do Nothing
    }

    public void goToGroupActivity(View v){
        Intent goToFriends=new Intent(FriendActivity.this,GroupActivity.class);
        startActivity(goToFriends);
    }

    public void goToMap(View v){
        Intent goToMaps = new Intent(FriendActivity.this, MapView.class);
        startActivity(goToMaps);
    }


    public void goToSettings(View v){
        Intent goToSettings=new Intent(FriendActivity.this, SettingActivity.class);
        startActivity(goToSettings);
    }
}