package at.ac.univie;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import at.ac.univie.SplitDAO.Currencies;
import at.ac.univie.SplitDAO.Friend;
import at.ac.univie.SplitDAO.FriendManager;
import at.ac.univie.SplitDAO.Group;
import at.ac.univie.SplitDAO.GroupManager;
import at.ac.univie.adapter.Child;
import at.ac.univie.adapter.FancyExpandableListAdapter;
import at.ac.univie.adapter.Parent;
import at.ac.univie.frog.R;
import at.ac.univie.main.FriendActivity;
import at.ac.univie.main.GroupActivity;
import at.ac.univie.main.MapView;
import at.ac.univie.main.SettingActivity;

public class GroupSettingsActivity extends AppCompatActivity {

    ExpandableListView currencyView, participantView;
    ExpandableListAdapter currencyAdapter, participantAdapter;
    ArrayList<Friend> friends = new ArrayList<>();
    ArrayList<Group> groups;
    ArrayList<Child> friendsToString = new ArrayList<>();
    ArrayList<Child> currency = new ArrayList<>();
    ArrayList<Parent> participantParent = new ArrayList<>();
    ArrayList<Parent> currencyParent = new ArrayList();
    EditText groupName;
    GroupManager groupdao;
    FriendManager frienddao;
    ArrayList<Friend> friendpos;
    ArrayList<String> groupCurrencies;
    List<String> friendsSelected = new ArrayList();

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
                //TODO Settings uebernehmen schaetze ich
                Intent addGroup = new Intent(GroupSettingsActivity.this, GroupActivity.class);
                startActivity(addGroup);
                return true;
            default: return  false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_group_settings);
        getSupportActionBar().setTitle("Group Settings");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView group=(TextView) findViewById(R.id.imageGroupsWithText);
        group.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_group_clicked,0,0);
        group.setTextColor(Color.parseColor("#000000"));

        groupName = (EditText) findViewById(R.id.groupName);

        groupdao = new GroupManager();
        frienddao =  new FriendManager();
        friendpos = new ArrayList<>();
        groupCurrencies = new ArrayList<>();

        try {
            groupdao.loadGroupData(getApplicationContext(), "Groups");
            frienddao.loadFriendData(getApplicationContext(), "Friends");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        currencyView = (ExpandableListView) findViewById(R.id.currencyView);
        participantView = (ExpandableListView) findViewById(R.id.participantView);

        Currencies curr = new Currencies();
        HashMap<String, String> currmap = curr.getCurrencies();
        Iterator it = currmap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry valuepair = (Map.Entry)it.next();
            currency.add(new Child((String) valuepair.getKey() + "  " + valuepair.getValue()));
        }

        Friend me = frienddao.getFriendList().get(0);
        friends =  frienddao.getFriendList();
        groups = groupdao.getGroupList();
        List<Friend> list = groups.get(getIntent().getIntExtra("groupIndex", 0)).getMembers();
        for (Friend temp : list) {
            System.out.println(temp);
        }

        for (Friend temp : friends) {
            friendsToString.add(new Child(temp.getName() + " " + temp.getSurname()));
        }

        for (Friend temp : list) {
            friendsSelected.add(temp.getName() + " " + temp.getSurname());
        }

        Parent parentCurrency = new Parent("Select Currencies", currency);
        Parent parentFriends = new Parent("Add Friends", friendsToString);

        currencyParent.add(parentCurrency);
        participantParent.add(parentFriends);

        currencyAdapter = new FancyExpandableListAdapter(this, currencyParent, true);
        participantAdapter = new FancyExpandableListAdapter(this, participantParent, true);

        currencyView.setAdapter(currencyAdapter);
        participantView.setAdapter(participantAdapter);

        // highlighting already selected ones still missing!

        currencyView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

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

                        if (!groupCurrencies.contains(currency.get(childPosition))) {
                            groupCurrencies.add(currency.get(childPosition).getName());
                        }
                    }

                    if (child.isSelected() && turn == true) {
                        child.setSelected(false);
                        view.setBackgroundColor(Color.TRANSPARENT);

                        if (groupCurrencies.contains(currency.get(childPosition).toString())) {
                            groupCurrencies.remove(currency.get(childPosition).toString());
                        }
                    }
                return false;
            }
        });

        participantView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

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

    public void gotToFriendsActivity(View v){
        Intent goToFriends=new Intent(GroupSettingsActivity.this,FriendActivity.class);
        startActivity(goToFriends);
    }

    public void goToGroupActivity(View v){
        Intent goToGroups = new Intent(GroupSettingsActivity.this, GroupActivity.class);
        startActivity(goToGroups);
    }

    public void goToMap(View v){
        Intent goToMaps = new Intent(GroupSettingsActivity.this, MapView.class);
        startActivity(goToMaps);
    }

    public void goToSettings(View v){
        Intent goToSettings=new Intent(GroupSettingsActivity.this, SettingActivity.class);
        startActivity(goToSettings);
    }
}
