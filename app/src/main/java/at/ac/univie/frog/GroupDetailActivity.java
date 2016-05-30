package at.ac.univie.frog;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import at.ac.univie.SplitDAO.Expense;
import at.ac.univie.SplitDAO.Friend;
import at.ac.univie.SplitDAO.FriendManager;
import at.ac.univie.SplitDAO.Group;
import at.ac.univie.SplitDAO.GroupManager;

public class GroupDetailActivity extends AppCompatActivity {

    ListView expenseView;
    ArrayAdapter adapter;
    ArrayList<Friend> expense = new ArrayList();
    ArrayList<String> expenseToString = new ArrayList();
    ArrayList<String> expenseInitials = new ArrayList();
    ArrayList<String> date = new ArrayList();
    ArrayList<String> amount = new ArrayList();
    ArrayList<String> iconColors = new ArrayList();

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_expense);
        getSupportActionBar().setTitle("Expenses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setHomeAsUpIndicator(R.drawable.katze); // if different icon is desired


        TextView group=(TextView) findViewById(R.id.imageGroupsWithText);
        group.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_group_clicked,0,0);
        group.setTextColor(Color.parseColor("#000000"));



        Intent intent = getIntent();
        int position = intent.getIntExtra("GroupPosition", 1);
        GroupManager groupdao = new GroupManager();
        try {
            groupdao.loadGroupData(getApplicationContext(), "Groups");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<Expense> expense = (ArrayList<Expense>) groupdao.getGroupList().get(position).getExpenses();



        /*
        //Wenn der intent ein StringArrayExtra mit dem Namen values hat wird dieser ausgelesen
        //Bevor der Intent ausgelesen werden kann, muss zuerst die Liste der Freunde geladen werden0
        if(intent.getStringArrayExtra("values")!=null){
            String[] newFriend = intent.getStringArrayExtra("values");

            //Einen neuen Freund der Liste hinzufuegen, die Nummer wird durch die Anzahl der Elemente in der Liste +1 festgelegt.
            expense.add(new Friend(expense.size()+1,newFriend[0],newFriend[1],newFriend[2]));
        }
*/

        expenseToString.add("Add Expense");
        expenseInitials.add("+");
        amount.add("");
        date.add("");
        iconColors.add("#6E6E6E");

        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH.mm", Locale.GERMAN);

        for (Expense temp : expense) {
            expenseToString.add(temp.getDescription());
            amount.add(temp.getAmount() + "€");
            date.add(df.format(temp.getDate()));
            expenseInitials.add("$");
            iconColors.add("#00CC7A");
        }

        expenseView = (ListView) findViewById(R.id.expenseView);
        adapter = new FancyListAdapter(this, R.layout.fancy_list, expenseToString, expenseInitials, amount, date, iconColors);

        expenseView.setAdapter(adapter);

        expenseView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {

                if (position == 0) {
                   Intent addFriend = new Intent(GroupDetailActivity.this, AddExpenseActivity.class);
                   startActivity(addFriend);
                }
                else {
                    Intent detailExpense = new Intent(GroupDetailActivity.this, ExpenseDetailActivity.class);
                    startActivity(detailExpense);
                }

            }

        });

    }

    public void gotToFriendsActivity(View v){
        //Do Nothing
    }

    public void goToGroupActivity(View v){
        Intent goToFriends=new Intent(GroupDetailActivity.this,GroupActivity.class);
        startActivity(goToFriends);
    }

    public void goToMap(View v){
        Intent goToMaps = new Intent(GroupDetailActivity.this, MapView.class);
        startActivity(goToMaps);
    }

    public void goToMeActivity(View v){
        Intent goToMe=new Intent(GroupDetailActivity.this,MeActivity.class);
        startActivity(goToMe);
    }

    public void goToSettings(View v){
        Intent goToSettings=new Intent(GroupDetailActivity.this, SettingActivity.class);
        startActivity(goToSettings);
    }

}
