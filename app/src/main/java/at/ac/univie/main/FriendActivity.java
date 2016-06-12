package at.ac.univie.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import at.ac.univie.SplitDAO.Friend;
import at.ac.univie.SplitDAO.FriendManager;
import at.ac.univie.SplitDAO.Group;
import at.ac.univie.SplitDAO.GroupManager;
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
    boolean doubleBackToExitPressedOnce=false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

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



        //calculate friendbalance
        GroupManager groupdao = new GroupManager();
        try {
            groupdao.loadGroupData(getApplicationContext(), "Groups");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<Group> groups = new ArrayList<>();
        groups = groupdao.getGroupList();

        DecimalFormat doubleform = new DecimalFormat("#.##");
        ArrayList<Double> balance = new ArrayList<>();

        for (Friend owingfriend : friends) {
            if(owingfriend != me) {
                double owes = 0;
                for (Group temp : groups) {

                    ArrayList<Friend> members = (ArrayList<Friend>) temp.getMembers();
                    boolean contains = false;
                    for (Friend friend : members) {
                        if(friend.getFriendID() == owingfriend.getFriendID()) {
                            contains = true;
                            break;
                        }
                    }

                    if(contains) {
                        owes += temp.calculateowes(me, owingfriend);
                    }
                }
                balance.add(owes);
            }

        }

        //end


        int i=0;
        for (Friend temp : friends) {
            if (temp != me) { // in order to not see yourself as a friend
                friendsToString.add(temp.getName() + " " + temp.getSurname());
                amount.add(((balance.get(i) >0) ? "+" : "") + doubleform.format(balance.get(i++)) + "€");
                date.add("tap for details");
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