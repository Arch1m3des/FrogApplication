package at.ac.univie.frog;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import at.ac.univie.SplitDAO.*;
import at.ac.univie.SplitDAO.Group;

public class AddGroupActivity extends AppCompatActivity {

    ExpandableListView listView;
    ExpandableListAdapter adapter;
    ArrayList<Friend> friends = new ArrayList<>();
    ArrayList<Group> groups;
    ArrayList<Child> friendsToString = new ArrayList<>();
    ArrayList<Child> currency = new ArrayList<>();
    ArrayList<Parent> parents = new ArrayList<>();
    Button btnAddGroup;
    EditText groupName;
    GroupManager groupdao;
    FriendManager frienddao;
    ArrayList<Friend> friendpos;

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_add_group);
        getSupportActionBar().setTitle("Add Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView group=(TextView) findViewById(R.id.imageGroupsWithText);
        group.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_group_clicked,0,0);
        group.setTextColor(Color.parseColor("#000000"));

        groupName = (EditText) findViewById(R.id.groupName);
        btnAddGroup = (Button) findViewById(R.id.addGroup);

        groupdao = new GroupManager();
        frienddao =  new FriendManager();
        friendpos = new ArrayList<>();

        try {
            groupdao.loadGroupData(getApplicationContext(), "Groups");
            frienddao.loadFriendData(getApplicationContext(), "Friends");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        btnAddGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO check and add friendpos

                if (groupName.getText().length() == 0)
                    Toast.makeText(getApplicationContext(), "Please add a group name.", Toast.LENGTH_LONG).show();
                else if (friendpos.size() == 0)
                    Toast.makeText(getApplicationContext(), "Please select at least one friend.", Toast.LENGTH_LONG).show();
                else {

                    at.ac.univie.SplitDAO.Group newgroup = new Group(groups.size(), groupName.getText().toString());
                    for (Friend friend : friendpos) {
                        newgroup.addMember(friend);
                    }
                    groups.add(newgroup);
                    groupdao.setGroupList(groups);
                    try {
                        groupdao.saveGroupData(getApplicationContext(),"Groups");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Intent goToGroup=new Intent(AddGroupActivity.this,GroupActivity.class);
                    startActivity(goToGroup);
                }
            }
        });



        listView = (ExpandableListView) findViewById(R.id.listView);

        currency.add(new Child("€"));
        currency.add(new Child("$"));
        currency.add(new Child("¥"));
        currency.add(new Child("£"));
        currency.add(new Child("Fr"));

        Friend me = frienddao.getFriendList().get(0);
        friends =  frienddao.getFriendList();
        groups = groupdao.getGroupList();

        for (Friend temp : friends) {
            friendsToString.add(new Child(temp.getName() + " " + temp.getSurname()));
        }

        Parent parentCurrency = new Parent("Default Currency", currency);
        Parent parentFriends = new Parent("Add Friends", friendsToString);

        parents.add(parentCurrency);
        parents.add(parentFriends);

        adapter = new FancyExpandableListAdapter(this, parents);

        listView.setAdapter(adapter);


        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {


            /*

                   //Who participated? Multiple checks possible.
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {



            ExpandableListAdapter itemAdapter = parent.getExpandableListAdapter();
            Child child = (Child) itemAdapter.getChild(groupPosition, childPosition);
            turn = true;

            if (!child.isSelected()) {
                child.setSelected(true);
                view.setBackgroundColor(Color.parseColor("#7ecece"));
                turn = false;

                if (!friendpos.contains(friends.get(childPosition))) {
                    friendpos.add(friends.get(childPosition));
                }
            }

            if (child.isSelected() && turn == true) {
                child.setSelected(false);
                view.setBackgroundColor(Color.TRANSPARENT);

                if (friendpos.contains(friends.get(childPosition))) {
                    friendpos.remove(friends.get(childPosition));
                }
            }
            return false;
        }
    });
             */

            View currentView;
            boolean turn = true;

            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {

                ExpandableListAdapter itemAdapter = parent.getExpandableListAdapter();
                Child child = (Child) itemAdapter.getChild(groupPosition, childPosition);
                turn = true;

                if (!child.isSelected()) {
                    child.setSelected(true);
                    view.setBackgroundColor(Color.parseColor("#7ecece"));
                    turn = false;

                    if (!friendpos.contains(friends.get(childPosition))) {
                        friendpos.add(friends.get(childPosition));
                    }
                }

                if (child.isSelected() && turn == true) {
                    child.setSelected(false);
                    view.setBackgroundColor(Color.TRANSPARENT);

                    if (friendpos.contains(friends.get(childPosition))) {
                        friendpos.remove(friends.get(childPosition));
                    }
                }


                return false;
            }
        });
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        listView.setIndicatorBoundsRelative(listView.getRight() - 200, listView.getWidth());
    }

    public void gotToFriendsActivity(View v){
        Intent goToFriends=new Intent(AddGroupActivity.this,FriendActivity.class);
        startActivity(goToFriends);
    }

    public void goToGroupActivity(View v){
        Intent goToGroup=new Intent(AddGroupActivity.this,GroupActivity.class);
        startActivity(goToGroup);
    }

    public void goToMap(View v){
        Intent goToMaps = new Intent(AddGroupActivity.this, MapView.class);
        startActivity(goToMaps);
    }

    public void goToMeActivity(View v){
        Intent goToMe=new Intent(AddGroupActivity.this,MeActivity.class);
        startActivity(goToMe);
    }

    public void goToSettings(View v){
        Intent goToSettings=new Intent(AddGroupActivity.this, SettingActivity.class);
        startActivity(goToSettings);
    }

}
