package at.ac.univie.frog;

import at.ac.univie.SplitDAO.MapMarker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;

public class MapView extends AppCompatActivity implements OnMapReadyCallback{
    //MapMarker mapMarker;
    Marker marker;
    GoogleMap map;
    ArrayList<MapMarker> places = new ArrayList<MapMarker>();
    ArrayList<LatLng> points = new ArrayList<LatLng>();



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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map_view);
        getSupportActionBar().setTitle("Map");
        getSupportActionBar().setElevation(0);

        TextView settings = (TextView) findViewById(R.id.imageMapWithText);
        settings.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.ic_map_clicked,0,0);
        settings.setTextColor(Color.parseColor("#000000"));

        MapFragment mf = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mf.getMapAsync(this);
        //Hardcoded MapMarkers
        MapMarker perth = new MapMarker(-31.90, 115.86, "Snakes on the plane");
        MapMarker sydney = new MapMarker(-33.86, 151.20, "I met a guy pretending to be a banana");
        MapMarker canberra = new MapMarker (-35.343784, 149.082977, "the food was amazing!");
        //places.add(perth);
        //places.add(sydney);
        //places.add(canberra);
        places.add(new MapMarker(10.088645, 99.826005, "Streetfood at the beach"));
        places.add(new MapMarker(10.090023, 99.827195, "Divingsession with Callie"));
        places.add(new MapMarker(10.094486, 99.828300, "Freediving course"));
        places.add(new MapMarker(10.121690, 99.832549, "Bike Rental"));

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
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MapView Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://at.ac.univie.frog/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onMapReady(GoogleMap Gmap) {


        this.map = Gmap;
        //static final LatLng PERTH = new LatLng(-31.90, 115.86)
        PolylineOptions line = new PolylineOptions().width(5).color(Color.RED); //looks nice enough?
        LatLng obj = null;
        for(int i = 0; i<places.size(); i++){
            obj = new LatLng(places.get(i).getLat(), places.get(i).getLang());

            map.addMarker(new MarkerOptions() //for the markers
                    .position(obj)
                    .title(places.get(i).getDescription()));

            points.add(obj); //for the route
        }
        Polyline route = map.addPolyline(line);
        route.setPoints(points); //route


        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(places.get(0).getLat(),places.get(0).getLang()));
        builder.include(new LatLng(places.get(places.size()-1).getLat(),places.get(places.size()-1).getLang()));
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 13);
        this.map.animateCamera(cu);

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

    public void goToMeActivity(View v){
        Intent goToMe=new Intent(MapView.this , MeActivity.class);
        startActivity(goToMe);
    }

    public void goToSettings(View v){
        Intent goToSettings=new Intent(MapView.this , SettingActivity.class);
        startActivity(goToSettings);
    }
}
