package at.ac.univie;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import at.ac.univie.SplitDAO.*;
import at.ac.univie.adapter.Child;
import at.ac.univie.adapter.FancyExpandableListAdapter;
import at.ac.univie.adapter.Parent;
import at.ac.univie.frog.R;
import at.ac.univie.main.FriendActivity;
import at.ac.univie.main.GroupActivity;
import at.ac.univie.main.MapView;
import at.ac.univie.main.MeActivity;
import at.ac.univie.main.SettingActivity;

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
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView group=(TextView) findViewById(R.id.imageGroupsWithText);
        group.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_group_clicked,0,0);
        group.setTextColor(Color.parseColor("#000000"));

        listView = (ExpandableListView) findViewById(R.id.textMember);
        ArrayList<Parent> groups = new ArrayList();
        ArrayList<Child> members = new ArrayList();

        Intent intent = getIntent();
        int groupindex = intent.getIntExtra("groupindex", 0);
        int expenseindex = intent.getIntExtra("expenseindex", 0);

        GroupManager groupdao = new GroupManager();
        try {
            groupdao.loadGroupData(getApplicationContext(), "Groups");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<at.ac.univie.SplitDAO.Group> groupList = groupdao.getGroupList();

        List<Expense> list = groupList.get(groupindex).getExpenses();
        Expense thisexpense = list.get(expenseindex);
        try {
            thisexpense.calculateDebt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DecimalFormat doubleform = new DecimalFormat("#.##");
        SimpleDateFormat dateform = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMAN);


        TextView txt_desc = (TextView) findViewById(R.id.textDescription);
        TextView txt_amt = (TextView) findViewById(R.id.textAmount);
        TextView txt_date = (TextView) findViewById(R.id.textDate);
        TextView txt_cat = (TextView) findViewById(R.id.textCategory);
        txt_amt.setText(txt_amt.getText().toString() + "  " + doubleform.format(thisexpense.getAmount()));
        txt_date.setText(txt_date.getText().toString() + "  " + dateform.format(thisexpense.getDate()));
        txt_cat.setText(txt_cat.getText().toString() + "  " + thisexpense.getCategory());

        txt_desc.setText(txt_desc.getText().toString() + " " + thisexpense.getDescription());

        if (thisexpense.getSpending().size() == thisexpense.getParticipants().size()) {
            for (int i= 0; i<thisexpense.getParticipants().size(); i++) {
                String name = thisexpense.getParticipants().get(i).getName() + " " + thisexpense.getParticipants().get(i).getSurname();
                double amount = thisexpense.getSpending().get(i);
                members.add(new Child(name + "    " + doubleform.format(amount)));
            }
        }

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

    public void goToSettings(View v){
        Intent goToSettings=new Intent(ExpenseDetailActivity.this, SettingActivity.class);
        startActivity(goToSettings);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        listView.setIndicatorBoundsRelative(listView.getRight() - 200, listView.getWidth());
    }
}
