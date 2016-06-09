package at.ac.univie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import at.ac.univie.SplitDAO.Expense;
import at.ac.univie.SplitDAO.Friend;
import at.ac.univie.SplitDAO.FriendManager;
import at.ac.univie.frog.R;
import at.ac.univie.main.FriendActivity;
import at.ac.univie.main.GroupActivity;
import at.ac.univie.qr.QRGenerate;

import java.io.IOException;


public class Overview extends AppCompatActivity {


    private SharedPreferences sharedPreferences;
    String name, surname, email;

    /**
     * Restricts user to go back to registration details after filling them out
     */

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_overview);
        getSupportActionBar().setTitle("Overview");
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_button);

        final TextView textView = (TextView) findViewById(R.id.textView);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Button friends = (Button) findViewById(R.id.button);
        Button delete = (Button) findViewById(R.id.delete);
        Button group = (Button) findViewById((R.id.group));

        sharedPreferences = getSharedPreferences("Log", Context.MODE_PRIVATE);

        if (sharedPreferences.contains("Name")) {
            name = sharedPreferences.getString("Name", "");
        }

        if (sharedPreferences.contains("Surname")) {
            surname = sharedPreferences.getString("Surname", "");
        }

        if (sharedPreferences.contains("Email")) {
            email = sharedPreferences.getString("Email", "");
        }


        Toast.makeText(getApplicationContext(), "name = " + name + ", surname = " + surname + ", email = " + email, Toast.LENGTH_LONG).show();


        FriendManager friendsdao =  new FriendManager();


        Friend max =  new Friend(1,"Glett", "Max", "ich@du.com");
        Friend andy = new Friend(2,"Weinbahn", "Andy", "ich@du.com");
        System.out.println(max.toString());
        String code = max.getUniqueid().toString();
        code = surname + ";" + name + ";" + email;


        Friend max2 =  new Friend(3,"Dreibahn", "Andy", "ich@du.com");
        Friend max3 =  new Friend(4,"Vierbah", "Andy", "ich@du.com");

        friendsdao.addFriend(max);
        friendsdao.addFriend(andy);
        friendsdao.addFriend(max2);
        friendsdao.addFriend(max3);


        //saving friends
        try {
            friendsdao.saveFriendData(this, "Friends");
            TextView textView2 = (TextView) findViewById(R.id.textView2);
            textView2.setText("Successfully saved");
        } catch (IOException e) {
            e.printStackTrace();
        }


        //load friends

        /*
        try {
            friendsdao.loadFriendData(this, "Friends");
            TextView textView2 = (TextView) findViewById(R.id.textView2);
            textView2.setText("Successfully loaded friends");
            textView2.setText(friendsdao.friendList.get(0).getName());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        */


        //manage parents




        //Save parents
        /*
        try {
            x.saveGroupData(this, "Groups");
            TextView textView2 = (TextView) findViewById(R.id.textView2);
            textView2.setText("Successfully saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
*/





       // System.out.println(x.groupList.get(1).getName());
/*
        Parent thailand = new Parent(1, "Thailand1");
        try {
            thailand.addMember(andy);
            thailand.addMember(max);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Parent thailand2 = new Parent(1, "Thailand2");
        try {
            thailand2.addMember(andy);
            thailand2.addMember(max);

        } catch (Exception e) {
            e.printStackTrace();
        }

        x.groupList = thailand2;

        try {
            x.saveGroupData(this, "1");
            TextView textView2 = (TextView) findViewById(R.id.textView2);
            textView2.setText("Scuccess");
        } catch (IOException e) {
            e.printStackTrace();
        }
        */








/*

        Expense first = new SplitEqual(max, max, (double) 30, "Kai ist gut");
        try {
            first.addparticipant(andy);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Double> list;
        list = first.calculate
*/

        QRGenerate myqr = new QRGenerate(Overview.this, textView, imageView, code);
        myqr.execute();

        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToFriends = new Intent(Overview.this, FriendActivity.class);
                startActivity(goToFriends);

            }

        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getApplicationContext().getSharedPreferences("Log", 0).edit().clear().commit();

            }

        });

        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToGroup = new Intent(Overview.this, GroupActivity.class);
                startActivity(goToGroup);

            }

        });


    }

}
