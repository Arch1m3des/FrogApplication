package at.ac.univie.SplitDAO;

import android.util.Log;

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
public class SplitPercent extends Expense {


    public SplitPercent(Friend creator, Friend payer, double amount, String description) {
        super(creator, payer, amount, description);
        inputfields.put(payer, (double) 100.0);
    }

    public boolean setitem(Friend friend, double percent) {
        if (inputfields.containsKey(friend))  {
            inputfields.put(friend, percent);
            calculatedebt();
            return true;
        }
        else return false;
    }
    
    public double sumitems () {
        double sum = 0;
        int zeroes = 0;
        if(!participants.isEmpty()) {


            for (Friend friend : participants) {
                if (inputfields.get(friend) != 0) {
                    sum += inputfields.get(friend);
                } else zeroes++;
            }
        }

        return sum;
    }

    public void optimizeinputs() {
        if(sumitems()<100.0 && inputfields.containsValue((double) 0)) {
            inputfields.put(getFriendfromKey((double) 0), 100.0-sumitems());
        }
        else if(sumitems()>100.0) {
            //convert to relative
            double rel = (((sumitems()-100.0))/participants.size());
            for (Friend friend: participants) {
                double relamnt = inputfields.get(friend);
                if(relamnt < rel) {
                    inputfields.put(friend, 0.0);
                    relamnt = 0;
                    optimizeinputs();
                    break;
                }
                if(relamnt == 0) continue;

                inputfields.put(friend, (relamnt-rel));
            }

        }

    }

    public HashMap<Friend, Double> getinput() {
        return inputfields;
    }

    private Friend getFriendfromKey(Double value) {
        for (Map.Entry<Friend, Double> entry : inputfields.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }


    @Override
    public boolean setparticipants(List<Friend> participants) {
        if(super.setparticipants(participants)) {
            //TODO change get old percentage and update it with size
            inputfields.clear();
            for (Friend friend : participants) {
                inputfields.put(friend, 0.0);
            }
            return true;
        }
        else return false;
    }

    @Override
    public boolean addparticipant(Friend friend) {
        if(super.addparticipant(friend)) {
            inputfields.put(friend, 0.0);
            return true;
        }
        else return false;
    }


    public boolean calculatedebt() {
        try {
            ArrayList<Double> debts = new ArrayList<Double>();

            for (Friend friend :participants) {
                debts.add(inputfields.get(friend)*amount/100.0);
            }

            setSpending(debts);
            return true;
        } catch (Exception e) {
            return false;
        }



    }


}
