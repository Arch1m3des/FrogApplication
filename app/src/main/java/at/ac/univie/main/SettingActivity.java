package at.ac.univie.main;

import android.content.Intent;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import at.ac.univie.frog.R;

public class SettingActivity extends AppCompatActivity {

    Switch controlGPS;
    boolean canGetLocation;

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

        controlGPS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Toast.makeText(getApplicationContext(), "yey", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

            }

        });
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

    public void goToMeActivity(View v){
        Intent goToMe=new Intent(SettingActivity.this,MeActivity.class);
        startActivity(goToMe);
    }

    public void goToSettings(View v){
        //
    }
}
