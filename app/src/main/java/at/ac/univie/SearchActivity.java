package at.ac.univie;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import at.ac.univie.SplitDAO.*;
import at.ac.univie.SplitDAO.Group;
import at.ac.univie.adapter.SearchListAdapter;
import at.ac.univie.adapter.SimpleListAdapter;
import at.ac.univie.frog.R;
import at.ac.univie.main.FriendActivity;
import at.ac.univie.main.GroupActivity;
import at.ac.univie.main.MapView;
import at.ac.univie.main.SettingActivity;

public class SearchActivity extends AppCompatActivity {
    ListView groupView;
    ArrayAdapter adapter;
    ArrayList<Group> groups = new ArrayList();
    ArrayList<String> groupsToString = new ArrayList();
    GroupManager groupdao;


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_search);
        getSupportActionBar().setTitle("Gruppe wählen");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView settings = (TextView) findViewById(R.id.imageMapWithText);
        settings.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_map_clicked,0,0);
        settings.setTextColor(Color.parseColor("#000000"));

        groupdao = new GroupManager();

        try {
            groupdao.loadGroupData(getApplicationContext(), "Groups");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        groups = groupdao.getGroupList();

        for (Group temp : groups) {
            groupsToString.add(temp.getName());
        }

        groupView = (ListView) findViewById(R.id.groupView);
        adapter = new SearchListAdapter(this, R.layout.search_list, groupsToString);

        groupView.setAdapter(adapter);

        groupView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                Intent mapGroup = new Intent(SearchActivity.this, MapView.class);
                mapGroup.putExtra("GroupPosition", position);
                startActivity(mapGroup);
            }

        });
    }

    public void gotToFriendsActivity(View v){
        Intent goToFriends = new Intent(SearchActivity.this, FriendActivity.class);
        startActivity(goToFriends);
    }

    public void goToGroupActivity(View v){
        Intent goToGroups=new Intent(SearchActivity.this,GroupActivity.class);
        startActivity(goToGroups);
    }

    public void goToMap(View v){
        Intent goToMaps=new Intent(SearchActivity.this,MapView.class);
        startActivity(goToMaps);
    }

    public void goToSettings(View v){
        Intent goToSettings=new Intent(SearchActivity.this , SettingActivity.class);
        startActivity(goToSettings);
    }
}
