package at.ac.univie.SplitDAO;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Daniel on 24.05.2016.
 */
public class CurrencyChanger extends AsyncTask<String,Void,JSONObject>{
    //API Abfrage des Waehrungskurses, die Basis ist Euro und ausgegeben werden soll der Kurs der uebergebenen Waehrung
    /*
        Supported Currencies from API:
        "AUD"
        "BGN"
        "BRL"
        "CAD"
        "CHF"
        "CNY"
        "CZK"
        "DKK"
        "EUR"
        "GBP"
        "HKD"
        "HRK"
        "HUF"
        "IDR"
        "ILS"
        "INR"
        "JPY"
        "KRW"
        "MXN"
        "MYR"
        "NOK"
        "NZD"
        "PHP"
        "PLN"
        "RON"
        "RUB"
        "SEK"
        "SGD"
        "THB"
        "TRY"
        "USD"
        "ZAR"
     */
    private static final String API_URL="http://api.fixer.io/latest";
    private static final String[] currencies={"AUD","BGN","BRL","CAD","CHF","CNY","CZK","DKK","GBP","HKD","HRK","HUF","IDR","ILS","INR","JPY","KRW","MXN","MYR","NOK","NZD","PHP","PLN","RON","RUB","SEK","SGD","THB","TRY","USD","ZAR"};
    Context context;

    public CurrencyChanger(Context context) {
        this.context = context;
    }

    @Override
    //Beim Aufruf muss als erstes Element die Fremdwaehrung und als zweites Element der Betrag uebergeben werden
    protected JSONObject doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        URL url = null;
        JSONObject object = null;
        InputStream inStream = null;

        try {
            //Zusammenfuegen des api-Strings und spezifizieren der URL Connection
            url = new URL(API_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            inStream = urlConnection.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
            String temp, response = "";
            while ((temp = bReader.readLine()) != null) {
                response += temp;
            }
            object = (JSONObject) new JSONTokener(response).nextValue();
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
        } finally {
            if (inStream != null) {
                try {
                    // this will close the bReader as well
                    inStream.close();
                } catch (IOException ignored) {
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return object;
    }

    protected void onPostExecute(JSONObject result){
        double rate=0.0;

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=sharedPrefs.edit();

        try{
            JSONObject rateResult=result.getJSONObject("rates");

            for(String currency:currencies) {
                rate = rateResult.getDouble(currency);
                editor.putString(currency,""+rate);
            }

            editor.apply();
        }catch (JSONException e) {
            Log.e("ERROR", e.getMessage(), e);
        }


    }
}
