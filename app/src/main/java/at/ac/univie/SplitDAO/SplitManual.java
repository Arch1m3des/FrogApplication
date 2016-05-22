package at.ac.univie.SplitDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Andy on 21.05.16.
 */
public class SplitManual extends Expense{

    public SplitManual(Friend creator, Friend payer, double amount, String description) {
        super(creator, payer, amount, description);
        inputfields.put(payer, (double) 0);
    }

    public boolean setitem(Friend friend, double manual) {
        if (inputfields.containsKey(friend) && manual<=sumitems() && sumitems()<amount)  {
            inputfields.put(friend, manual);
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
        if(sumitems()<amount && inputfields.containsValue((double) 0)) {
            inputfields.put(getFriendfromKey((double) 0), amount-sumitems());
        }
        else if(sumitems()>amount) {
            //convert to relative
            double rel = (((sumitems()-amount))/participants.size());
            for (Friend friend: participants) {
                double relamnt = inputfields.get(friend);
                if(relamnt < rel) {
                    inputfields.put(friend, 0.0);
                    relamnt = 0;
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
            //TODO change get old manualspending and update it with size
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
