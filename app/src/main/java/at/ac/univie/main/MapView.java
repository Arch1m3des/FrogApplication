package at.ac.univie.main;

import at.ac.univie.SplitDAO.*;
import at.ac.univie.frog.R;
import at.ac.univie.SearchActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;

public class MapView extends AppCompatActivity implements OnMapReadyCallback{
    //MapMarker mapMarker;
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String gruppenname;

    GoogleMap map;
    ArrayList<MapMarker> placesGroups = new ArrayList();
    ArrayList<LatLng> points = new ArrayList<LatLng>();
    GroupManager groupManager;
    String groupname;
    boolean doubleBackToExitPressedOnce=false;

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    //Constructor
    /**
    public MapView(double Longitude, double Lattitude, String Description){
        this.Longitude = Longitude;
        this.Lattitude = Lattitude;
        this.Description = Description;
    }
    **/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_search:
                Intent addGroup = new Intent(MapView.this, SearchActivity.class);
                startActivity(addGroup);
                return true;
            default: return  false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map_view);
        getSupportActionBar().setElevation(0);
        if (getIntent().hasExtra("lat") && getIntent().hasExtra("long"))
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView settings = (TextView) findViewById(R.id.imageMapWithText);
        settings.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_map_clicked,0,0);
        settings.setTextColor(Color.parseColor("#000000"));

        groupManager = new GroupManager();

        try {
            groupManager.loadGroupData(getApplicationContext(), "Groups");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<Group> groups = groupManager.getGroupList();

        Intent intent=getIntent();
        if(intent.hasExtra("GroupPosition")){
            groupname = intent.getStringExtra("GroupPosition");
            getSupportActionBar().setTitle(groupname);

            sharedPreferences = getSharedPreferences("Log", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString("Groupname", groupname);
            editor.commit();

            for(Group group:groups){
                if(group.getName().equals(groupname)){
                    placesGroups.addAll(group.getPlaces());
                }
            }
        }else {
            sharedPreferences = getSharedPreferences("Log", Context.MODE_PRIVATE);

            if (sharedPreferences.contains("Groupname")) {
                gruppenname = sharedPreferences.getString("Groupname", "");
                getSupportActionBar().setTitle(gruppenname);

                for(Group group:groups){
                    if(group.getName().equals(gruppenname)){
                        placesGroups.addAll(group.getPlaces());
                    }
                }

            }else {
                Group group = groups.get(0);
                getSupportActionBar().setTitle(group.getName());
                placesGroups.addAll(group.getPlaces());
            }
        }

        MapFragment mf = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mf.getMapAsync(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MapView Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://at.ac.univie.frog/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "MapView Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://at.ac.univie.frog/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onMapReady(GoogleMap Gmap) {
        LatLngBounds bounds=null;

        if(placesGroups.isEmpty()){
            Toast.makeText(this, "There are no locations saved in this group", Toast.LENGTH_SHORT).show();
        }else {
            this.map = Gmap;

            PolylineOptions line = new PolylineOptions().width(5).color(Color.RED); //looks nice enough?
            LatLng obj = null;
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            for (MapMarker marker : placesGroups) {
                obj = new LatLng(marker.getLat(), marker.getLang());
                map.addMarker(new MarkerOptions().position(obj).title(marker.getDescription()));
                points.add(obj);

                if (getIntent().hasExtra("lat") && getIntent().hasExtra("long")) {
                   builder.include(new LatLng(getIntent().getDoubleExtra("lat", 0), getIntent().getDoubleExtra("long", 0)));
                }
                else
                    builder.include(obj);
            }

            Polyline route = map.addPolyline(line);
            route.setPoints(points); //route
            bounds = builder.build();

            CameraUpdate cu;

            try{
                cu = CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 10.0f);
                map.animateCamera(cu);
            } catch(IllegalStateException e) {
                e.printStackTrace();
                cu = CameraUpdateFactory.newLatLngBounds(bounds,6000,6000,0);
                map.animateCamera(cu);
            }
        }

        /*  //this didn't work for me
        cu = CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 13.0f);
        this.map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,padding));
        this.map.animateCamera(cu);
        */
    }

    public void gotToFriendsActivity(View v){
        Intent goToFriends = new Intent(MapView.this, FriendActivity.class);
        startActivity(goToFriends);
    }

    public void goToGroupActivity(View v){
        Intent goToGroups=new Intent(MapView.this,GroupActivity.class);
        startActivity(goToGroups);
    }

    public void goToMap(View v){
       //
    }

    public void goToSettings(View v){
        Intent goToSettings=new Intent(MapView.this , SettingActivity.class);
        startActivity(goToSettings);
    }
}
