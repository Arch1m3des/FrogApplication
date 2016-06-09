package at.ac.univie;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import at.ac.univie.adapter.FancyListAdapter;
import at.ac.univie.frog.R;
import at.ac.univie.main.FriendActivity;
import at.ac.univie.main.GroupActivity;
import at.ac.univie.main.MapView;
import at.ac.univie.main.MeActivity;
import at.ac.univie.main.SettingActivity;

/**
 * Created by Tamara on 13.05.16.
 */

public class AddFriendActivity extends AppCompatActivity {

    ListView searchView;
    ArrayAdapter adapter;
    ArrayList<String> friends = new ArrayList();
    ArrayList<String> sign = new ArrayList();
    ArrayList<String> date = new ArrayList();
    ArrayList<String> amount = new ArrayList();
    ArrayList<String> color = new ArrayList();

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_add_friend);
        getSupportActionBar().setTitle("Add Friend");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView group=(TextView) findViewById(R.id.imageFriendsWithText);
        group.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_friends_clicked,0,0);
        group.setTextColor(Color.parseColor("#000000"));

        searchView = (ListView) findViewById(R.id.searchView);
        adapter = new FancyListAdapter(this, R.layout.fancy_list, friends, sign, amount, date, color);

        searchView.setAdapter(adapter);

        friends.add("Add Dummy");
        sign.add("+");
        amount.add("");
        date.add("");
        color.add("#6E6E6E");
        friends.add("Add Friend by QR-Code");
        sign.add("+");
        amount.add("");
        date.add("");
        color.add("#6E6E6E");

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {

                if (position == 0) {
                    Intent addDummy = new Intent(AddFriendActivity.this, AddDummyActivity.class);
                    startActivity(addDummy);
                }else if(position == 1){
                    //Starting QR Scan Intent. Greift auf die Klassen von zxing zu, welche das scannen des QR Codes uebernehmen.
                    IntentIntegrator scanIntegrator = new IntentIntegrator(AddFriendActivity.this);
                    scanIntegrator.initiateScan();
                }
                else if (position == 1){
                    //TODO implement QR-Code scan and friend add
                }
                else {
                    //TODO add existing friend to friendlist
                }

            }

        });

    }

    // add friend via QRCode? (name, surname & email address)
    // Auslesen und Verwerten des Ergebnis.
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //TODO chatching Crash when going back from scan-app
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
                    addQR.putExtra("values",parts);
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
        Intent goToFriends=new Intent(AddFriendActivity.this,FriendActivity.class);
        startActivity(goToFriends);
    }

    public void goToGroupActivity(View v){
        Intent goToFriends=new Intent(AddFriendActivity.this,GroupActivity.class);
        startActivity(goToFriends);
    }

    public void goToMap(View v){
        Intent goToMaps = new Intent(AddFriendActivity.this, MapView.class);
        startActivity(goToMaps);
    }

    public void goToMeActivity(View v){
        Intent goToMe=new Intent(AddFriendActivity.this,MeActivity.class);
        startActivity(goToMe);
    }

    public void goToSettings(View v){
        Intent goToSettings=new Intent(AddFriendActivity.this, SettingActivity.class);
        startActivity(goToSettings);
    }
}
