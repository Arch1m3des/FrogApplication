package at.ac.univie.frog;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import at.ac.univie.SplitDAO.Friend;
import at.ac.univie.SplitDAO.FriendManager;

public class AddGroupActivity extends AppCompatActivity {

    ExpandableListView listView;
    ExpandableListAdapter adapter;
    ArrayList<Friend> friends = new ArrayList<>();
    ArrayList<Child> friendsToString = new ArrayList<>();
    ArrayList<Child> currency = new ArrayList<>();
    ArrayList<Parent> parents = new ArrayList<>();

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

        TextView group=(TextView) findViewById(R.id.imageGroupsWithText);
        group.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_group_clicked,0,0);
        group.setTextColor(Color.parseColor("#000000"));

        listView = (ExpandableListView) findViewById(R.id.listView);

        FriendManager frienddao = new FriendManager();

        try {
            frienddao.loadFriendData(getApplicationContext(), "Friends");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        currency.add(new Child("€"));
        currency.add(new Child("$"));
        currency.add(new Child("¥"));
        currency.add(new Child("£"));
        currency.add(new Child("Fr"));

        Friend me = frienddao.getFriendList().get(0);
        friends =  frienddao.getFriendList();

        for (Friend temp : friends) {
            if (temp != me)
                friendsToString.add(new Child(temp.getName() + " " + temp.getSurname()));
        }

        Parent parentCurrency = new Parent("Default Currency", currency);
        Parent parentFriends = new Parent("Add Friends", friendsToString);

        parents.add(parentCurrency);
        parents.add(parentFriends);

        adapter = new FancyExpandableListAdapter(this, parents);

        listView.setAdapter(adapter);

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            View currentView;

            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {

                listView.setChoiceMode(ExpandableListView.CHOICE_MODE_SINGLE);

                view.setSelected(true);
                if (currentView != null) {
                    currentView.setBackgroundColor(Color.TRANSPARENT);
                }
                currentView = view;
                currentView.setBackgroundColor(Color.parseColor("#676767"));

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
        Intent goToFriends=new Intent(AddGroupActivity.this,GroupActivity.class);
        startActivity(goToFriends);
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
