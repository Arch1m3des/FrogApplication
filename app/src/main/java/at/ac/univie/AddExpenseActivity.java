package at.ac.univie;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import at.ac.univie.SplitDAO.*;
import at.ac.univie.adapter.Child;
import at.ac.univie.adapter.FancyExpandableListAdapter;
import at.ac.univie.adapter.Parent;
import at.ac.univie.frog.R;

public class AddExpenseActivity extends AppCompatActivity implements LocationListener {

    CheckedTextView locationView;
    EditText amount;
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
    at.ac.univie.SplitDAO.Group thisGroup;
    Button button;
    int groupindex;
    ArrayList<Friend> participants = new ArrayList<>();
    ArrayList<Friend> members;
    Location location;
    boolean canGetLocation;

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
                int amountInt = Integer.parseInt(amount.getText().toString());
                Intent goToSplitOptions = new Intent(AddExpenseActivity.this, SplitViewActivity.class);
                goToSplitOptions.putExtra("option", 1);
                Expense expense = new SplitManual(members.get(0), members.get(0), amountInt, "Test", "Food", 1);
                thisGroup.addExpense(expense);
                System.out.println("just added expense: " + expense);
                goToSplitOptions.putExtra("amount", amountInt);
                finish();
                startActivity(goToSplitOptions);
                return true;
            default: return  false;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_add_expense);

        getSupportActionBar().setTitle("Add Expense");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_button);

        locationView = (CheckedTextView) findViewById(R.id.textLocation);
        description = (TextView) findViewById(R.id.viewDescription);
        categoryView = (ExpandableListView) findViewById(R.id.textCategory);
        currencyView = (ExpandableListView) findViewById(R.id.viewCurrency);
        splitView = (ExpandableListView) findViewById(R.id.viewSplitOptions);
        friendsView = (ExpandableListView) findViewById(R.id.viewFriends);
        payerView = (ExpandableListView) findViewById(R.id.viewPayer);
        amount = (EditText) findViewById(R.id.amount);

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
        thisGroup = groups.get(groupindex);

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

        /* Single-Choice fuer List */
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



        locationView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (locationView.isChecked())
                    locationView.setChecked(false);
                else {
                    locationView.setChecked(true);
                    getLocation();
                    if (getLocation() != null) {
                        Toast.makeText(getApplicationContext(), "location is " + getLocation(), Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Your location could not be determined. Please make sure you enabled GPS for this app in your settings.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public Location getLocation() {

        location = null;
        final float minDistance = 10;
        final long minTime = 35000;

        try {
            LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

            // getting GPS status
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    try {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, this);
                        if (locationManager != null) {

                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        }
                    }
                    catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        try {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);
                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {

                                }
                            }
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
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
