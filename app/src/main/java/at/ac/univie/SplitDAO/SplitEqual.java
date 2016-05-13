package at.ac.univie.SplitDAO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Andy on 13.05.16.
 */
public class SplitEqual extends Expense implements IExpensecalculations {
    public SplitEqual(Friend creator, Friend payer, double amount, String description, Category category) {
        super(creator, payer, amount, description);
    }

    @Override
    public List<Double> calculatedebt(Friend payer, List<Friend> participants, double amount) throws Exception {
        ArrayList<Double> debts = new ArrayList<Double>(participants.size());

        int payerindex;
        if (participants.contains(payer)) {
            payerindex = participants.indexOf(payer);

            debts.set(payerindex, (double) -amount/(participants.size()+1));
        }
        else throw new Exception("Payer is not participant ");

        ListIterator<Friend> friend = participants.listIterator();

        while (friend.hasNext()) {
            debts.set(friend.nextIndex(), amount/(participants.size()+1));

        }

        return debts;
    }
}
