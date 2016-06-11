package at.ac.univie.SplitDAO;

import android.content.Context;
import android.location.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Andy on 21.05.16.
 */
public class SplitParts extends Expense implements Serializable{

    public SplitParts(Friend payer, double amount, String description, String category, int splitOption) {
        super(payer, amount, description, category, splitOption);
        inputFields.put(payer, (double) 1);
    }

    public boolean setitem(Friend friend, double part) {
        if (inputFields.containsKey(friend) && participants.contains(friend)) {
            part =  (double) ((int) part);
            inputFields.put(friend, part);
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
        if(inputFields.containsValue((double) 0)) {
            while (inputFields.containsValue(0.0))
                inputFields.put(getFriendfromKey((double) 0), (double) 1);
        }
        else if(sumitems()>100) {
            double rel = (sumitems()/10);
            for (Friend friend: participants) {
                double relamnt = inputFields.get(friend);
                if(relamnt < rel) {
                    inputFields.put(friend, 1.0);
                    relamnt = 1;
                }
                if(relamnt == 1) continue;
                inputFields.put(friend, (rel/10));
            }

        }
        calculateDebt();

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
            //TODO change get old partspending and update it with size
            inputFields.clear();
            for (Friend friend : participants) {
                inputFields.put(friend, 1.0);
            }
            return true;
        }
        else return false;
    }

    @Override
    public boolean addParticipant(Friend friend) {
        if(super.addParticipant(friend)) {
            inputFields.put(friend, (double) 1);
            return true;
        }
        else return false;
    }


    public boolean calculateDebt() {
        try {
            ArrayList<Double> debts = new ArrayList<Double>();

            int totalparts = (int) sumitems();
            for (Friend friend :participants) {
                double parts = inputFields.get(friend);
                debts.add(parts/totalparts*amount);
            }

            setSpending(debts);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
