package at.ac.univie.SplitDAO;

import android.content.Context;
import android.location.Location;
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
    public SplitEqual(Friend payer, double amount, String description, String category, int splitOption) {
        super(payer, amount, description, category,splitOption);
    }

    @Override
    public boolean addParticipant(Friend friend) {
        super.addParticipant(friend);
        return calculateDebt();
    }

    @Override
    public boolean setParticipants(List<Friend> participants) {
        super.setParticipants(participants);
        inputFields.clear();
        return calculateDebt();
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
    public boolean calculateDebt() {
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


