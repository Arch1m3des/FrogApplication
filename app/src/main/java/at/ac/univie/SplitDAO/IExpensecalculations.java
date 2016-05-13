package at.ac.univie.SplitDAO;

import java.util.List;

/**
 * Created by Andy on 13.05.16.
 */
public interface IExpensecalculations {

    public List<Double> calculatedebt(Friend payer, List<Friend> participants, double amount) throws Exception;
}
