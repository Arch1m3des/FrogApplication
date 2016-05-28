package at.ac.univie.SplitDAO;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Andy on 13.05.16.
 */
public class SplitEqual extends Expense implements Serializable {
    public SplitEqual(Friend creator, Friend payer, double amount, String description) {
        super(creator, payer, amount, description);
    }

    @Override
    public boolean addparticipant(Friend friend) {
        super.addparticipant(friend);
        return calculatedebt();
    }

    @Override
    public boolean setparticipants(List<Friend> participants) {
        super.setparticipants(participants);
        inputfields.clear();
        return calculatedebt();
    }

    @Override
    public boolean setitem(Friend friend, double item) {
        return false;
    }

    @Override
    public double sumitems() {
        return (double) participants.size();
    }

    @Override
    public void optimizeinputs() {

    }

    @Override
    public HashMap<Friend, Double> getinput() {
        return null;
    }


    @Override
    public boolean calculatedebt() {
        ArrayList<Double> debts = new ArrayList<Double>();

        try {
            ListIterator<Friend> friend = participants.listIterator();

            while (friend.hasNext()) {
                debts.add(amount/(participants.size()));
                friend.next();

            }

            setSpending(debts);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}


