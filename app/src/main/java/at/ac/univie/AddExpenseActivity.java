package at.ac.univie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import at.ac.univie.SplitDAO.*;
import at.ac.univie.adapter.Child;
import at.ac.univie.adapter.FancyExpandableListAdapter;
import at.ac.univie.adapter.Parent;
import at.ac.univie.frog.R;
import at.ac.univie.main.GroupActivity;

public class AddExpenseActivity extends AppCompatActivity implements LocationListener {

    CheckedTextView locationView;
    boolean wantLocation;
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
    int groupindex;
    ArrayList<Friend> members;
    Location location;
    boolean canGetLocation;
    //For Expense needed
    Friend exPayer = null;
    ArrayList<Friend> participants = new ArrayList<>();
    int splitMode = -1;
    String exCategory = "";
    String exCurrency = "";
    boolean doubleBackToExitPressedOnce=false;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_menu_done:

                DecimalFormat doubleform = new DecimalFormat("#.##");
                double amountDbl = 0;
                String exDescription = "";

                try {
                    exDescription = description.getText().toString();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Please add a description", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (exDescription.length() == 0) {
                    Toast.makeText(getApplicationContext(), "This is a really short description", Toast.LENGTH_SHORT).show();
                    return false;
                }

                try {
                    //TODO limt to 2 decimal places
                    try {
                        amountDbl = Double.parseDouble(amount.getText().toString());
                    } catch (NumberFormatException e2) {
                        try {
                            NumberFormat format = NumberFormat.getInstance(Locale.GERMAN);
                            Number number = format.parse(amount.getText().toString());
                        } catch (ParseException e1) {
                            amountDbl = Double.parseDouble(amount.getText().toString());
                        }
                    }


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Please insert an amount.", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (amountDbl == 0) {
                    Toast.makeText(getApplicationContext(), "0 is not much for an expense.", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (exCurrency.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please select a currency.", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (exPayer == null) {
                    Toast.makeText(getApplicationContext(), "Please select a payer.", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (splitMode == -1) {
                    Toast.makeText(getApplicationContext(), "Please select a split mode.", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (exCategory.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please select a category.", Toast.LENGTH_SHORT).show();
                    return false;
                }

                Expense newExpense = null;
                switch (splitMode) {
                    case 0:
                        newExpense = new SplitEqual(exPayer, amountDbl, exDescription, exCategory, splitMode);
                        try {
                            newExpense.calculateDebt();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        newExpense = new SplitManual(exPayer, amountDbl, exDescription, exCategory, splitMode);
                        break;
                    case 2:
                        newExpense = new SplitPercent(exPayer, amountDbl, exDescription, exCategory, splitMode);
                        break;
                    case 3:
                        newExpense = new SplitParts(exPayer, amountDbl, exDescription, exCategory, splitMode);
                        break;
                    default:
                        return false;
                }

                if (newExpense != null) {
                    //add participants
                    for (Friend friend : participants) {
                        newExpense.addParticipant(friend);
                    }

                    //Expenseinfo for Debugging
                    //Toast.makeText(getApplicationContext(), "Expenseinfo " + splitMode + ";" + exPayer + ";" + amountDbl + ";" + exDescription + ";" + exCategory, Toast.LENGTH_SHORT).show();

                    //add currency;
                    if (exCurrency != null)
                        newExpense.setCurrency(exCurrency);

                    //add location as last point
                    Location currLocation = getLocation();
                    if (currLocation != null && wantLocation) {
                        newExpense.setHasLocation(true);
                        newExpense.setLatitude(currLocation.getLatitude());
                        newExpense.setLongitude(currLocation.getLongitude());
                        thisGroup.addPlace(new MapMarker(newExpense.getLatitude(), newExpense.getLongitude(), newExpense.getDescription()));
                    } else {
                        newExpense.setHasLocation(false);
                        if (wantLocation)
                            Toast.makeText(getApplicationContext(), "Your location could not be determined. Please make sure you enabled GPS for this app in your settings.", Toast.LENGTH_LONG).show();
                    }

                    //if Splitequal store in DAO and return to group intent
                    if (splitMode == 0) {

                        //calculate in homecurrency
                        CurrencyManager cm = new CurrencyManager(getApplicationContext());

                        //calculate in homecurrencies
                        newExpense.setSpendingInHomeCurrency(cm.getSpendingInHomeCurrency(newExpense.getSpending(), newExpense.getCurrency()));
                        newExpense.setAmountinHomeCurrency(cm.getAmountinHomeCurrency(newExpense.getAmount(), newExpense.getCurrency()));

                        thisGroup.addExpense(newExpense);
                        int expenseIndex = thisGroup.getExpenses().indexOf(newExpense);

                        //check if it was added to groups
                        if (groups.get(groupindex).getExpenses().contains(newExpense)) {
                            groupDAO.setGroupList(groups);
                            try {
                                groupDAO.saveGroupData(getApplicationContext(), "Groups");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            //Switch Intent
                            Intent goToGroupDetails = new Intent(AddExpenseActivity.this, GroupDetailActivity.class);
                            goToGroupDetails.putExtra("GroupPosition", groupindex);
                            finish();
                            startActivity(goToGroupDetails);
                        }
                    } else {
                        Group newGroup = new Group(0, "DummyGroup");
                        newGroup.addExpense(newExpense);
                        ArrayList<Group> newgroups = new ArrayList<>();
                        newgroups.add(newGroup);
                        groupDAO.setGroupList(newgroups);
                        try {
                            groupDAO.saveGroupData(getApplicationContext(), "newExpense");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Intent goToSplitOptions = new Intent(AddExpenseActivity.this, SplitViewActivity.class);
                        goToSplitOptions.putExtra("option", splitMode);
                        goToSplitOptions.putExtra("newGroupIndex", 0);
                        goToSplitOptions.putExtra("newExpenseindex", 0);
                        goToSplitOptions.putExtra("groupIndex", groupindex);

                        finish();
                        startActivity(goToSplitOptions);
                    }
                    return true;


                }
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

        new CurrencyChanger(getApplicationContext()).execute();


        getSupportActionBar().setTitle("Add Expense");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_button);

        locationView = (CheckedTextView) findViewById(R.id.textLocation);
        description = (TextView) findViewById(R.id.description);
        categoryView = (ExpandableListView) findViewById(R.id.textCategory);
        currencyView = (ExpandableListView) findViewById(R.id.viewCurrency);
        splitView = (ExpandableListView) findViewById(R.id.viewSplitOptions);
        friendsView = (ExpandableListView) findViewById(R.id.viewFriends);
        payerView = (ExpandableListView) findViewById(R.id.viewPayer);
        amount = (EditText) findViewById(R.id.amount);

        Intent intent = getIntent();
        groupindex = intent.getIntExtra("groupIndex", 0);

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

        optionAdapter = new FancyExpandableListAdapter(this, options, false);
        payerAdapter = new FancyExpandableListAdapter(this, payer, false);
        friendsAdapter = new FancyExpandableListAdapter(this, friends, true);

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

                ExpandableListAdapter itemAdapter = parent.getExpandableListAdapter();
                Child child = (Child) itemAdapter.getChild(groupPosition, childPosition);
                String selectedItem = child.getName();
                Parent group = (Parent) itemAdapter.getGroup(groupPosition);
                group.setName(selectedItem);

                splitMode = childPosition;
                //TODO remove toast
                //Toast.makeText(getApplicationContext(), "Mode is " + splitMode, Toast.LENGTH_SHORT).show();


                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                }

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

                ExpandableListAdapter itemAdapter = parent.getExpandableListAdapter();
                Parent group = (Parent) itemAdapter.getGroup(groupPosition);
                group.setName("Who participated?" + " (" + participants.size() + ")");

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

                exPayer = thisGroup.getMembers().get(childPosition);

                ExpandableListAdapter itemAdapter = parent.getExpandableListAdapter();
                Child child = (Child) itemAdapter.getChild(groupPosition, childPosition);
                String selectedItem = child.getName();
                Parent group = (Parent) itemAdapter.getGroup(groupPosition);
                group.setName(selectedItem + " paid");
                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                }

                return false;
            }
        });


        ArrayList<Child> listCategory = new ArrayList();
        ArrayList<Child> listCurrency = new ArrayList();


        listCategory.add(new Child("Misc"));
        listCategory.add(new Child("Food & Drinks"));
        listCategory.add(new Child("Fun"));
        listCategory.add(new Child("Medical"));
        listCategory.add(new Child("Money Transfer"));


        Currencies curr = new Currencies();
        for (String currency : thisGroup.getCurrencies()) {
            listCurrency.add(new Child(currency + " " + curr.getCurrencies().get(currency)));
        }

        String defaultCurrency =  curr.getCurrencies().get(thisGroup.getCurrencies().get(0));
        exCurrency = thisGroup.getCurrencies().get(0);

        Parent parentCategory = new Parent("Category: ", listCategory);
        Parent parentCurrency = new Parent("Amount in " + defaultCurrency, listCurrency);

        categories.add(parentCategory);
        currency.add(parentCurrency);

        adapterCategory = new FancyExpandableListAdapter(this, categories, false);

        categoryView.setAdapter(adapterCategory);

        adapterCurrency = new FancyExpandableListAdapter(this, currency, false);

        currencyView.setAdapter(adapterCurrency);


        categoryView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                ExpandableListAdapter itemAdapter = parent.getExpandableListAdapter();
                Child child = (Child) itemAdapter.getChild(groupPosition, childPosition);
                String selectedItem = child.getName();
                Parent group = (Parent) itemAdapter.getGroup(groupPosition);
                group.setName("Category: " + selectedItem);

                exCategory = selectedItem;

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

                exCurrency = selectedItem.substring(0,3);

                String sub =  selectedItem.substring(4,5);
                group.setName("Amount in " + selectedItem.substring(4,5));
                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                }

                return true;

            }
        });



        locationView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (locationView.isChecked()) {
                    locationView.setChecked(false);
                    wantLocation = false;
                }
                else {
                    locationView.setChecked(true);
                    wantLocation = true;
                    getLocation();
                    if (getLocation() != null) {
                        //Toast.makeText(getApplicationContext(), "location is " + getLocation(), Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Your location could not be determined. Please make sure you enabled GPS for this app in your settings.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    /* http://www.androidhive.info/2012/07/android-gps-location-manager-tutorial/ */

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


    /*only way to avoid keyboard pop up*/ //http://stackoverflow.com/questions/4828636/edittext-clear-focus-on-touch-outside/28939113#28939113

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

}
