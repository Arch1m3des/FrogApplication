package at.ac.univie.SplitDAO;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Daniel on 25.05.2016.
 */
public class CurrencyManager {
    Context context;

    public CurrencyManager(Context context){
        this.context=context;
    }

    public List<Double> getSpendingInHomeCurrency(List<Double> spending,String currency){
        List<Double> spendingsInHomeCurrency=new ArrayList<>();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        double rate=Double.parseDouble(sharedPrefs.getString(currency,""));

        for(double amount:spending){
            spendingsInHomeCurrency.add(amount/rate);
        }

        return spendingsInHomeCurrency;
    }
}
