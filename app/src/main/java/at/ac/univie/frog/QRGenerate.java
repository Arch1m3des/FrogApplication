package at.ac.univie.frog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.w3c.dom.Text;

/**
 * Created by Andy on 03.05.16.
 */
public class QRGenerate extends AsyncTask<String, Void, Void> {

    TextView textView;
    ImageView imageView;
    Context context;
    Bitmap bmp;
    String code;

    public QRGenerate(Context context, TextView textView, ImageView imageView, String code) {
        this.context = context;
        this.textView = textView;
        this.imageView = imageView;
        this.code = code;
    }

    @Override
    protected Void doInBackground(String... params) {

        try {
            this.bmp = encode(code);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(Void aVoid) {

        if (bmp != null) imageView.setImageBitmap(bmp);
        else {
            imageView.setBackgroundColor(Color.BLACK);
        }
    }


    public Bitmap encode(String code) {

        Bitmap image = null;
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(code, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            image = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

        } catch (WriterException e) {
            e.printStackTrace();
            image =  null;
        }

        return image;
    }


}