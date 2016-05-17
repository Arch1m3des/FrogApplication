package at.ac.univie.frog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Created by Tamara on 13.05.16.
 */

public class AddFriendActivity extends AppCompatActivity {

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_friend);
        getSupportActionBar().setTitle("Add Friend");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // add friend via QRCode? (name, surname & email address)
    }
}
