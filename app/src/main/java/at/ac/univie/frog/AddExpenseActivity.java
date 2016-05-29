package at.ac.univie.frog;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import at.ac.univie.SplitDAO.Expense;
import at.ac.univie.SplitDAO.Friend;
import at.ac.univie.SplitDAO.FriendManager;
import at.ac.univie.SplitDAO.GroupManager;

public class AddExpenseActivity extends AppCompatActivity {

    CheckedTextView location;
    ExpandableListView categoryView, currencyView;
    ExpandableListAdapter adapterCategory, adapterCurrency;
    ArrayList<Group> categories = new ArrayList();
    ArrayList<Group> currency = new ArrayList();
    ExpandableListView splitView;
    ExpandableListAdapter adapter;
    ArrayList<Friend> friends = new ArrayList();
    ArrayList<String> friendsToString = new ArrayList();
    ArrayList<Group> options = new ArrayList();
    Button button;
    Expense newexpense;

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_add_expense);
        getSupportActionBar().setTitle("Add Expense");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_button);

        location = (CheckedTextView) findViewById(R.id.textLocation);
        categoryView = (ExpandableListView) findViewById(R.id.textCategory);
        currencyView = (ExpandableListView) findViewById(R.id.viewCurrency);
        splitView = (ExpandableListView) findViewById(R.id.viewSplitOptions);

        FriendManager frienddao = new FriendManager();
        GroupManager groupdao = new GroupManager();


        try {
            frienddao.loadFriendData(getApplicationContext(), "Friends");
            groupdao.loadGroupData(getApplicationContext(), "Groups");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        friends =  frienddao.getFriendList();

        for (Friend temp : friends)
            friendsToString.add(temp.getName() + " " + temp.getSurname());

        Group groupFriends = new Group("Who participated?", friendsToString);
        Group groupPayer = new Group("Who paid?", friendsToString);

        options.add(groupFriends);
        options.add(groupPayer);

        adapter = new FancyExpandableListAdapter(this, options);
        button = (Button) findViewById(R.id.button);

        splitView.setAdapter(adapter);



        splitView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            View currentView;
            boolean childClicked = false;
            boolean nope = false;

            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {


                //TODO deselect doesn't work yet :(
                    if (!view.isSelected()) {
                        Toast.makeText(getApplicationContext(), "checked = " + childClicked, Toast.LENGTH_SHORT).show();
                        view.setBackgroundColor(Color.parseColor("#79d2a6"));
                        view.setSelected(true);
                        nope = true;
                        currentView = view;
                    }

                    else {
                        Toast.makeText(getApplicationContext(), "hi" + childClicked, Toast.LENGTH_SHORT).show();
                        view.setBackgroundColor(Color.TRANSPARENT);
                        view.setSelected(true);
                        childClicked = true;
                        currentView = view;
                    }

                return true;
            }
        });

        button = (Button) findViewById(R.id.forward);

        ArrayList<String> listCategory = new ArrayList();
        ArrayList<String> listCurrency = new ArrayList();

        listCategory.add("Transport");
        listCategory.add("Food");
        listCategory.add("Culture");

        listCurrency.add("€");
        listCurrency.add("$");
        listCurrency.add("§");

        String defaultCurrency = "€"; // dynamically from (?) shared preferences

        Group groupCategory = new Group("Category", listCategory);
        Group groupCurrency = new Group("Amount in " + defaultCurrency, listCurrency);

        categories.add(groupCategory);
        currency.add(groupCurrency);

        adapterCategory = new FancyExpandableListAdapter(this, categories);

        categoryView.setAdapter(adapterCategory);

        adapterCurrency = new FancyExpandableListAdapter(this, currency);

        currencyView.setAdapter(adapterCurrency);

        categoryView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                ExpandableListAdapter itemAdapter = parent.getExpandableListAdapter();
                String selectedItem = (String) itemAdapter.getChild(groupPosition, childPosition);
                Group group = (Group) itemAdapter.getGroup(groupPosition);
                group.setName(selectedItem);
                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                }

                return true;

            }
        });

        currencyView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                ExpandableListAdapter itemAdapter = parent.getExpandableListAdapter();
                String selectedItem = (String) itemAdapter.getChild(groupPosition, childPosition);
                Group group = (Group) itemAdapter.getGroup(groupPosition);
                group.setName("Amount in " + selectedItem);
                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                }

                return true;

            }
        });



        location.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (location.isChecked())
                    location.setChecked(false);
                else
                    location.setChecked(true);

            }
        });

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View view) {
                Intent goToSplitOptions = new Intent(AddExpenseActivity.this, SplitOptionActivity.class);
                startActivity(goToSplitOptions);
            }
        });

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        categoryView.setIndicatorBoundsRelative(categoryView.getRight()-200, categoryView.getWidth());
        currencyView.setIndicatorBoundsRelative(currencyView.getRight()-500, currencyView.getWidth());
        splitView.setIndicatorBoundsRelative(splitView.getRight()-200, splitView.getWidth());

    }

}
