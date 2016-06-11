package at.ac.univie.SplitDAO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Andy on 01.06.16.
 */
public class Currencies {
    private static HashMap<String, String> currencies;
    private static HashMap<String, String> currenciesCountries;


    //TODO maybe no constructor needed
    public Currencies() {
        currencies = new HashMap();
        currenciesCountries = new HashMap();

        currenciesCountries.put("EUR","€ - Europe");
        currenciesCountries.put("AUD","$ - Australia");
        currenciesCountries.put("BRL","R$ - Brazil");
        currenciesCountries.put("CAD","R$ - Canada");
        currenciesCountries.put("CHF","CHF - Switzerland");
        currenciesCountries.put("CNY","¥ - China");
        currenciesCountries.put("CZK","Kč - Chech");
        currenciesCountries.put("DKK","kr - Denmark");
        currenciesCountries.put("GBP","£ - Great Britian");
        currenciesCountries.put("HKD","$ - Hong Kong");
        currenciesCountries.put("HRK","kn - Croatia");
        currenciesCountries.put("HUF","Ft - Hungary");
        currenciesCountries.put("IDR","Rp - India");
        currenciesCountries.put("ILS","₪ - Palestine");
        currenciesCountries.put("INR","Rp - India");
        currenciesCountries.put("JPY","¥ - Japan");
        currenciesCountries.put("KRW","₩ - Korea");
        currenciesCountries.put("MXN","$ - Mexico");
        currenciesCountries.put("MYR","RM - Malaysia");
        currenciesCountries.put("NOK","kr - Norway");
        currenciesCountries.put("PHP","₱ - Phillipines");
        currenciesCountries.put("PLN","zł - Poland");
        currenciesCountries.put("RON","lei - Latvia");
        currenciesCountries.put("RUB","руб - Russia");
        currenciesCountries.put("SEK","kr - Israel");
        currenciesCountries.put("SGD","$ - Singapore");
        currenciesCountries.put("THB","฿ - Thailand");
        currenciesCountries.put("TRY","Lir - Turkey");
        currenciesCountries.put("USD","$ - USA");
        currenciesCountries.put("ZAR","R - South Africa");

        currencies.put("EUR","€");
        currencies.put("AUD","$");
        currencies.put("BRL","R$ ");
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

    public HashMap<String, String> getCurrenciesCountries() {
        return currenciesCountries;
    }

    //quick sort hashmap http://beginnersbook.com/2013/12/how-to-sort-hashmap-in-java-by-keys-and-values/
    public static HashMap<String,String> sortedCurrencies() {
        List list = new LinkedList(currencies.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        HashMap sortedMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public static HashMap<String,String> sortedCurrenciesCountries() {
        List list = new LinkedList(currenciesCountries.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        HashMap sortedMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }


    //example
    /*
    public void howToApply() {
        Iterator it = currencies.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + "  " + pair.getValue());
            //it.remove(); //try it it works without
        }

    }
    */



}
