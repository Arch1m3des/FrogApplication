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
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import at.ac.univie.GroupSettingsActivity;
import at.ac.univie.SplitDAO.Friend;
import at.ac.univie.SplitDAO.FriendManager;
import at.ac.univie.SplitDAO.Group;
import at.ac.univie.SplitDAO.GroupManager;
import at.ac.univie.adapter.FancyListAdapter;
import at.ac.univie.AddGroupActivity;
import at.ac.univie.GroupDetailActivity;
import at.ac.univie.frog.R;

/**
 * Created by Tamara on 13.05.16.
 */


public class GroupActivity extends AppCompatActivity {

    ListView groupView;
    ArrayAdapter adapter;
    ArrayList<Group> groups = new ArrayList();
    Friend me;
    ArrayList<String> groupsToString = new ArrayList();
    ArrayList<String> iconColors = new ArrayList();
    ArrayList<String> text = new ArrayList();
    ArrayList<String> date = new ArrayList();
    ArrayList<String> amount = new ArrayList();
    GroupManager groupdao;
    FriendManager frienddao;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_add:
                Intent addGroup = new Intent(GroupActivity.this, AddGroupActivity.class);
                startActivity(addGroup);
                return true;
            default: return  false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_group);
        getSupportActionBar().setTitle("Groups");
        getSupportActionBar().setElevation(0);

        TextView group=(TextView) findViewById(R.id.imageGroupsWithText);
        group.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_group_clicked,0,0);
        group.setTextColor(Color.parseColor("#000000"));

        Intent intent = getIntent();
        if (intent.getStringExtra("check") != null)
            Toast.makeText(getApplicationContext(), "Your expense has been saved", Toast.LENGTH_SHORT).show();
        // get from extra //parents =   (ArrayList<Parent>)intent.getSerializableExtra("Groups");

        groupdao = new GroupManager();
        frienddao =  new FriendManager();

        try {
            groupdao.loadGroupData(getApplicationContext(), "Groups");
            frienddao.loadFriendData(getApplicationContext(), "Friends");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Friend me = frienddao.getFriendList().get(0);
        groups = groupdao.getGroupList();
        me = frienddao.getFriendList().get(0);


        DecimalFormat doubleform = new DecimalFormat("#.##");


        for (Group temp : groups) {
                //TODO Balance in green or red depending on + or - and smaller
                groupsToString.add(temp.getName());
                amount.add(doubleform.format(temp.getsumexpenses()) + "€");
                date.add("tap to see expenses");
                iconColors.add(temp.getIconColor());
                text.add("");
        }

        groupView = (ListView) findViewById(R.id.groupView);
        adapter = new FancyListAdapter(this, R.layout.fancy_list, groupsToString, text, amount, date, iconColors);

        groupView.setAdapter(adapter);

        groupView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                Intent detailGroup = new Intent(GroupActivity.this, GroupDetailActivity.class);
                detailGroup.putExtra("GroupPosition", position);
                detailGroup.putExtra("groupIndex", position);

                startActivity(detailGroup);
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

    public void goToSettings(View v){
        Intent goToSettings=new Intent(GroupActivity.this, SettingActivity.class);
        startActivity(goToSettings);
    }
}