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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView group=(TextView) findViewById(R.id.imageFriendsWithText);
        group.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_friends_clicked,0,0);
        group.setTextColor(Color.parseColor("#000000"));

        button = (Button) findViewById(R.id.addFriend);
        email = (EditText) findViewById(R.id.editEmail);
        name = (EditText) findViewById(R.id.editName);
        surname = (EditText) findViewById(R.id.editSurname);

        button.setTransformationMethod(null); // to decapitalize the button text

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailToString = "dummy@mail.com"; //email.getText().toString();
                final String nameToString = name.getText().toString();
                final String surnameToString = surname.getText().toString();

                if (emailToString.length() == 0)
                    Toast.makeText(getApplicationContext(), "Please enter an email address.", Toast.LENGTH_LONG).show();
                else if (nameToString.length() == 0)
                    Toast.makeText(getApplicationContext(), "Please enter a name.", Toast.LENGTH_LONG).show();
                else if (surnameToString.length() == 0)
                    Toast.makeText(getApplicationContext(), "Please enter a surname.", Toast.LENGTH_LONG).show();

                else {
                    String[] parts=new String[3];
                    parts[0]=surnameToString;
                    parts[1]=nameToString;
                    parts[2]=emailToString;

                    Intent addQR = new Intent(AddDummyActivity.this, FriendActivity.class);
                    addQR.putExtra("values",parts);
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