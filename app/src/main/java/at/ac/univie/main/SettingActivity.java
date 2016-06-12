package at.ac.univie.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.util.ArrayList;

import at.ac.univie.AddFriendActivity;
import at.ac.univie.SplitDAO.Friend;
import at.ac.univie.SplitDAO.FriendManager;
import at.ac.univie.SplitDAO.MapMarker;
import at.ac.univie.frog.R;
import at.ac.univie.qr.QRGenerate;

public class SettingActivity extends AppCompatActivity{
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String name, surname, email;
    Switch controlGPS;

    @Override
    protected void onRestart(){
        super.onRestart();

        controlGPS = (Switch) findViewById(R.id.gps);

        LocationManager lm=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            controlGPS.setChecked(false);
        }else{
            controlGPS.setChecked(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_setting);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setElevation(0);

        TextView settings = (TextView) findViewById(R.id.imageSettingsWithText);
        settings.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_settings_clicked,0,0);
        settings.setTextColor(Color.parseColor("#000000"));

        controlGPS = (Switch) findViewById(R.id.gps);

        LocationManager lm=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            controlGPS.setChecked(false);
        }else{
            controlGPS.setChecked(true);
        }

        final TextView qrCodeText = (TextView) findViewById(R.id.qrCodeText);
        final ImageView qrCode = (ImageView) findViewById(R.id.qrCode);

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

        String code = surname + ";" + name + ";" + email;

        QRGenerate myqr = new QRGenerate(SettingActivity.this, qrCodeText, qrCode, code);
        myqr.execute();


        controlGPS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }

        });

        qrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Starting QR Scan Intent. Greift auf die Klassen von zxing zu, welche das scannen des QR Codes uebernehmen.
                IntentIntegrator scanIntegrator = new IntentIntegrator(SettingActivity.this);
                scanIntegrator.initiateScan();

            }
        });

    }

    public void changeUserData(View v){
        Intent goToMe = new Intent(SettingActivity.this,MeActivity.class);
        startActivity(goToMe);
    }

    public void gotToFriendsActivity(View v){
        Intent goToFriends = new Intent(SettingActivity.this, FriendActivity.class);
        startActivity(goToFriends);
    }

    public void goToGroupActivity(View v){
        Intent goToGroups=new Intent(SettingActivity.this,GroupActivity.class);
        startActivity(goToGroups);
    }

    public void goToMap(View v){
        Intent goToMaps = new Intent(SettingActivity.this, MapView.class);
        startActivity(goToMaps);
    }

    public void goToSettings(View v){
        //
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
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

                    
                    Intent addQR = new Intent(this, FriendActivity.class);
                    addQR.putExtra("values",parts);
                    startActivity(addQR);
                }else{
                    //Sollte der gescannte Content nicht aus 3 Teilen bestehen, handelt es sich nicht um einen QR Code von uns
                    Toast toast = Toast.makeText(getApplicationContext(),"Scanned QR Code wasn´t a Friend!", Toast.LENGTH_SHORT);
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
}
