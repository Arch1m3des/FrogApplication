package at.ac.univie.frog;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import at.ac.univie.SplitDAO.*;
import at.ac.univie.SplitDAO.Group;

/**
 * Created by Tamara on 13.05.16.
 */

public class FriendDetailActivity extends AppCompatActivity {

    ListView groupView;
    ListAdapter adapter;
    TextView name;
    ArrayList<Group> groups = new ArrayList();
    ArrayList<String> groupsToString = new ArrayList();
    ArrayList<String> iconColors = new ArrayList();
    ArrayList<String> text = new ArrayList();
    ArrayList<String> date = new ArrayList();
    ArrayList<String> amount = new ArrayList();
    Friend me;
    Friend owingfriend;

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_friend_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Friend Details");
        getSupportActionBar().setElevation(0);

        TextView group=(TextView) findViewById(R.id.imageFriendsWithText);
        group.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_friends_clicked,0,0);
        group.setTextColor(Color.parseColor("#000000"));

        GroupManager groupdao = new GroupManager();
        FriendManager frienddao =  new FriendManager();
        name = (TextView) findViewById(R.id.name);

        Intent intent = getIntent();
        int friendposition = intent.getIntExtra("friendposition",0);


        try {
            groupdao.loadGroupData(getApplicationContext(), "Groups");
            frienddao.loadFriendData(getApplicationContext(), "Friends");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        me = frienddao.getFriendList().get(0);  //TODO 0 is always me
        owingfriend = frienddao.getFriendList().get(friendposition);
        groups = groupdao.getGroupList();

        String nameString = owingfriend.getName() + " " + owingfriend.getSurname(); //intent.getStringExtra("name");
        name.setText(nameString);


        DecimalFormat doubleform = new DecimalFormat("#.##");
        SimpleDateFormat dateform = new SimpleDateFormat("dd.MM.", Locale.GERMAN);
        Date today =  new Date();

        for (Group temp : groups) {
            //TODO Balance in green or red depending on + or - and smaller
            //show only groups where owingfriend is a member

            ArrayList<Friend> members = (ArrayList<Friend>) temp.getMembers();
            boolean contains = false;
            for (Friend friend : members) {
                if(friend.getFriendID() == owingfriend.getFriendID()) {
                    contains = true;
                    break;
                }
            }

            if(contains) {
                double owes = temp.calculateowes(me, owingfriend);
                if(owes==0) continue;
                else {
                    groupsToString.add(temp.getName());
                    amount.add(doubleform.format(owes) + "€");
                    date.add("last expense: " + dateform.format(today));
                    iconColors.add(temp.getIconColor());
                    text.add("");
                }
            }

        }

        groupView = (ListView) findViewById(R.id.groupView);
        adapter = new FancyListAdapter(this, R.layout.fancy_list, groupsToString, text, amount, date, iconColors);

        groupView.setAdapter(adapter);
    }

    public void gotToFriendsActivity(View v){
        Intent goToFriends=new Intent(FriendDetailActivity.this,FriendActivity.class);
        startActivity(goToFriends);
    }

    public void goToGroupActivity(View v){
        Intent goToFriends=new Intent(FriendDetailActivity.this,GroupActivity.class);
        startActivity(goToFriends);
    }

    public void goToMap(View v){
        Intent goToMaps = new Intent(FriendDetailActivity.this, MapView.class);
        startActivity(goToMaps);
    }

    public void goToMeActivity(View v){
        Intent goToMe=new Intent(FriendDetailActivity.this,MeActivity.class);
        startActivity(goToMe);
    }

    public void goToSettings(View v){
        Intent goToSettings=new Intent(FriendDetailActivity.this, SettingActivity.class);
        startActivity(goToSettings);
    }
}