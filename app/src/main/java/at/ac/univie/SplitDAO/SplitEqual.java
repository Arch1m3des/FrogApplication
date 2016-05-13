package at.ac.univie.SplitDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 13.05.16.
 */
public class SplitEqual extends Expense implements IExpensecalculations {
    SplitEqual(Friend creator, Friend payer, double amount, String description, Category category) {
        super(creator, payer, amount, description, category);
    }


    @Override
    public List<Double> calculatedebt(Friend payer, List<Friend> participants, double amount) throws Exception {
        ArrayList<Double> debts = new ArrayList<Double>(participants.size());

        int payerindex;
        if (participants.contains(payer)) {
            payerindex = participants.indexOf(payer);

            debts.set(payerindex, (double) 0);
        }
        else throw new Exception("Payer is not participant ");




        return null;
    }
}
