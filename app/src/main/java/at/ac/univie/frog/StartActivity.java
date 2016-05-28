package at.ac.univie.frog;

/**
 * Created by Tamara on 15.05.16.
 */

// name SplitIt? TravelSplit?

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import at.ac.univie.SplitDAO.CurrencyChanger;
import at.ac.univie.SplitDAO.CurrencyManager;
import at.ac.univie.SplitDAO.Expense;
import at.ac.univie.SplitDAO.Friend;
import at.ac.univie.SplitDAO.FriendManager;
import at.ac.univie.SplitDAO.Group;
import at.ac.univie.SplitDAO.GroupManager;
import at.ac.univie.SplitDAO.SplitEqual;
import at.ac.univie.SplitDAO.SplitParts;

public class StartActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    EditText email, surname, name;
    SharedPreferences.Editor editor;
    Button button;

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getApplicationContext().getSharedPreferences("Log", 0);

        new CurrencyChanger(StartActivity.this).execute();


        //Add Friends & Groups Hardcoded and save them
        FriendManager frienddao =  new FriendManager();

        Friend f1 =  new Friend(1,"ICH", "HCI", "ich@chsdfsdl.at");
        Friend f2 =  new Friend(2,"Weinbahn", "Andy", "arch1m3des1988@gmail.com");
        Friend f3 =  new Friend(3,"Druggs", "Tamara", "tammyd@googlemail.com");
        Friend f4 =  new Friend(4,"Bubly", "Daniel", "ich@du.com");
        Friend f5 =  new Friend(5,"Duda", "Samuel", "sammy@deluxe.com");
        Friend f6 =  new Friend(6,"Bada", "Margus", "ich@du.com");

        frienddao.addFriend(f1);
        frienddao.addFriend(f2);
        frienddao.addFriend(f3);
        frienddao.addFriend(f4);
        frienddao.addFriend(f6);
        frienddao.addFriend(f5);

        try {
            frienddao.saveFriendData(getApplicationContext(), "Friends");
        } catch (IOException e) {
            e.printStackTrace();
        }

//
        //Add Groups
        GroupManager groupdao =  new GroupManager();

        Group g1 =  new Group(1, "Thailand");
        Group g2 =  new Group(2, "London");
        Group g3 =  new Group(3, "Spa Weekend");
        Group g4 =  new Group(4, "Uruguay");

        //add members

        g1.addMember(f1);
        g1.addMember(f2);
        g1.addMember(f3);
        g1.addMember(f4);

        g2.addMember(f1);
        g2.addMember(f4);
        g2.addMember(f5);
        g2.addMember(f6);

        g3.addMember(f1);
        g3.addMember(f3);
        g3.addMember(f6);

        g4.addMember(f3);
        g4.addMember(f2);

        //add expenses
        Expense firstexpense = new SplitEqual(g1.getMembers().get(0) ,g1.getMembers().get(0), 350, "Straßenessen");
        firstexpense.addparticipant(f1);
        firstexpense.addparticipant(f2);
        firstexpense.addparticipant(f3);
        firstexpense.addparticipant(f4);

        g1.addExpense(firstexpense);

        firstexpense = new SplitParts(f1 , f3 , 34, "Eis essen Hotel-Restaurant");
        firstexpense.addparticipant(f3);
        firstexpense.addparticipant(f6);
        firstexpense.setitem(f3, 4);
        firstexpense.setitem(f6, 1);
        try {
            firstexpense.calculatedebt();
            //firstexpense.calculateDebtInHomeSpendings();
        } catch (Exception e) {
            e.printStackTrace();
        }

        g2.addExpense(firstexpense);

        groupdao.addGroup(g1);
        groupdao.addGroup(g2);
        groupdao.addGroup(g3);
        groupdao.addGroup(g4);

        try {
            groupdao.saveGroupData(getApplicationContext(), "Groups");
        } catch (IOException e) {
            e.printStackTrace();
        }


        sharedPreferences = getSharedPreferences("Log", Context.MODE_PRIVATE);

        /*
        String me_name = "";
        String me_surname = "";
        String me_email = "";
        if (sharedPreferences.contains("Name")) {
            me_name = sharedPreferences.getString("Name", "");
        }

        if (sharedPreferences.contains("Surname")) {
            me_surname = sharedPreferences.getString("Surname", "");
        }

        if (sharedPreferences.contains("Email")) {
            me_email = sharedPreferences.getString("Email", "");
        }*/


        /*

        CurrencyManager crm =  new CurrencyManager();
        try {
            crm.loadCurrencyData(StartActivity.this, "Currency");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        */



        if (sharedPreferences.contains("Name")) {
            Intent goToOverview = new Intent(StartActivity.this, GroupActivity.class);
            startActivity(goToOverview);
        }else {

            setContentView(R.layout.content_start);
            getSupportActionBar().setTitle("Join us");

            button = (Button) findViewById(R.id.toOverview);
            email = (EditText) findViewById(R.id.editEmail);
            name = (EditText) findViewById(R.id.editName);
            surname = (EditText) findViewById(R.id.editSurname);

            button.setTransformationMethod(null); // to decapitalize the button text

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // persistently save user information with SharedPreferences
                    editor = sharedPreferences.edit();

                    final String emailToString = email.getText().toString();
                    final String nameToString = name.getText().toString();
                    final String surnameToString = surname.getText().toString();

                    if (emailToString.length() == 0)
                        Toast.makeText(getApplicationContext(), "Please enter your email address.", Toast.LENGTH_LONG).show();
                    else if (nameToString.length() == 0)
                        Toast.makeText(getApplicationContext(), "Please enter your name.", Toast.LENGTH_LONG).show();
                    else if (surnameToString.length() == 0)
                        Toast.makeText(getApplicationContext(), "Please enter your surname.", Toast.LENGTH_LONG).show();

                    else {

                        editor.putString("Email", emailToString); // use Mailgun email verification!
                        editor.putString("Name", nameToString);
                        editor.putString("Surname", surnameToString);
                        editor.commit();

                        /*FriendManager frienddao =  new FriendManager();

                        Friend f1 =  new Friend(1,surnameToString, nameToString, emailToString);
                        Friend f2 =  new Friend(2,"Weinbahn", "Andy", "arch1m3des1988@gmail.com");
                        Friend f3 =  new Friend(3,"Druggs", "Tamara", "tammyd@googlemail.com");
                        Friend f4 =  new Friend(4,"Bubly", "Daniel", "ich@du.com");
                        Friend f5 =  new Friend(5,"Duda", "Samuel", "sammy@deluxe.com");
                        Friend f6 =  new Friend(6,"Bada", "Margus", "ich@du.com");

                        frienddao.addFriend(f1);
                        frienddao.addFriend(f2);
                        frienddao.addFriend(f3);
                        frienddao.addFriend(f4);
                        frienddao.addFriend(f6);
                        frienddao.addFriend(f5);


                        //saving friends
                        try {
                            frienddao.saveFriendData(StartActivity.this, "Friends");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //Add Groups
                        GroupManager groupdao =  new GroupManager();

                        Group g1 =  new Group(1, "Thailand");
                        Group g2 =  new Group(2, "London");
                        Group g3 =  new Group(3, "Spa Weekend");
                        Group g4 =  new Group(4, "Uruguay");

                        //add members

                        g1.addMember(f1);
                        g1.addMember(f2);
                        g1.addMember(f3);
                        g1.addMember(f4);

                        g2.addMember(f4);
                        g2.addMember(f5);
                        g2.addMember(f6);

                        g3.addMember(f3);
                        g3.addMember(f6);

                        g4.addMember(f3);
                        g4.addMember(f2);

                        //add expenses
                        Expense firstexpense = new SplitEqual(g1.getMembers().get(1) ,g1.getMembers().get(1), 350, "Straßenessen");
                        firstexpense.addparticipant(f1);
                        firstexpense.addparticipant(f2);
                        firstexpense.addparticipant(f3);
                        firstexpense.addparticipant(f4);

                        g1.addExpense(firstexpense);

                        firstexpense = new SplitParts(g2.getMembers().get(1) ,g2.getMembers().get(1), 34, "Eis essen Hotel-Restaurant");
                        firstexpense.addparticipant(f3);
                        firstexpense.addparticipant(f6);
                        firstexpense.setitem(f3, 4);
                        firstexpense.setitem(f6, 1);
                        try {
                            firstexpense.calculatedebt();
                            //firstexpense.calculateDebtInHomeSpendings();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        g2.addExpense(firstexpense);


                        groupdao.addGroup(g1);
                        groupdao.addGroup(g2);
                        groupdao.addGroup(g3);
                        groupdao.addGroup(g4);

                        //saving groups
                        try {
                            groupdao.saveGroupData(StartActivity.this, "Groups");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/

                        Intent goToHomeScreen = new Intent(StartActivity.this, GroupActivity.class);
                        startActivity(goToHomeScreen);
                    }

                }

            });
        }

    }
}
