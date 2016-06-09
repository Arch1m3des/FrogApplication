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
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import at.ac.univie.SplitDAO.Friend;
import at.ac.univie.SplitDAO.FriendManager;
import at.ac.univie.adapter.SimpleListAdapter;
import at.ac.univie.frog.R;

public class SplitViewActivity extends AppCompatActivity {

    ListView listView;
    SimpleListAdapter adapter;
    ArrayList<Friend> friends = new ArrayList();
    ArrayList<String> friendsToString = new ArrayList();

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
                Intent goToGroupDetail = new Intent(SplitViewActivity.this, GroupDetailActivity.class);
                double sum = 0;

                for (int i = 0; i < listView.getAdapter().getCount(); i++) {
                    View view = listView.getChildAt(i);
                    EditText editText = (EditText) view.findViewById(R.id.simpleEdit);
                    System.out.println(editText.getText().toString());
                    sum += Double.parseDouble(editText.getText().toString());
                }

                if (sum != getIntent().getIntExtra("amount", 0))
                    Toast.makeText(getApplicationContext(), "Your sum does not add up to " + getIntent().getIntExtra("amount", 0) + ". Please change that.", Toast.LENGTH_LONG).show();
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

        int splitOption = getIntent().getIntExtra("option", 0);
        String option;

        System.out.println(splitOption);

        switch(splitOption) {
            case 1 : option = " manually";
                break;
            case 2 : option = " in parts";
                break;
            case 3 : option = " in percent";
                break;
            default : option = "";
        }

        getSupportActionBar().setTitle("Split" + option);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        listView = (ListView) findViewById(R.id.splitListView);

        FriendManager frienddao = new FriendManager();

        try {
            frienddao.loadFriendData(getApplicationContext(), "Friends");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        friends =  frienddao.getFriendList();

        for (Friend temp : friends)
            friendsToString.add(temp.getName() + " " + temp.getSurname());

        adapter = new SimpleListAdapter(this, R.layout.simple_edit_list, friendsToString);

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                Toast.makeText(getApplicationContext(), "hi", Toast.LENGTH_LONG).show();
            }

        });

    }

}
