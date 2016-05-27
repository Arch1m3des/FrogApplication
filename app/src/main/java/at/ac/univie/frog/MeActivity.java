package at.ac.univie.frog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MeActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String name, surname, email;
    EditText emailEdit,surnameEdit,nameEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        getSupportActionBar().setTitle("Me");

        TextView group=(TextView) findViewById(R.id.imageMeWithText);
        group.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_me_clicked,0,0);
        group.setTextColor(Color.parseColor("#000000"));

        final TextView qrCodeText = (TextView) findViewById(R.id.qrCodeText);
        final ImageView qrCode = (ImageView) findViewById(R.id.qrCode);
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

        String code = surname + ";" + name + ";" + email;

        QRGenerate myqr = new QRGenerate(MeActivity.this, qrCodeText, qrCode, code);
        myqr.execute();
    }

    public void updateUserdata(View v){
        final TextView qrCodeText = (TextView) findViewById(R.id.qrCodeText);
        final ImageView qrCode = (ImageView) findViewById(R.id.qrCode);
        emailEdit=(EditText) findViewById(R.id.editEmail);
        surnameEdit=(EditText) findViewById(R.id.editSurname);
        nameEdit=(EditText) findViewById(R.id.editName);

        email=emailEdit.getText().toString();
        surname=surnameEdit.getText().toString();
        name=nameEdit.getText().toString();

        sharedPreferences = getSharedPreferences("Log", Context.MODE_PRIVATE);

        if(!sharedPreferences.getString("Email","").equals(email)||
                !sharedPreferences.getString("Surname","").equals(surname)||
                !sharedPreferences.getString("Name","").equals(name)){

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

                String code = surname + ";" + name + ";" + email;

                QRGenerate myqr = new QRGenerate(MeActivity.this, qrCodeText, qrCode, code);
                myqr.execute();
            }
        }
    }

    public void gotToFriendsActivity(View v){
        Intent goToFriends=new Intent(MeActivity.this,FriendActivity.class);
        startActivity(goToFriends);
    }

    public void goToGroupActivity(View v){
        Intent goToFriends=new Intent(MeActivity.this,GroupActivity.class);
        startActivity(goToFriends);
    }

    public void goToMap(View v){
        Toast toast=Toast.makeText(getApplicationContext(),"Not implemented yet!",Toast.LENGTH_LONG);
        toast.show();
    }

    public void goToMeActivity(View v){
        Toast toast=Toast.makeText(getApplicationContext(),"You are already in the Me Tab!",Toast.LENGTH_LONG);
        toast.show();
    }

    public void goToSettings(View v){
        Toast toast=Toast.makeText(getApplicationContext(),"Not implemented yet!",Toast.LENGTH_LONG);
        toast.show();
    }
}