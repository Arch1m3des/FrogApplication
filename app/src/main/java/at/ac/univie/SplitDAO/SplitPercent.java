package at.ac.univie.SplitDAO;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import java.io.Serializable;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Andy on 19.05.16.
 */

public class SplitPercent extends Expense implements Serializable {


    public SplitPercent(Friend payer, double amount, String description, String category, int splitOption) {
        super(payer, amount, description, category, splitOption);
        inputFields.put(payer, (double) 100.0);
    }

    public boolean setitem(Friend friend, double percent) {
        if (inputFields.containsKey(friend))  {
            inputFields.put(friend, percent);
            calculateDebt();
            return true;
        }
        else return false;
    }
    
    public double sumitems () {
        double sum = 0;
        int zeroes = 0;
        if(!participants.isEmpty()) {


            for (Friend friend : participants) {
                if (inputFields.get(friend) != 0) {
                    sum += inputFields.get(friend);
                } else zeroes++;
            }
        }

        return sum;
    }

    public void optimizeinputs() {
        if(sumitems()<100.0 && inputFields.containsValue((double) 0)) {
            inputFields.put(getFriendfromKey((double) 0), 100.0-sumitems());
        }
        else if(sumitems()>100.0) {
            //convert to relative
            double rel = (((sumitems()-100.0))/participants.size());
            for (Friend friend: participants) {
                double relamnt = inputFields.get(friend);
                if(relamnt < rel) {
                    inputFields.put(friend, 0.0);
                    relamnt = 0;
                    optimizeinputs();
                    break;
                }
                if(relamnt == 0) continue;

                inputFields.put(friend, (relamnt-rel));
            }

        }

    }

    public HashMap<Friend, Double> getinput() {
        return inputFields;
    }

    private Friend getFriendfromKey(Double value) {
        for (Map.Entry<Friend, Double> entry : inputFields.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }


    @Override
    public boolean setParticipants(List<Friend> participants) {
        if(super.setParticipants(participants)) {
            //TODO change get old percentage and update it with size
            inputFields.clear();
            for (Friend friend : participants) {
                inputFields.put(friend, 0.0);
            }
            return true;
        }
        else return false;
    }

    @Override
    public boolean addParticipant(Friend friend) {
        if(super.addParticipant(friend)) {
            inputFields.put(friend, 0.0);
            return true;
        }
        else return false;
    }


    public boolean calculateDebt() {
        try {
            ArrayList<Double> debts = new ArrayList<Double>();

            for (Friend friend :participants) {
                debts.add(inputFields.get(friend)*amount/100.0);
            }

            setSpending(debts);
            return true;
        } catch (Exception e) {
            return false;
        }



    }


}
