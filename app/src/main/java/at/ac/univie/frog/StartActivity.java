package at.ac.univie.frog;

/**
 * Created by Tamara on 15.05.16.
 */

// name SplitIt? TravelSplit?

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    EditText email, surname, name;
    SharedPreferences.Editor editor;
    Button button;

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getApplicationContext().getSharedPreferences("Log", 0);

        if (sharedPreferences.contains("Name")) {
            Intent goToOverview = new Intent(StartActivity.this, Overview.class);
            startActivity(goToOverview);

        }

        else {

            setContentView(R.layout.content_start);
            getSupportActionBar().setTitle("Join us");

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

                    if (emailToString.length() == 0)
                        Toast.makeText(getApplicationContext(), "Please enter your email address.", Toast.LENGTH_LONG).show();
                    else if (nameToString.length() == 0)
                        Toast.makeText(getApplicationContext(), "Please enter your name.", Toast.LENGTH_LONG).show();
                    else if (surnameToString.length() == 0)
                        Toast.makeText(getApplicationContext(), "Please enter your surname.", Toast.LENGTH_LONG).show();

                    else {

                        editor.putString("Email", emailToString); // use Mailgun email verification!
                        editor.putString("Name", nameToString);
                        editor.putString("Surname", surnameToString);
                        editor.commit();

                        Intent goToOverview = new Intent(StartActivity.this, Overview.class);
                        startActivity(goToOverview);
                    }

                }

            });
        }
    }
}
