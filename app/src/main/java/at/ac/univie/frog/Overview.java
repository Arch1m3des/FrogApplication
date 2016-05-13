package at.ac.univie.frog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import at.ac.univie.SplitDAO.Expense;
import at.ac.univie.SplitDAO.Friend;
import at.ac.univie.SplitDAO.SplitEqual;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.encoder.QRCode;

import java.util.List;


public class Overview extends AppCompatActivity {
//First Push to git
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        final TextView textView = (TextView) findViewById(R.id.textView);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);


        Friend max = new Friend(1, "Weinbahn", "Andy", "ich@du.com");
        Friend andy = new Friend(2, "Hagen", "Nina",  "ich@du.com");
        System.out.println(max.toString());
        textView.setText(max.toString());
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





    }




}
