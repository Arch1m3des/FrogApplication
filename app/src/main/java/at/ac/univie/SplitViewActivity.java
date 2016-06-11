package at.ac.univie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import at.ac.univie.SplitDAO.Currencies;
import at.ac.univie.SplitDAO.Expense;
import at.ac.univie.SplitDAO.Friend;
import at.ac.univie.SplitDAO.FriendManager;
import at.ac.univie.SplitDAO.GroupManager;
import at.ac.univie.adapter.SimpleListAdapter;
import at.ac.univie.frog.R;

public class SplitViewActivity extends AppCompatActivity {

    ListView listView;
    SimpleListAdapter adapter;
    ArrayList<Friend> friends = new ArrayList();
    ArrayList<String> friendsToString = new ArrayList();
    GroupManager groupDAO;
    ArrayList<at.ac.univie.SplitDAO.Group> groups = new ArrayList();
    at.ac.univie.SplitDAO.Group thisGroup;
    Expense thisExpense;
    TextView totalAmt;
    String splitSign;
    int splitOption;

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

                if (splitOption == 1) {

                }
                //Splitmanual
                double sum = 0;

                for (int i = 0; i < listView.getAdapter().getCount(); i++) {
                    View view = listView.getChildAt(i);
                    EditText editText = (EditText) view.findViewById(R.id.simpleEdit);
                    System.out.println(editText.getText().toString());
                    thisExpense.setitem(thisExpense.getParticipants().get(i), Double.parseDouble(editText.getText().toString()));
                    sum += Double.parseDouble(editText.getText().toString());
                }

                boolean optimize = true;
                if (optimize) {
                    //need to calc the sum again
                    sum = 0;
                    thisExpense.optimizeinputs();
                    for (int i = 0; i < listView.getAdapter().getCount(); i++) {
                        View view = listView.getChildAt(i);
                        EditText editText = (EditText) view.findViewById(R.id.simpleEdit);
                        sum += Double.parseDouble(editText.getText().toString());
                    }
                    System.out.print(sum);
                }

                DecimalFormat doubleform = new DecimalFormat("#.##");
                totalAmt.setText("Total: " + doubleform.format(sum) + "/" + thisExpense.getAmount());

                if (!((sum+1 > thisExpense.getAmount()) && (sum-1 < thisExpense.getAmount())))
                    Toast.makeText(getApplicationContext(), "Your sum (" + sum + ") does not add up to " + thisExpense.getAmount() + ". Please change that.", Toast.LENGTH_LONG).show();
                else {
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

        int groupindex = getIntent().getIntExtra("groupIndex", 0);
        int expenseindex = getIntent().getIntExtra("expenseindex", 0);
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
        Toast.makeText(getApplicationContext(), "Expense" + thisExpense.toString(), Toast.LENGTH_SHORT).show();


        System.out.println(splitOption);

        Currencies currList = new Currencies();

        switch(splitOption) {
            case 1 : option = " manually";
                splitSign = currList.getCurrencies().get(thisExpense.getCurrency());
                break;
            case 2 : option = " in parts";
                splitSign = "Pts.";
                break;
            case 3 : option = " in percent";
                splitSign = "%";
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

    }

}
