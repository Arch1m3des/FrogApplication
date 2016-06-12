package at.ac.univie;

/**
 * Created by Tamara on 15.05.16.
 */

// name SplitIt? TravelSplit?

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import at.ac.univie.SplitDAO.CurrencyChanger;
import at.ac.univie.SplitDAO.Expense;
import at.ac.univie.SplitDAO.Friend;
import at.ac.univie.SplitDAO.FriendManager;
import at.ac.univie.SplitDAO.Group;
import at.ac.univie.SplitDAO.GroupManager;
import at.ac.univie.SplitDAO.MapMarker;
import at.ac.univie.SplitDAO.SplitEqual;
import at.ac.univie.SplitDAO.SplitParts;
import at.ac.univie.frog.R;
import at.ac.univie.main.GroupActivity;

public class StartActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    EditText email, surname, name;
    SharedPreferences.Editor editor;
    Button button;
    private GoogleApiClient client;

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getApplicationContext().getSharedPreferences("Log", 0);
        sharedPreferences = getSharedPreferences("Log", Context.MODE_PRIVATE);



        if (sharedPreferences.contains("Name")) {
            Intent goToOverview = new Intent(StartActivity.this, GroupActivity.class);
            startActivity(goToOverview);
        } else {

            setContentView(R.layout.content_start);
            getSupportActionBar().setTitle("Join us");
            getSupportActionBar().setElevation(0);

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

                    if (emailToString.length() == 0 || !emailToString.contains("@"))
                        Toast.makeText(getApplicationContext(), "Please enter your email address.", Toast.LENGTH_LONG).show();
                    else if (nameToString.length() < 2)
                        Toast.makeText(getApplicationContext(), "Please enter your name.", Toast.LENGTH_LONG).show();
                    else if (surnameToString.length() <= 1)
                        Toast.makeText(getApplicationContext(), "Please enter your surname.", Toast.LENGTH_LONG).show();

                    else {

                        editor.putString("Email", emailToString); // use Mailgun email verification!
                        editor.putString("Name", nameToString);
                        editor.putString("Surname", surnameToString);
                        editor.commit();

                        createDAO(sharedPreferences);

                        Intent goToHomeScreen = new Intent(StartActivity.this, GroupActivity.class);
                        startActivity(goToHomeScreen);
                    }

                }

            });
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void createDAO(SharedPreferences sharedPreferences) {

        //new CurrencyChanger(StartActivity.this).execute();


        //Add Friends & Groups Hardcoded and save them
        FriendManager frienddao = new FriendManager();


        Friend f1;
        if (sharedPreferences.contains("Name") && sharedPreferences.contains("Surname") && sharedPreferences.contains("Email")) {
            f1 = new Friend(1, sharedPreferences.getString("Surname", ""), sharedPreferences.getString("Name", ""), sharedPreferences.getString("Email", ""));
        } else {
            f1 = new Friend(1, "ICH", "HCI", "ich@du.at");
        }

        Friend f2 = new Friend(2, "Weinbahn", "Andy", "arch1m3des1988@gmail.com");
        Friend f3 = new Friend(3, "Druggs", "Tamara", "tammyd@googlemail.com");
        Friend f4 = new Friend(4, "Bubly", "Daniel", "ich@du.com");
        Friend f5 = new Friend(5, "Bada", "Margus", "ich@du.com");
        Friend f6 = new Friend(6, "Duda", "Samuel", "sammy@deluxe.com");

        frienddao.addFriend(f1);
        frienddao.addFriend(f2);
        frienddao.addFriend(f3);
        frienddao.addFriend(f4);
        frienddao.addFriend(f5);
        frienddao.addFriend(f6);

        try {
            frienddao.saveFriendData(getApplicationContext(), "Friends");
        } catch (IOException e) {
            e.printStackTrace();
        }

//
        //Add Groups
        GroupManager groupdao = new GroupManager();

        Group g1 = new Group(1, "Thailand");
        Group g2 = new Group(2, "London");
        Group g3 = new Group(3, "Spa Weekend");
        Group g4 = new Group(4, "Uruguay");

        //add currencies
        List<String> groupCurrencies = new ArrayList<>();

        groupCurrencies.add("EUR");
        groupCurrencies.add("USD");
        groupCurrencies.add("THB");
        groupCurrencies.add("RON");



        g1.setCurrencies(groupCurrencies);
        g2.setCurrencies(groupCurrencies);
        g3.setCurrencies(groupCurrencies);
        g4.setCurrencies(groupCurrencies);


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
        g4.addMember(f1);


        //add expenses
        Expense newExpense = new SplitEqual(g1.getMembers().get(0), 30, "Tempelbesuch", "Culture", 0);
        newExpense.setHasLocation(true);
        newExpense.addParticipant(f1);
        newExpense.addParticipant(f2);
        newExpense.addParticipant(f3);
        newExpense.addParticipant(f4);
        newExpense.setLatitude(13.746697);
        newExpense.setLongitude(100.4932212);

        newExpense.setCurrency("EUR");

        newExpense.setAmountinHomeCurrency(newExpense.getAmount());
        newExpense.setSpendingInHomeCurrency(newExpense.getSpending());


        g1.addPlace(new MapMarker(13.746697,100.4932212,"Tempelbesuch"));
        g1.addExpense(newExpense);

        newExpense = new SplitEqual(g1.getMembers().get(0), 5, "Früchte Essen", "Food", 0);
        newExpense.setHasLocation(true);
        newExpense.addParticipant(f1);
        newExpense.addParticipant(f2);
        newExpense.addParticipant(f3);
        newExpense.addParticipant(f4);
        newExpense.setLatitude(13.7134702);
        newExpense.setLongitude(100.5133035);

        newExpense.setCurrency("EUR");

        newExpense.setAmountinHomeCurrency(newExpense.getAmount());
        newExpense.setSpendingInHomeCurrency(newExpense.getSpending());


        g1.addPlace(new MapMarker(13.7134702,100.5133035,"Früchte Essen"));
        g1.addExpense(newExpense);

        newExpense = new SplitParts(f1, 34, "Fish'n'Chips essen", "Food", 1);
        newExpense.setHasLocation(true);
        newExpense.addParticipant(f3);
        newExpense.addParticipant(f6);
        newExpense.setitem(f3, 1);
        newExpense.setitem(f3, 4);
        newExpense.setitem(f6, 1);
        newExpense.setLatitude(51.5304439);
        newExpense.setLongitude(-0.826729);

        newExpense.setAmountinHomeCurrency(newExpense.getAmount());
        newExpense.setSpendingInHomeCurrency(newExpense.getSpending());

        try {
            newExpense.calculateDebt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        g2.addPlace(new MapMarker(51.5304439,-0.826729,"Fish'n'Chips"));
        g2.addExpense(newExpense);

        newExpense = new SplitEqual(f3, 28.50, "Eintritt Therme", "Culture", 0);
        newExpense.setHasLocation(true);
        newExpense.setLatitude(48.1265264);
        newExpense.setLongitude(16.3932835);
        try {
            newExpense.calculateDebt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        newExpense.setAmountinHomeCurrency(newExpense.getAmount());
        newExpense.setSpendingInHomeCurrency(newExpense.getSpending());


        g3.addPlace(new MapMarker(48.1265264,16.3932835,"Eintritt Therme"));
        g3.addExpense(newExpense);

        newExpense = new SplitEqual(f5, 30.50, "Eintritt Therme", "Culture", 0);
        newExpense.setHasLocation(true);
        newExpense.setLatitude(48.1265264);
        newExpense.setLongitude(16.4032835);
        try {
            newExpense.calculateDebt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        newExpense.setAmountinHomeCurrency(newExpense.getAmount());
        newExpense.setSpendingInHomeCurrency(newExpense.getSpending());

        g3.addPlace(new MapMarker(48.1265264,16.4032835,"Eintritt Therme"));
        g3.addExpense(newExpense);


        newExpense = new SplitParts(f3, 340, "Safari", "Culture", 1);
        newExpense.setHasLocation(true);
        newExpense.setLatitude(-36.459652);
        newExpense.setLongitude(-64.0360471);
        newExpense.addParticipant(f2);
        newExpense.addParticipant(f1);

        newExpense.setCurrency("EUR");
        newExpense.setitem(f3, 2);
        newExpense.setitem(f2, 1);
        newExpense.setitem(f2, 1);

        try {
            newExpense.calculateDebt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        newExpense.setAmountinHomeCurrency(newExpense.getAmount());
        newExpense.setSpendingInHomeCurrency(newExpense.getSpending());


        g4.addPlace(new MapMarker(-36.459652,-64.0360471,"Safari"));
        g4.addExpense(newExpense);

        groupdao.addGroup(g1);
        groupdao.addGroup(g2);
        groupdao.addGroup(g3);
        groupdao.addGroup(g4);

        try {
            groupdao.saveGroupData(getApplicationContext(), "Groups");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
