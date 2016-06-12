package at.ac.univie;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import at.ac.univie.SplitDAO.*;
import at.ac.univie.SplitDAO.Group;
import at.ac.univie.adapter.FancyListAdapter;
import at.ac.univie.frog.R;
import at.ac.univie.main.FriendActivity;
import at.ac.univie.main.GroupActivity;
import at.ac.univie.main.MapView;
import at.ac.univie.main.MeActivity;
import at.ac.univie.main.SettingActivity;

/**
 * Created by Tamara on 13.05.16.
 */

public class FriendDetailActivity extends AppCompatActivity {

    ListView groupView;
    ListAdapter adapter;
    TextView name;
    Button btnExportList;
    ArrayList<Group> groups = new ArrayList();
    ArrayList<String> groupsToString = new ArrayList();
    ArrayList<String> iconColors = new ArrayList();
    ArrayList<String> text = new ArrayList();
    ArrayList<String> date = new ArrayList();
    ArrayList<String> amount = new ArrayList();
    Friend me;
    Friend owingfriend;
    double balance =  0;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_friend_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Friend Details");
        getSupportActionBar().setElevation(0);

        TextView group=(TextView) findViewById(R.id.imageFriendsWithText);
        TextView friendBalance = (TextView) findViewById(R.id.textBalance);
        group.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_friends_clicked,0,0);
        group.setTextColor(Color.parseColor("#000000"));

        GroupManager groupdao = new GroupManager();
        FriendManager frienddao =  new FriendManager();
        name = (TextView) findViewById(R.id.name);
        btnExportList = (Button) findViewById(R.id.export);

        Intent intent = getIntent();
        int friendposition = intent.getIntExtra("friendposition",0);


        try {
            groupdao.loadGroupData(getApplicationContext(), "Groups");
            frienddao.loadFriendData(getApplicationContext(), "Friends");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        me = frienddao.getFriendList().get(0);
        owingfriend = frienddao.getFriendList().get(friendposition);
        groups = groupdao.getGroupList();

        String nameString = owingfriend.getName() + " " + owingfriend.getSurname(); //intent.getStringExtra("name");
        name.setText(nameString);


        DecimalFormat doubleform = new DecimalFormat("#.##");
        SimpleDateFormat dateform = new SimpleDateFormat("dd.MM.", Locale.GERMAN);
        Date today =  new Date();

        for (Group temp : groups) {

            ArrayList<Friend> members = (ArrayList<Friend>) temp.getMembers();
            boolean contains = false;
            for (Friend friend : members) {
                if(friend.getFriendID() == owingfriend.getFriendID()) {
                    contains = true;
                    break;
                }
            }

            if(contains) {
                double owes = temp.calculateowes(me, owingfriend);
                balance += owes;
                if(owes==0) continue;
                else {
                    groupsToString.add(temp.getName());
                    String plus = (owes>0) ? "+" : "";
                    amount.add(plus + doubleform.format(owes) + "€");
                    date.add((owes>0) ? (owingfriend.getName() + " owes you") : ("You owe " + owingfriend.getName()));
                    iconColors.add(temp.getIconColor());
                    text.add("");
                }
            }
        }

        //set Balance Text
        friendBalance.setText("Summary: " + ((balance>0) ? (owingfriend.getName() + " owes you ") : ("You owe " + owingfriend.getName() + " ")) + doubleform.format(balance) + "€");

        groupView = (ListView) findViewById(R.id.groupView);
        adapter = new FancyListAdapter(this, R.layout.fancy_list, groupsToString, text, amount, date, iconColors);

        groupView.setAdapter(adapter);

        btnExportList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, owingfriend.getMailaddress());
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_BCC, owingfriend.getMailaddress());
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Frog - Debt report for " + me.getName() + " to " + owingfriend.getName()+ "!");
                emailIntent.putExtra(Intent.EXTRA_TEXT   , "Hi, " + owingfriend.getName() + "!\n" + "Attached a summary of our trip expenses.\n" + ("Summary: " + ((balance<0) ? (me.getName() + " owes you ") : ("You owe " + me.getName() + " ")) + balance + "€"));


                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    finish();
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(FriendDetailActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void gotToFriendsActivity(View v){
        Intent goToFriends=new Intent(FriendDetailActivity.this,FriendActivity.class);
        startActivity(goToFriends);
    }

    public void goToGroupActivity(View v){
        Intent goToFriends=new Intent(FriendDetailActivity.this,GroupActivity.class);
        startActivity(goToFriends);
    }

    public void goToMap(View v){
        Intent goToMaps = new Intent(FriendDetailActivity.this, MapView.class);
        startActivity(goToMaps);
    }

    public void goToSettings(View v){
        Intent goToSettings=new Intent(FriendDetailActivity.this, SettingActivity.class);
        startActivity(goToSettings);
    }
}