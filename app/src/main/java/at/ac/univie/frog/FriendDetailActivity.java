package at.ac.univie.frog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Created by Tamara on 13.05.16.
 */

public class FriendDetailActivity extends AppCompatActivity {

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
