package at.ac.univie.frog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by tamara on 17.05.16.
 */

public class AddDummyActivity extends AppCompatActivity {

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
        Toast toast=Toast.makeText(getApplicationContext(),"Not implemented yet!",Toast.LENGTH_LONG);
        toast.show();
    }

    public void goToMeActivity(View v){
        Intent goToMe=new Intent(AddDummyActivity.this,MeActivity.class);
        startActivity(goToMe);
    }

    public void goToSettings(View v){
        Toast toast=Toast.makeText(getApplicationContext(),"Not implemented yet!",Toast.LENGTH_LONG);
        toast.show();
    }
}
