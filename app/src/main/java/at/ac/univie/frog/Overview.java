package at.ac.univie.frog;

import android.graphics.Bitmap;
import android.media.Image;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import at.ac.univie.SplitDAO.Friend;

public class Overview extends AppCompatActivity {
//First Push to git
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        TextView textView = (TextView) findViewById(R.id.textView);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        Friend max = new Friend(1, "Weinbahn", "Andy");
        System.out.println(max.toString());
        textView.setText(max.toString());
        String maxuid = max.getUniqueid().toString();

    }

}
