package at.ac.univie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import at.ac.univie.SplitDAO.Currencies;
import at.ac.univie.SplitDAO.Expense;
import at.ac.univie.SplitDAO.Friend;
import at.ac.univie.SplitDAO.GroupManager;
import at.ac.univie.adapter.FancyListAdapter;
import at.ac.univie.frog.R;
import at.ac.univie.main.FriendActivity;
import at.ac.univie.main.GroupActivity;
import at.ac.univie.main.MapView;
import at.ac.univie.main.MeActivity;
import at.ac.univie.main.SettingActivity;

public class GroupDetailActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String gruppenname;

    ListView expenseView;
    ArrayAdapter adapter;
    TextView noExpenses;
    ArrayList<String> expenseToString = new ArrayList();
    ArrayList<String> expenseInitials = new ArrayList();
    ArrayList<String> date = new ArrayList();
    ArrayList<String> amount = new ArrayList();
    ArrayList<String> iconColors = new ArrayList();
    int groupindex = 0;
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
        getMenuInflater().inflate(R.menu.group_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_add:
                Intent addExpense = new Intent(GroupDetailActivity.this, AddExpenseActivity.class);
                addExpense.putExtra("groupIndex",groupindex);
                finish();
                startActivity(addExpense);
                return true;
            case R.id.action_menu_settings:
                Intent goToSettings=new Intent(GroupDetailActivity.this, GroupSettingsActivity.class);
                goToSettings.putExtra("groupIndex", groupindex);
                startActivity(goToSettings);
            default: return  false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_expense);
        getSupportActionBar().setTitle("Expenses");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setHomeAsUpIndicator(R.drawable.katze); // if different icon is desired


        TextView group=(TextView) findViewById(R.id.imageGroupsWithText);
        group.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_group_clicked,0,0);
        group.setTextColor(Color.parseColor("#000000"));

        noExpenses = (TextView) findViewById(R.id.textView);

        Intent intent = getIntent();
        groupindex = intent.getIntExtra("GroupPosition", 0);
        GroupManager groupdao = new GroupManager();
        try {
            groupdao.loadGroupData(getApplicationContext(), "Groups");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<Expense> expense = (ArrayList<Expense>) groupdao.getGroupList().get(groupindex).getExpenses();
        Collections.reverse(expense);
        if (expense.isEmpty())
            noExpenses.setVisibility(View.VISIBLE);

        gruppenname=groupdao.getGroupList().get(groupindex).getName();

        sharedPreferences = getSharedPreferences("Log", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("Groupname", gruppenname);
        editor.commit();


        /*
        //Wenn der intent ein StringArrayExtra mit dem Namen values hat wird dieser ausgelesen
        //Bevor der Intent ausgelesen werden kann, muss zuerst die Liste der Freunde geladen werden0
        if(intent.getStringArrayExtra("values")!=null){
            String[] newFriend = intent.getStringArrayExtra("values");

            //Einen neuen Freund der Liste hinzufuegen, die Nummer wird durch die Anzahl der Elemente in der Liste +1 festgelegt.
            expense.add(new Friend(expense.size()+1,newFriend[0],newFriend[1],newFriend[2]));
        }
*/

        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yy", Locale.GERMAN);
        DecimalFormat doubleform = new DecimalFormat("#.##");


        Currencies curr = new Currencies();
        for (Expense temp : expense) {
            expenseToString.add(temp.getDescription());
            amount.add(doubleform.format(temp.getAmountInHomeCurrency()) + "€");
            date.add(df.format(temp.getDate()));
            expenseInitials.add(curr.getCurrencies().get(temp.getCurrency()));
            iconColors.add("#00CC7A");
        }

        expenseView = (ListView) findViewById(R.id.expenseView);
        adapter = new FancyListAdapter(this, R.layout.fancy_list, expenseToString, expenseInitials, amount, date, iconColors);

        expenseView.setAdapter(adapter);

        expenseView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                Intent detailExpense = new Intent(GroupDetailActivity.this, ExpenseDetailActivity.class);
                detailExpense.putExtra("groupindex",groupindex);
                detailExpense.putExtra("expenseindex", position);
                startActivity(detailExpense);
            }

        });

    }

    public void gotToFriendsActivity(View v){
        Intent goToFriends=new Intent(GroupDetailActivity.this,FriendActivity.class);
        startActivity(goToFriends);
    }

    public void goToGroupActivity(View v){
        Intent goToFriends=new Intent(GroupDetailActivity.this,GroupActivity.class);
        startActivity(goToFriends);
    }

    public void goToMap(View v){
        Intent goToMaps = new Intent(GroupDetailActivity.this, MapView.class);
        startActivity(goToMaps);
    }

    public void goToSettings(View v){
        Intent goToSettings=new Intent(GroupDetailActivity.this, SettingActivity.class);
        startActivity(goToSettings);
    }

}
