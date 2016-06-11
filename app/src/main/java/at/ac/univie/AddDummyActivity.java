package at.ac.univie;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.util.ArrayList;

import at.ac.univie.SplitDAO.Friend;
import at.ac.univie.SplitDAO.FriendManager;
import at.ac.univie.frog.R;
import at.ac.univie.main.FriendActivity;
import at.ac.univie.main.GroupActivity;
import at.ac.univie.main.MapView;
import at.ac.univie.main.MeActivity;
import at.ac.univie.main.SettingActivity;

/**
 * Created by tamara on 17.05.16.
 */

public class AddDummyActivity extends AppCompatActivity {
    EditText email, surname, name;

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_friend_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_qr:
                //Starting QR Scan Intent. Greift auf die Klassen von zxing zu, welche das scannen des QR Codes uebernehmen.
                IntentIntegrator scanIntegrator = new IntentIntegrator(AddDummyActivity.this);
                scanIntegrator.initiateScan();
                return true;
            case R.id.action_menu_done:

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
                    friends.add(new Friend(friends.size()+1, surnameToString, nameToString, emailToString));

                    frienddao.setFriendList(friends);
                    try {
                        frienddao.saveFriendData(getApplicationContext(),"Friends");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Intent goToFriends = new Intent(AddDummyActivity.this, FriendActivity.class);
                    startActivity(goToFriends);
                }

                return true;
            default: return  false;
        }
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

        email = (EditText) findViewById(R.id.editEmail);
        email.setHint(email.getHint().toString() + " (optional)");
        name = (EditText) findViewById(R.id.editName);
        surname = (EditText) findViewById(R.id.editSurname);
    }

    // add friend via QRCode? (name, surname & email address)
    // Auslesen und Verwerten des Ergebnis.
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //TODO chatching Crash when going back from scan-app
        if(intent==null){
            return;
        }

        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        //Ueberpruefen ob gescannt werden konnte
        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            //Ueberpruefen ob Scan ein QR Code war
            if(scanFormat.equals("QR_CODE")){
                String parts[]=scanContent.split(";");

                //Ueberpruefen ob der QR Code aus 3 Teilen besteht
                if(parts.length==3){
                    //Besteht der Content aus 3 Teilen, nehmen wir an das einer von unseren QR Codes gescannt wurde
                    //Nun wird ein neuer Intent angelegt, die Daten mitgegeben und danach wird FriendActivity gestartet
                    Intent addQR = new Intent(this, FriendActivity.class);

                    FriendManager frienddao = new FriendManager();
                    try {
                        frienddao.loadFriendData(getApplicationContext(),"Friends");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    ArrayList<Friend> friends = frienddao.getFriendList();
                    friends.add(new Friend(friends.size()+1, parts[0], parts[1], parts[2]));

                    frienddao.setFriendList(friends);
                    try {
                        frienddao.saveFriendData(getApplicationContext(),"Friends");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    //addQR.putExtra("values",parts);
                    startActivity(addQR);
                }else{
                    //Sollte der gescannte Content nicht aus 3 Teilen bestehen, handelt es sich nicht um einen QR Code von uns
                    Toast toast = Toast.makeText(getApplicationContext(),"Scant QR Code wasn´t our!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }else{
                //Sollte es sich bei dem gescannten Objekt nicht um einen QR Code handeln, wird ein Fehler ausgegeben
                Toast toast = Toast.makeText(getApplicationContext(),"Scan wasn´t a QR Code!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

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

    public void goToSettings(View v){
        Intent goToSettings=new Intent(AddDummyActivity.this, SettingActivity.class);
        startActivity(goToSettings);
    }
}