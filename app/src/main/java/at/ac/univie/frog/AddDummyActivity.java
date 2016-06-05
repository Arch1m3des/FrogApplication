package at.ac.univie.frog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import at.ac.univie.SplitDAO.Friend;
import at.ac.univie.SplitDAO.FriendManager;

/**
 * Created by tamara on 17.05.16.
 */

public class AddDummyActivity extends AppCompatActivity {
    EditText email, surname, name;
    Button button;

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_dummy);
        getSupportActionBar().setTitle("Add Friend");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView group=(TextView) findViewById(R.id.imageFriendsWithText);
        group.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_friends_clicked,0,0);
        group.setTextColor(Color.parseColor("#000000"));

        button = (Button) findViewById(R.id.addFriend);
        email = (EditText) findViewById(R.id.editEmail);
        email.setHint(email.getHint().toString() + " (optional)");
        name = (EditText) findViewById(R.id.editName);
        surname = (EditText) findViewById(R.id.editSurname);

        button.setTransformationMethod(null); // to decapitalize the button text

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailToString = (email.getText().toString().length()==0) ? "@mailto" : email.getText().toString();
                final String nameToString = name.getText().toString();
                final String surnameToString = surname.getText().toString();


                if (nameToString.length() == 0)
                    Toast.makeText(getApplicationContext(), "Please enter a name.", Toast.LENGTH_LONG).show();
                else if (surnameToString.length() == 0)
                    Toast.makeText(getApplicationContext(), "Please enter a surname.", Toast.LENGTH_LONG).show();

                else {
                    String[] parts = new String[3];
                    parts[0]=surnameToString;
                    parts[1]=nameToString;
                    parts[2]=emailToString;

                    FriendManager frienddao = new FriendManager();
                    try {
                        frienddao.loadFriendData(getApplicationContext(),"Friends");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    ArrayList<Friend> friends = frienddao.getFriendList();
                    friends.add(new Friend(friends.size(), surnameToString, nameToString, emailToString));

                    frienddao.setFriendList(friends);
                    try {
                        frienddao.saveFriendData(getApplicationContext(),"Friends");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Intent addQR = new Intent(AddDummyActivity.this, FriendActivity.class);
                    //addQR.putExtra("values",parts);
                    startActivity(addQR);
                }

            }

        });
    }

    public void gotToFriendsActivity(View v){
        Intent goToFriends=new Intent(AddDummyActivity.this,FriendActivity.class);
        startActivity(goToFriends);
    }

    public void goToGroupActivity(View v){
        Intent goToFriends=new Intent(AddDummyActivity.this,GroupActivity.class);
        startActivity(goToFriends);
    }

    public void goToMap(View v){
        Intent goToMaps = new Intent(AddDummyActivity.this, MapView.class);
        startActivity(goToMaps);
    }

    public void goToMeActivity(View v){
        Intent goToMe=new Intent(AddDummyActivity.this,MeActivity.class);
        startActivity(goToMe);
    }

    public void goToSettings(View v){
        Intent goToSettings=new Intent(AddDummyActivity.this, SettingActivity.class);
        startActivity(goToSettings);
    }
}