package at.ac.univie.frog;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import at.ac.univie.SplitDAO.Friend;
import at.ac.univie.SplitDAO.FriendManager;
import at.ac.univie.SplitDAO.Group;
import at.ac.univie.SplitDAO.GroupManager;

/**
 * Created by Tamara on 13.05.16.
 */


public class GroupActivity extends AppCompatActivity {

    ListView groupView;
    ArrayAdapter adapter;
    ArrayList<Group> groups = new ArrayList();
    ArrayList<String> groupsToString = new ArrayList();
    ArrayList<String> iconColors = new ArrayList();
    ArrayList<String> text = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_group);
        getSupportActionBar().setTitle("Groups");

        TextView group=(TextView) findViewById(R.id.imageGroupsWithText);
        group.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_group_clicked,0,0);
        group.setTextColor(Color.parseColor("#000000"));

        Intent intent = getIntent();
        // get from extra //groups =   (ArrayList<Group>)intent.getSerializableExtra("Groups");

        GroupManager groupdao = new GroupManager();
        FriendManager frienddao =  new FriendManager();
        try {
            groupdao.loadGroupData(getApplicationContext(), "Groups");
            frienddao.loadFriendData(getApplicationContext(), "Friends");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Friend me = frienddao.getFriendList().get(0);
        groups = groupdao.getGroupList();




        groupsToString.add("New Group");
        text.add("+");
        iconColors.add("#6E6E6E");


        for (Group temp : groups) {
                //TODO Balance in green or red depending on + or - and smaller
                groupsToString.add(temp.getName() + " " + temp.getsumexpenses() + "€");
                iconColors.add(temp.getIconColor());
                text.add("");
        }

        groupView = (ListView) findViewById(R.id.groupView);
        adapter = new FancyListAdapter(this, R.layout.fancy_list, groupsToString, text, iconColors);

        groupView.setAdapter(adapter);

        groupView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {

                if (position == 0) {
                    Intent addGroup = new Intent(GroupActivity.this, AddGroupActivity.class);
                    startActivity(addGroup);
                }
                else {
                    Intent detailGroup = new Intent(GroupActivity.this, GroupDetailActivity.class);
                    detailGroup.putExtra("GroupPosition", position-1);
                    startActivity(detailGroup);
                }

            }

        });




    }

    public void gotToFriendsActivity(View v){
        Intent goToFriends=new Intent(GroupActivity.this,FriendActivity.class);
        startActivity(goToFriends);
    }

    public void goToGroupActivity(View v){
        //Do Nothing
    }

    public void goToMap(View v){
        Intent goToMaps = new Intent(GroupActivity.this, MapView.class);
        startActivity(goToMaps);
    }

    public void goToMeActivity(View v){
        Intent goToMe=new Intent(GroupActivity.this,MeActivity.class);
        startActivity(goToMe);
    }

    public void goToSettings(View v){
        Intent goToSettings=new Intent(GroupActivity.this, SettingActivity.class);
        startActivity(goToSettings);
    }
}