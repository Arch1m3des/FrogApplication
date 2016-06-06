package at.ac.univie.SplitDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Andy on 01.06.16.
 */
public class Currencies {
    private static HashMap<String, String> currencies;

    //TODO maybe no constructor needed
    public Currencies(HashMap<String, String> currencies) {
        currencies = new HashMap();

        currencies.put("EUR","€");
        currencies.put("AUD","$");
        currencies.put("BRL","R$");
        currencies.put("CAD","R$");
        currencies.put("CHF","CHF");
        currencies.put("CNY","¥");
        currencies.put("CZK","Kč");
        currencies.put("DKK","kr");
        currencies.put("GBP","£");
        currencies.put("HKD","$");
        currencies.put("HRK","kn");
        currencies.put("HUF","Ft");
        currencies.put("IDR","Rp");
        currencies.put("ILS","₪");
        currencies.put("INR","Rp");
        currencies.put("JPY","¥");
        currencies.put("KRW","₩");
        currencies.put("MXN","$");
        currencies.put("MYR","RM");
        currencies.put("NOK","kr");
        currencies.put("NZD","$");
        currencies.put("PHP","₱");
        currencies.put("PLN","zł");
        currencies.put("RON","lei");
        currencies.put("RUB","руб");
        currencies.put("SEK","kr");
        currencies.put("SGD","$");
        currencies.put("THB","฿");
        currencies.put("TRY","Lira");
        currencies.put("USD","$");
        currencies.put("ZAR","R");
    }

    public HashMap<String, String> getCurrencies() {
        return currencies;
    }

    public void howToApply() {
        Iterator it = currencies.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + "  " + pair.getValue());
            //it.remove(); //try it it works without
        }

    }

}
