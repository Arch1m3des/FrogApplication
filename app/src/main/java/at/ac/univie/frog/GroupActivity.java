package at.ac.univie.frog;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import at.ac.univie.SplitDAO.Group;

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

        groups.add(new Group(1, "Thailand"));
        groups.add(new Group(2, "South America"));
        groups.add(new Group(3, "Wales"));
        groups.add(new Group(4, "Fieberbrunn"));
        groups.add(new Group(5, "Spa Weekend"));
        groups.add(new Group(6, "Gruppe 1"));
        groups.add(new Group(7, "Gruppe 2"));
        groups.add(new Group(8, "Gruppe 3"));
        groups.add(new Group(9, "Gruppe 4"));
        groups.add(new Group(10, "Gruppe 5"));

        groupsToString.add("New Group");
        text.add("+");
        iconColors.add("#6E6E6E");

        for (Group temp : groups) {
            groupsToString.add(temp.getName());
            iconColors.add(temp.getIconColor());
            text.add("");
        }

        groupView = (ListView) findViewById(R.id.groupView);
        adapter = new FancyListAdapter(this, R.layout.fancy_list, groupsToString, text, iconColors);

        groupView.setAdapter(adapter);
        
        
       
    }

    public void gotToFriendsActivity(View v){
        Intent goToFriends=new Intent(GroupActivity.this,FriendActivity.class);
        startActivity(goToFriends);
    }

    public void goToGroupActivity(View v){
        Toast toast=Toast.makeText(getApplicationContext(),"You are already in the Group Tab!",Toast.LENGTH_LONG);
        toast.show();
    }

    public void goToMap(View v){
        Toast toast=Toast.makeText(getApplicationContext(),"Not implemented yet!",Toast.LENGTH_LONG);
        toast.show();
    }

    public void goToMeActivity(View v){
        Intent goToMe=new Intent(GroupActivity.this,MeActivity.class);
        startActivity(goToMe);
    }

    public void goToSettings(View v){
        Toast toast=Toast.makeText(getApplicationContext(),"Not implemented yet!",Toast.LENGTH_LONG);
        toast.show();
    }
}
