package at.ac.univie.frog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import at.ac.univie.SplitDAO.Group;

/**
 * Created by Tamara on 13.05.16.
 */


public class GroupActivity extends AppCompatActivity {

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
}
