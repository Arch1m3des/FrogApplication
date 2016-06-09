package at.ac.univie;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import at.ac.univie.SplitDAO.*;
import at.ac.univie.adapter.Child;
import at.ac.univie.adapter.FancyExpandableListAdapter;
import at.ac.univie.adapter.Parent;
import at.ac.univie.frog.R;

public class AddExpenseActivity extends AppCompatActivity {

    CheckedTextView location;
    TextView description;
    ExpandableListView categoryView, currencyView, splitView, friendsView, payerView;
    ExpandableListAdapter adapterCategory, adapterCurrency, optionAdapter, friendsAdapter, payerAdapter;
    ArrayList<Parent> categories = new ArrayList();
    ArrayList<Parent> currency = new ArrayList();
    ArrayList<at.ac.univie.SplitDAO.Group> groups = new ArrayList();
    ArrayList<Child> friendsToString = new ArrayList();
    ArrayList<Parent> options = new ArrayList();
    ArrayList<Parent> payer = new ArrayList();
    ArrayList<Parent> friends = new ArrayList();
    ArrayList<Child> splitOptions = new ArrayList();
    GroupManager groupDAO;
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
        description = (TextView) findViewById(R.id.viewDescription);
        categoryView = (ExpandableListView) findViewById(R.id.textCategory);
        currencyView = (ExpandableListView) findViewById(R.id.viewCurrency);
        splitView = (ExpandableListView) findViewById(R.id.viewSplitOptions);
        friendsView = (ExpandableListView) findViewById(R.id.viewFriends);
        payerView = (ExpandableListView) findViewById(R.id.viewPayer);

        Intent intent = getIntent();
        groupindex = intent.getIntExtra("groupindex", 0);

        groupDAO = new GroupManager();

        try {
            groupDAO.loadGroupData(getApplicationContext(), "Groups");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        groups =  groupDAO.getGroupList();
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

        friends.add(parentFriends);
        payer.add(parentPayer);
        options.add(parentOption);

        optionAdapter = new FancyExpandableListAdapter(this, options);
        friendsAdapter = new FancyExpandableListAdapter(this, friends);
        payerAdapter = new FancyExpandableListAdapter(this, payer);
        button = (Button) findViewById(R.id.button);

        splitView.setAdapter(optionAdapter);
        friendsView.setAdapter(friendsAdapter);
        payerView.setAdapter(payerAdapter);



        int i = 0;
        for (Parent group : options) {
            for (Child child : group.getItems()) {
                child.setId(i++);
                System.out.println("Child with id = " + child.getId() + " is " + child.getName());
            }
        }

        splitView.setChoiceMode(ExpandableListView.CHOICE_MODE_SINGLE);

        splitView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            View currentView;

            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {

                view.setSelected(true);
                if (currentView != null) {
                    currentView.setBackgroundColor(Color.TRANSPARENT);
                }
                currentView = view;
                currentView.setBackgroundColor(Color.parseColor("#7ecece"));

                return false;
            }
        });

        friendsView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            boolean turn = true;

            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {

                ExpandableListAdapter participationAdapter = parent.getExpandableListAdapter();
                Child child = (Child) participationAdapter.getChild(groupPosition, childPosition);

                turn = true;

                System.out.println("Child id clicked is " + child.getId() + " and group position is " + groupPosition);

                if (!child.isSelected()) {
                    child.setSelected(true);
                    parent.getExpandableListAdapter().getChildView(groupPosition, childPosition, false, view, (ViewGroup) view.getParent()).setBackgroundColor(Color.parseColor("#7ecece"));
                    if (view.isSelected()) {
                        System.out.println("view 0 is selected");
                    }
                    else
                        view.setSelected(true);
                    System.out.println("view 0 is " + view);
                    System.out.println("setting background color of child " + child.getId() + " at group position " + groupPosition);
                    turn = false;

                    if (!participants.contains(members.get(childPosition))) {
                        participants.add(members.get(childPosition));
                    }
                }

                if (child.isSelected() && turn) {
                    child.setSelected(false);
                    parent.getExpandableListAdapter().getChildView(groupPosition, childPosition, false, view, (ViewGroup) view.getParent()).setBackgroundColor(Color.TRANSPARENT);

                    if (participants.contains(members.get(childPosition))) {
                        participants.remove(members.get(childPosition));
                    }
                }
                return false;
            }
        });

        payerView.setChoiceMode(ExpandableListView.CHOICE_MODE_SINGLE);

        payerView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            View currentView;

            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {

                view.setSelected(true);
                if (currentView != null) {
                    currentView.setBackgroundColor(Color.TRANSPARENT);
                }
                currentView = view;
                currentView.setBackgroundColor(Color.parseColor("#7ecece"));

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
        friendsView.setIndicatorBoundsRelative(splitView.getRight()-200, splitView.getWidth());
        payerView.setIndicatorBoundsRelative(splitView.getRight()-200, splitView.getWidth());

    }

}
