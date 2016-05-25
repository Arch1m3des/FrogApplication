package at.ac.univie.SplitDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Andy on 21.05.16.
 */
public class SplitParts extends Expense {

    public SplitParts(Friend creator, Friend payer, double amount, String description) {
        super(creator, payer, amount, description);
        inputfields.put(payer, (double) 1);
    }

    public boolean setitem(Friend friend, double part) {
        if (inputfields.containsKey(friend) && participants.contains(friend)) {
            part =  (double) ((int) part);
            inputfields.put(friend, part);
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
        if(inputfields.containsValue((double) 0)) {
            inputfields.put(getFriendfromKey((double) 0), (double) 1);
        }
        else if(sumitems()>100) {
            double rel = (sumitems()/10);
            for (Friend friend: participants) {
                double relamnt = inputfields.get(friend);
                if(relamnt < rel) {
                    inputfields.put(friend, 1.0);
                    relamnt = 1;
                }
                if(relamnt == 1) continue;
                inputfields.put(friend, (rel/10));
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
            //TODO change get old partspending and update it with size
            inputfields.clear();
            for (Friend friend : participants) {
                inputfields.put(friend, 1.0);
            }
            return true;
        }
        else return false;
    }

    @Override
    public boolean addparticipant(Friend friend) {
        if(super.addparticipant(friend)) {
            inputfields.put(friend, (double) 1);
            return true;
        }
        else return false;
    }


    public boolean calculatedebt() {
        try {
            ArrayList<Double> debts = new ArrayList<Double>();

            int totalparts = (int) sumitems();
            for (Friend friend :participants) {
                double parts = inputfields.get(friend);
                debts.add(parts/totalparts*amount);
            }

            setSpending(debts);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
