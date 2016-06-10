package at.ac.univie.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import at.ac.univie.SearchActivity;
import at.ac.univie.frog.R;
import at.ac.univie.StartActivity;
import at.ac.univie.qr.QRGenerate;

public class MeActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String name, surname, email;
    EditText emailEdit,surnameEdit,nameEdit;

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_done:
                emailEdit=(EditText) findViewById(R.id.editEmail);
                surnameEdit=(EditText) findViewById(R.id.editSurname);
                nameEdit=(EditText) findViewById(R.id.editName);

                email=emailEdit.getText().toString();
                surname=surnameEdit.getText().toString();
                name=nameEdit.getText().toString();

                sharedPreferences = getSharedPreferences("Log", Context.MODE_PRIVATE);

                if(!sharedPreferences.getString("Email","").equals(email)||
                        !sharedPreferences.getString("Surname","").equals(surname)||
                        !sharedPreferences.getString("Name","").equals(name)) {

                    editor = sharedPreferences.edit();

                    if (email.length() == 0)
                        Toast.makeText(getApplicationContext(), "Please enter your email address.", Toast.LENGTH_LONG).show();
                    else if (surname.length() == 0)
                        Toast.makeText(getApplicationContext(), "Please enter your surname.", Toast.LENGTH_LONG).show();
                    else if (name.length() == 0)
                        Toast.makeText(getApplicationContext(), "Please enter your name.", Toast.LENGTH_LONG).show();

                    else {

                        editor.putString("Email", email); // use Mailgun email verification!
                        editor.putString("Surname", surname);
                        editor.putString("Name", name);
                        editor.commit();
                    }
                }

                Intent toSettings = new Intent(MeActivity.this, SettingActivity.class);
                startActivity(toSettings);
                return true;
            default: return  false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_me);
        getSupportActionBar().setTitle("Persönliche Daten");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView settings = (TextView) findViewById(R.id.imageSettingsWithText);
        settings.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_settings_clicked,0,0);
        settings.setTextColor(Color.parseColor("#000000"));

        emailEdit=(EditText) findViewById(R.id.editEmail);
        surnameEdit=(EditText) findViewById(R.id.editSurname);
        nameEdit=(EditText) findViewById(R.id.editName);

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

        emailEdit.setText(email);
        nameEdit.setText(name);
        surnameEdit.setText(surname);
    }

    public void deleteSharedPreferences(View v) {
        getApplicationContext().getSharedPreferences("Log", 0).edit().clear().commit();
        Intent goToStart = new Intent(MeActivity.this, StartActivity.class);
        startActivity(goToStart);
    }

    public void gotToFriendsActivity(View v){
        Intent goToFriends=new Intent(MeActivity.this,FriendActivity.class);
        startActivity(goToFriends);
    }

    public void goToGroupActivity(View v){
        Intent goToGroups=new Intent(MeActivity.this,GroupActivity.class);
        startActivity(goToGroups);
    }

    public void goToMap(View v){
        Intent goToMaps = new Intent(MeActivity.this, MapView.class);
        startActivity(goToMaps);
    }

    public void goToSettings(View v){
        Intent goToSettings=new Intent(MeActivity.this, SettingActivity.class);
        startActivity(goToSettings);
    }
}
