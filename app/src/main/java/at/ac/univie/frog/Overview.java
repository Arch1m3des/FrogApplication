package at.ac.univie.frog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import at.ac.univie.SplitDAO.Expense;
import at.ac.univie.SplitDAO.Friend;
import at.ac.univie.SplitDAO.SplitEqual;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.encoder.QRCode;

import java.util.List;


public class Overview extends AppCompatActivity {


    private SharedPreferences sharedPreferences;
    String name, surname, email;

    /**
     * Restricts user to go back to registration details after filling them out
     */

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_overview);
        getSupportActionBar().setTitle("Overview");
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back_button);

        final TextView textView = (TextView) findViewById(R.id.textView);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Button friends = (Button) findViewById(R.id.button);
        Button delete = (Button) findViewById(R.id.delete);
        Button group = (Button) findViewById((R.id.group));

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

        Toast.makeText(getApplicationContext(), "name = " + name + ", surname = " + surname + ", email = " + email, Toast.LENGTH_LONG).show();



        Friend max = new Friend(1, "Weinbahn", "Andy", "ich@du.com");
        Friend andy = new Friend(2, "Hagen", "Nina",  "ich@du.com");
        System.out.println(max.toString());
        String code = max.getUniqueid().toString();
        code = "HALT DIE FRESSE";

/*

        Expense first = new SplitEqual(max, max, (double) 30, "Kai ist gut");
        try {
            first.addparticipant(andy);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Double> list;
        list = first.calculate
*/

        QRGenerate myqr = new QRGenerate(Overview.this, textView, imageView, code);
        myqr.execute();

        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToFriends = new Intent(Overview.this, FriendActivity.class);
                startActivity(goToFriends);

            }

        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getApplicationContext().getSharedPreferences("Log", 0).edit().clear().commit();

            }

        });

        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToGroup = new Intent(Overview.this, GroupActivity.class);
                startActivity(goToGroup);

            }

        });


    }

}
