package at.ac.univie.SplitDAO;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Daniel on 25.05.2016.
 */
public class CurrencyManager {
    Context context;

    public CurrencyManager(Context context){
        this.context=context;
    }

    public List<Double> getSpendingInHomeCurrency(List<Double> spending,String currency) {
        List<Double> spendingsInHomeCurrency=new ArrayList<>();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        double rate = 1;
        try {
            rate = Double.parseDouble(sharedPrefs.getString(currency,"").toString());
        } catch (NumberFormatException e) {
            rate = 1;
        }

        for(double amount:spending){
            spendingsInHomeCurrency.add(amount/rate);
        }

        return spendingsInHomeCurrency;
    }

    public double getAmountinHomeCurrency(double amount,String currency){
        double amountInHomeCurrency = 0;

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        double rate;


        try {
            rate = Double.parseDouble(sharedPrefs.getString(currency,""));
        } catch (NumberFormatException e) {
            rate = 1;
        }
        amountInHomeCurrency = amount/rate;


        return amountInHomeCurrency;
    }


}
