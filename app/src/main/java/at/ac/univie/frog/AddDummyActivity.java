package at.ac.univie.frog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
        getSupportActionBar().setTitle("Add Dummy");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
