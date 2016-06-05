package at.ac.univie.frog;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import at.ac.univie.SplitDAO.*;

public class AddExpenseActivity extends AppCompatActivity {

    CheckedTextView location;
    ExpandableListView categoryView, currencyView;
    ExpandableListAdapter adapterCategory, adapterCurrency;
    ArrayList<Parent> categories = new ArrayList();
    ArrayList<Parent> currency = new ArrayList();
    ExpandableListView splitView;
    ExpandableListAdapter adapter;
    ArrayList<at.ac.univie.SplitDAO.Group> groups = new ArrayList();
    ArrayList<Child> friendsToString = new ArrayList();
    ArrayList<Parent> options = new ArrayList();
    ArrayList<Child> splitOptions = new ArrayList();
    GroupManager groupdao;
    Button button;
    int groupindex;
    ArrayList<Friend> participants = new ArrayList<>();
    ArrayList<Friend> members;

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
                //Intent goToSplitOptions = new Intent(AddExpenseActivity.this, SplitOptionActivity.class);
                Intent goToSplitOptions = new Intent(AddExpenseActivity.this, SplitViewActivity.class);

                finish();
                startActivity(goToSplitOptions);
                return true;
            default: return  false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_add_expense);

        getSupportActionBar().setTitle("Add Expense");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_button);

        location = (CheckedTextView) findViewById(R.id.textLocation);
        categoryView = (ExpandableListView) findViewById(R.id.textCategory);
        currencyView = (ExpandableListView) findViewById(R.id.viewCurrency);
        splitView = (ExpandableListView) findViewById(R.id.viewSplitOptions);

        Intent intent = getIntent();
        groupindex = intent.getIntExtra("groupindex", 0);

        groupdao = new GroupManager();

        try {
            groupdao.loadGroupData(getApplicationContext(), "Groups");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        groups =  groupdao.getGroupList();
        at.ac.univie.SplitDAO.Group thisGroup = groups.get(groupindex);

        members = (ArrayList<Friend>) thisGroup.getMembers();
        for (Friend temp : members) {
            friendsToString.add(new Child(temp.getName() + " " + temp.getSurname()));
        }

        splitOptions.add(new Child("Split equally"));
        splitOptions.add(new Child("Split manually"));
        splitOptions.add(new Child("Split in %"));
        splitOptions.add(new Child("Split in parts"));

        Parent parentFriends = new Parent("Who participated?", friendsToString);
        Parent parentPayer = new Parent("Who paid?", friendsToString);
        Parent parentOption = new Parent("How to split?", splitOptions);

        options.add(parentFriends);
        options.add(parentPayer);
        options.add(parentOption);

        adapter = new FancyExpandableListAdapter(this, options);
        button = (Button) findViewById(R.id.button);

        splitView.setAdapter(adapter);

        int i = 0;
        for (Parent group : options) {
            for (Child child : group.getItems()) {
                child.setId(i++);
            }
        }

        splitView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            boolean turn = true;
            View currentView;

            //Who participated? Multiple checks possible.
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {

               if (groupPosition == 0) {

                // check that childs have ids zero to size of list
                    turn = true;
                    ExpandableListAdapter participationAdapter = parent.getExpandableListAdapter();
                    Child child = (Child) participationAdapter.getChild(groupPosition, childPosition);

                    if (!child.isSelected()) {
                        child.setSelected(true);
                        view.setBackgroundColor(Color.parseColor("#7ecece"));
                        turn = false;

                        if (!participants.contains(members.get(childPosition))) {
                            participants.add(members.get(childPosition));
                        }
                    }

                    if (child.isSelected() && turn == true) {
                        child.setSelected(false);
                        view.setBackgroundColor(Color.TRANSPARENT);

                        if (participants.contains(members.get(childPosition))) {
                            participants.remove(members.get(childPosition));
                        }
                    }
                    return false;
                }

                if (groupPosition == 1) {

                    splitView.setChoiceMode(ExpandableListView.CHOICE_MODE_SINGLE);

                    view.setSelected(true);
                    if (currentView != null) {
                        currentView.setBackgroundColor(Color.TRANSPARENT);
                    }
                    currentView = view;
                    currentView.setBackgroundColor(Color.parseColor("#676767"));
                    return false;

                }

                if (groupPosition == 3) {
                    turn = true;
                    ExpandableListAdapter splitAdapter = parent.getExpandableListAdapter();
                    Child child = (Child) splitAdapter.getChild(groupPosition, childPosition);

                    if (!child.isSelected()) {
                        child.setSelected(true);
                        view.setBackgroundColor(Color.parseColor("#7ecece"));
                        turn = false;

                        if (!participants.contains(members.get(childPosition))) {
                            participants.add(members.get(childPosition));
                        }
                    }

                    if (child.isSelected() && turn == true) {
                        child.setSelected(false);
                        view.setBackgroundColor(Color.TRANSPARENT);

                        if (participants.contains(members.get(childPosition))) {
                            participants.remove(members.get(childPosition));
                        }
                    }

                }
                return false;
            }

        });

        ArrayList<Child> listCategory = new ArrayList();
        ArrayList<Child> listCurrency = new ArrayList();

        listCategory.add(new Child("Transport"));
        listCategory.add(new Child("Food"));
        listCategory.add(new Child("Culture"));

        listCurrency.add(new Child("€"));
        listCurrency.add(new Child("$"));
        listCurrency.add(new Child("§"));

        String defaultCurrency = "€"; // dynamically from (?) shared preferences

        Parent parentCategory = new Parent("Category", listCategory);
        Parent parentCurrency = new Parent("Amount in " + defaultCurrency, listCurrency);

        categories.add(parentCategory);
        currency.add(parentCurrency);

        adapterCategory = new FancyExpandableListAdapter(this, categories);

        categoryView.setAdapter(adapterCategory);

        adapterCurrency = new FancyExpandableListAdapter(this, currency);

        currencyView.setAdapter(adapterCurrency);


        categoryView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                ExpandableListAdapter itemAdapter = parent.getExpandableListAdapter();
                Child child = (Child) itemAdapter.getChild(groupPosition, childPosition);
                String selectedItem = child.getName();
                Parent group = (Parent) itemAdapter.getGroup(groupPosition);
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
                Child child = (Child) itemAdapter.getChild(groupPosition, childPosition);
                String selectedItem = child.getName();
                Parent group = (Parent) itemAdapter.getGroup(groupPosition);
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
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        categoryView.setIndicatorBoundsRelative(categoryView.getRight()-200, categoryView.getWidth());
        currencyView.setIndicatorBoundsRelative(currencyView.getRight()-500, currencyView.getWidth());
        splitView.setIndicatorBoundsRelative(splitView.getRight()-200, splitView.getWidth());

    }

}
