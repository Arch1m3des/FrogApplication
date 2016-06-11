package at.ac.univie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import at.ac.univie.SplitDAO.*;
import at.ac.univie.SplitDAO.Group;
import at.ac.univie.adapter.Child;
import at.ac.univie.adapter.FancyExpandableListAdapter;
import at.ac.univie.adapter.Parent;
import at.ac.univie.frog.R;
import at.ac.univie.main.FriendActivity;
import at.ac.univie.main.GroupActivity;
import at.ac.univie.main.MapView;
import at.ac.univie.main.MeActivity;
import at.ac.univie.main.SettingActivity;

public class AddGroupActivity extends AppCompatActivity {

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

                if (groupName.getText().length() == 0)
                    Toast.makeText(getApplicationContext(), "Please add a group name.", Toast.LENGTH_LONG).show();
                else if (friendpos.size() == 0)
                    Toast.makeText(getApplicationContext(), "Please select at least one friend.", Toast.LENGTH_LONG).show();
                else {

                    at.ac.univie.SplitDAO.Group newgroup = new Group(groups.size(), groupName.getText().toString());
                    newgroup.setCurrencies(groupCurrencies);
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

                return true;
            default: return  false;
        }
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
        HashMap<String, String> currmap = curr.sortedCurrenciesCountries();
        Iterator it = currmap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry valuepair = (Map.Entry)it.next();
            currency.add(new Child((String) valuepair.getKey() + " " + valuepair.getValue()));
        }

        Friend me = frienddao.getFriendList().get(0);
        friends =  frienddao.getFriendList();
        groups = groupdao.getGroupList();

        for (Friend temp : friends) {
            friendsToString.add(new Child(temp.getName() + " " + temp.getSurname()));
        }

        Parent parentCurrency = new Parent("Select Currencies", currency);
        Parent parentFriends = new Parent("Add Friends", friendsToString);

        currencyParent.add(parentCurrency);
        participantParent.add(parentFriends);

        currencyAdapter = new FancyExpandableListAdapter(this, currencyParent, true);
        participantAdapter = new FancyExpandableListAdapter(this, participantParent, true);

        currencyView.setAdapter(currencyAdapter);
        participantView.setAdapter(participantAdapter);


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
                        groupCurrencies.add(currency.get(childPosition).getName().substring(0,3));
                    }
                }

                if (child.isSelected() && turn == true) {
                    child.setSelected(false);
                    view.setBackgroundColor(Color.TRANSPARENT);

                    if (groupCurrencies.contains(currency.get(childPosition).toString().substring(0,3))) {
                        groupCurrencies.remove(currency.get(childPosition).toString().substring(0,3));
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

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        currencyView.setIndicatorBoundsRelative(currencyView.getRight() - 200, currencyView.getWidth());
        participantView.setIndicatorBoundsRelative(participantView.getRight() - 200, participantView.getWidth());
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

    public void goToSettings(View v){
        Intent goToSettings=new Intent(AddGroupActivity.this, SettingActivity.class);
        startActivity(goToSettings);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}
