package at.ac.univie;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import at.ac.univie.SplitDAO.*;
import at.ac.univie.adapter.Child;
import at.ac.univie.adapter.FancyExpandableListAdapter;
import at.ac.univie.adapter.Parent;
import at.ac.univie.adapter.SimpleListAdapter;
import at.ac.univie.frog.R;
import at.ac.univie.main.FriendActivity;
import at.ac.univie.main.GroupActivity;
import at.ac.univie.main.MapView;
import at.ac.univie.main.MeActivity;
import at.ac.univie.main.SettingActivity;

public class ExpenseDetailActivity extends AppCompatActivity {

    ExpandableListView listView;
    ListView detailView;
    ArrayList<String> listDetail = new ArrayList();
    ListAdapter detailAdapter;
    ExpandableListAdapter adapter;
    Button location;
    double longitude;
    double latitude;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

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
        //TextView textAmountCurr = (TextView) findViewById(R.id.textAmountCurr);
        //TextView convRate = (TextView) findViewById(R.id.convRate);
        //group.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_group_clicked,0,0);
        //group.setTextColor(Color.parseColor("#000000"));

        listView = (ExpandableListView) findViewById(R.id.textMember);
        detailView = (ListView) findViewById(R.id.expenseDetail);
        ArrayList<Parent> groups = new ArrayList();
        ArrayList<Child> members = new ArrayList();

        Intent intent = getIntent();
        int groupIndex = intent.getIntExtra("groupindex", 0);
        int expenseIndex = intent.getIntExtra("expenseindex", 0);

        GroupManager groupDAO = new GroupManager();
        try {
            groupDAO.loadGroupData(getApplicationContext(), "Groups");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<at.ac.univie.SplitDAO.Group> groupList = groupDAO.getGroupList();

        List<Expense> list = groupList.get(groupIndex).getExpenses();
        Collections.reverse(list);
        Expense expense = list.get(expenseIndex);
        try {
            expense.calculateDebt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DecimalFormat doubleform = new DecimalFormat("#.##");
        SimpleDateFormat dateform = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
        Currencies curr = new Currencies();

        listDetail.add("Description: " + expense.getDescription());
        listDetail.add("from " + dateform.format(expense.getDate()));
        listDetail.add("Amount in €: " + doubleform.format(expense.getAmountInHomeCurrency()));
        if (!curr.getCurrencies().get(expense.getCurrency()).equals("€")) {
            listDetail.add("Amount in " + curr.getCurrencies().get(expense.getCurrency()) + ": " + doubleform.format(expense.getAmount()));
            listDetail.add("Exchange rate: " + doubleform.format((expense.getAmount() / expense.getAmountInHomeCurrency())));
        }
        listDetail.add("Category: " + expense.getCategory());

        if (expense.getSpending().size() == expense.getParticipants().size()) {
            for (int i= 0; i<expense.getParticipants().size(); i++) {
                String name = expense.getParticipants().get(i).getName() + " " + expense.getParticipants().get(i).getSurname();
                double amount = expense.getSpendingInHomeCurrency().get(i);
                members.add(new Child(name + "\t\t\t" + doubleform.format(amount) + "€"));
            }
        }

        Parent parent = new Parent("Members", members);

        groups.add(parent);

        adapter = new FancyExpandableListAdapter(this, groups, false);
        detailAdapter = new SimpleListAdapter(this, R.layout.simple_list, listDetail, "");

        listView.setAdapter(adapter);
        detailView.setAdapter(detailAdapter);

        location = (Button) findViewById(R.id.location);

        if (expense.getHasLocation()) {

            longitude = expense.getLongitude();
            latitude = expense.getLatitude();

            location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent goToMaps = new Intent(ExpenseDetailActivity.this, MapView.class);
                    goToMaps.putExtra("long", longitude);
                    goToMaps.putExtra("lat", latitude);
                    goToMaps.putExtra("where", "ExpenseDetailActivity.class");
                    startActivity(goToMaps);
                }
            });
        }

        else  {
            location.setVisibility(View.GONE);
        }


    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        listView.setIndicatorBoundsRelative(listView.getRight() - 200, listView.getWidth());
    }
}
