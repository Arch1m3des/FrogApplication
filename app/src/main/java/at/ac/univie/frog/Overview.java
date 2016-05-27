package at.ac.univie.frog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import at.ac.univie.SplitDAO.Expense;
import at.ac.univie.SplitDAO.Expense;
import at.ac.univie.SplitDAO.Friend;
import at.ac.univie.SplitDAO.FriendManager;
import at.ac.univie.SplitDAO.Group;
import at.ac.univie.SplitDAO.GroupManager;
import at.ac.univie.SplitDAO.SplitEqual;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.encoder.QRCode;

import java.io.IOException;
import java.util.List;


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


        //manage groups

        Group thailand = new Group(1, "Thailand");
        try {
            thailand.addMember(andy);
            thailand.addMember(max);
            thailand.addMember(max2);
            thailand.addMember(max3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Expense firstexpense = new SplitEqual(max ,max, 26, "Erstes Essen nach dem Krankenhaus");
        firstexpense.addparticipant(andy);
        firstexpense.addparticipant(max2);
        firstexpense.addparticipant(max3);

        GroupManager  x =  new GroupManager();
        x.addGroup(thailand);


        //Save groups
        /*
        try {
            x.saveGroupData(this, "Groups");
            TextView textView2 = (TextView) findViewById(R.id.textView2);
            textView2.setText("Successfully saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
*/


        //Load groups
        try {
            x.loadGroupData(this, "Groups");
            TextView textView2 = (TextView) findViewById(R.id.textView2);
            textView2.setText("Successfully loaded2");
            textView2.setText(x.groupList.get(0).getName());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


       // System.out.println(x.groupList.get(1).getName());
/*
        Group thailand = new Group(1, "Thailand1");
        try {
            thailand.addMember(andy);
            thailand.addMember(max);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Group thailand2 = new Group(1, "Thailand2");
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
