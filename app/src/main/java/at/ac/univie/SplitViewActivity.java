package at.ac.univie;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import at.ac.univie.SplitDAO.Currencies;
import at.ac.univie.SplitDAO.CurrencyManager;
import at.ac.univie.SplitDAO.Expense;
import at.ac.univie.SplitDAO.Friend;
import at.ac.univie.SplitDAO.FriendManager;
import at.ac.univie.SplitDAO.GroupManager;
import at.ac.univie.SplitDAO.MapMarker;
import at.ac.univie.adapter.SimpleListAdapter;
import at.ac.univie.frog.R;

public class SplitViewActivity extends AppCompatActivity {

    ListView listView;
    SimpleListAdapter adapter;
    ArrayList<Friend> friends = new ArrayList();
    ArrayList<String> friendsToString = new ArrayList();
    boolean location = false;
    GroupManager groupDAO;
    ArrayList<at.ac.univie.SplitDAO.Group> groups = new ArrayList();
    at.ac.univie.SplitDAO.Group thisGroup;
    Expense thisExpense;
    TextView totalAmt;
    CheckedTextView optimize;
    String splitSign;
    int splitOption;
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
                int position = getIntent().getIntExtra("groupIndex", 0);
                System.out.println("position: " + position);
                Intent goToGroupDetail = new Intent(SplitViewActivity.this, GroupDetailActivity.class);
                goToGroupDetail.putExtra("GroupPosition", position);

                Intent intent = getIntent();
                int groupindex = intent.getIntExtra("groupIndex", 0);

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


                double sum = 0;

                for (int i = 0; i < listView.getAdapter().getCount(); i++) {
                    View view = listView.getChildAt(i);
                    EditText editText = (EditText) view.findViewById(R.id.simpleEdit);
                    System.out.println(editText.getText().toString());
                    double input = 0;
                    try {
                        input= Double.parseDouble(editText.getText().toString());
                        thisExpense.setitem(thisExpense.getParticipants().get(i), input);
                    } catch (NumberFormatException e1) {
                        input = Double.parseDouble(editText.getHint().toString());
                        thisExpense.setitem(thisExpense.getParticipants().get(i), input);
                    }
                    sum += input;
                }


                //optimize makes splitting a piece of cake
                if (optimize.isChecked()) {
                    thisExpense.optimizeinputs();
                    sum = thisExpense.sumitems();
                }

                DecimalFormat doubleform = new DecimalFormat("#.##");
                totalAmt.setText("Total: " + doubleform.format(sum) + "/" + thisExpense.getAmount());
                boolean passed = false;

                switch (splitOption) {
                    case 1: //SplitManual
                        if (thisExpense.getAmount() > sum+1 || thisExpense.getAmount() < sum -1) {
                            Toast.makeText(getApplicationContext(), "Your sum (" + sum + ") does not add up to " + thisExpense.getAmount() + ". Please change that.", Toast.LENGTH_LONG).show();
                            totalAmt.setText("Total: " + doubleform.format(sum) + "/" + doubleform.format(thisExpense.getAmount()));
                            return false;
                        }
                        else {
                            passed = true;
                        }
                        break;
                    case 2: //Splitpercent
                        if (!(sum<100.01 && sum>99.9)) {
                            Toast.makeText(getApplicationContext(), "Your percent (" + sum + ") do not add up to 100%. Please change that.", Toast.LENGTH_LONG).show();
                            totalAmt.setText("Total: " + doubleform.format(thisExpense.sumitems()) + "/100%");
                            return false;
                        }
                        else {
                            passed = true;
                        }
                        break;
                    case 3: //SplitParts
                        if (thisExpense.getinput().containsValue(0.0)) {
                            Toast.makeText(getApplicationContext(), "There are still members with 0 parts.", Toast.LENGTH_LONG).show();
                            totalAmt.setText("Total: " + doubleform.format(thisExpense.sumitems()) +" Parts");
                            return false;
                        }
                        else {
                            passed = true;
                        }
                        break;
                    default: return false;
                }


                if (passed) {
                    //calculate in homecurrency
                    CurrencyManager cm = new CurrencyManager(getApplicationContext());
                    thisExpense.setSpendingInHomeCurrency(cm.getSpendingInHomeCurrency(thisExpense.getSpending(), thisExpense.getCurrency()));
                    thisExpense.setAmountinHomeCurrency(cm.getAmountinHomeCurrency(thisExpense.getAmount(), thisExpense.getCurrency()));

                    thisGroup.addExpense(thisExpense);
                    if (getIntent().getStringExtra("location").equals("yes"))
                        thisGroup.addPlace(new MapMarker(thisExpense.getLatitude(), thisExpense.getLongitude(), thisExpense.getDescription()));

                    groupDAO.setGroupList(groups);
                    try {
                        groupDAO.saveGroupData(getApplicationContext(), "Groups");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    finish();
                    startActivity(goToGroupDetail);
                    return true;
                }
            default: return  false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_split_view);
        totalAmt = (TextView) findViewById(R.id.totalAmt);
        optimize = (CheckedTextView) findViewById(R.id.optimize);

        int groupindex = getIntent().getIntExtra("newGroupIndex", 0);
        int expenseindex = getIntent().getIntExtra("newExpenseindex", 0);
        splitOption = getIntent().getIntExtra("option", -1);
        String option;

        //getGroupInfo
        groupDAO = new GroupManager();

        try {
            groupDAO.loadGroupData(getApplicationContext(), "newExpense");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        groups =  groupDAO.getGroupList();
        thisGroup = groups.get(groupindex);
        thisExpense = thisGroup.getExpenses().get(expenseindex);
        //Toast.makeText(getApplicationContext(), "Expense" + thisExpense.toString(), Toast.LENGTH_SHORT).show();



        Currencies currList = new Currencies();
        DecimalFormat doubleform = new DecimalFormat("#.##");

        switch(splitOption) {
            case 1 : option = " manually";
                splitSign = currList.getCurrencies().get(thisExpense.getCurrency());
                totalAmt.setText("Total: " + "0.00/" + doubleform.format(thisExpense.getAmount()) + splitSign);
                break;
            case 2 : option = " in percent";
                splitSign = "%";
                totalAmt.setText("Total: " + "0/100%");
                break;
            case 3 : option = " in parts";
                splitSign = "Pts.";
                totalAmt.setText("Total: " + "0 Parts");

                break;
            default : option = "";
        }

        getSupportActionBar().setTitle("Split" + option);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        listView = (ListView) findViewById(R.id.splitListView);

        friends = (ArrayList<Friend>) thisExpense.getParticipants();

        for (Friend temp : friends)
            friendsToString.add(temp.getName() + " " + temp.getSurname());



        adapter = new SimpleListAdapter(this, R.layout.simple_edit_list, friendsToString, splitSign);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                Toast.makeText(getApplicationContext(), "hi", Toast.LENGTH_LONG).show();
            }

        });

        optimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optimize.isChecked())
                    optimize.setChecked(false);
                else {
                    optimize.setChecked(true);
                }
            }
        });

    }

}
