package at.ac.univie.frog;

import android.content.Context;
import android.content.Intent;
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
import at.ac.univie.SplitDAO.Friend;
import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.encoder.QRCode;


public class Overview extends AppCompatActivity {
//First Push to git
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_overview);

        final TextView textView = (TextView) findViewById(R.id.textView);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Button friends = (Button) findViewById(R.id.button);


        Friend max = new Friend(1, "Weinbahn", "Andy");
        System.out.println(max.toString());
        textView.setText(max.toString());
        String code = max.getUniqueid().toString();
        code = "HALT DIE FRESSE";

        QRGenerate myqr = new QRGenerate(Overview.this, textView, imageView, code);
        myqr.execute();


        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goToFriends = new Intent(Overview.this, FriendActivity.class);
                startActivity(goToFriends);

            }

        });


    }

}
