package at.ac.univie.frog;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ExpenseDetailActivity extends AppCompatActivity {

    ExpandableListView listView;
    ExpandableListAdapter adapter;

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_expense_detail);
        getSupportActionBar().setTitle("Expense Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView group=(TextView) findViewById(R.id.imageGroupsWithText);
        group.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_group_clicked,0,0);
        group.setTextColor(Color.parseColor("#000000"));

        listView = (ExpandableListView) findViewById(R.id.textMember);
        ArrayList<Parent> groups = new ArrayList();
        ArrayList<Child> members = new ArrayList();

        members.add(new Child("Markus Bader"));
        members.add(new Child("Andy Weinbahn"));
        members.add(new Child("Daniel Bubla"));

        Parent parent = new Parent("Members", members);

        groups.add(parent);

        adapter = new FancyExpandableListAdapter(this, groups);

        listView.setAdapter(adapter);
    }

    public void gotToFriendsActivity(View v){
        Intent goToFriends=new Intent(ExpenseDetailActivity.this,FriendActivity.class);
        startActivity(goToFriends);
    }

    public void goToGroupActivity(View v){
        Intent goToFriends=new Intent(ExpenseDetailActivity.this,GroupActivity.class);
        startActivity(goToFriends);
    }

    public void goToMap(View v){
        Intent goToMaps = new Intent(ExpenseDetailActivity.this, MapView.class);
        startActivity(goToMaps);
    }

    public void goToMeActivity(View v){
        Intent goToMe=new Intent(ExpenseDetailActivity.this,MeActivity.class);
        startActivity(goToMe);
    }

    public void goToSettings(View v){
        Intent goToSettings=new Intent(ExpenseDetailActivity.this, SettingActivity.class);
        startActivity(goToSettings);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        listView.setIndicatorBoundsRelative(listView.getRight() - 200, listView.getWidth());
    }
}
