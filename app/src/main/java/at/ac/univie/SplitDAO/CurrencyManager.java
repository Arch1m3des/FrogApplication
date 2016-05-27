package at.ac.univie.SplitDAO;

import android.content.Context;

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
    private HashMap<String,Double> currencyRates=new HashMap<>();

    /*
    public HashMap<String, Double> getCurrencyRates() {
        return currencyRates;
    }*/

    public void setCurrencyRates(HashMap<String, Double> currencyRates) {
        this.currencyRates = currencyRates;
    }

    public List<Double> getSpendingInHomeCurrency(List<Double> spending,String currency){
        List<Double> spendingsInHomeCurrency=new ArrayList<>();
        double rate=currencyRates.get(currency);

        for(double amount:spending){
            spendingsInHomeCurrency.add(amount/rate);
        }

        return spendingsInHomeCurrency;
    }
}
