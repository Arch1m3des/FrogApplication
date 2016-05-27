package at.ac.univie.SplitDAO;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

/**
 * gadsfjlksdlfk
 * Created by Andy on 12.05.16.
 */
public abstract class Expense implements IExpensecalculations {
    GregorianCalendar curr_date;
    Friend creator;
    double amount;
    String description;

    enum Category { travelling, food, drinks, fun, moneytransfer, medical , misc } //maybe more
    Category category;

    //TODO currency missing
    String currency="USD";
    //TODO location missing

    HashMap<Friend, Double> inputfields = new HashMap();
    List<Friend> participants = new ArrayList<>();
    List<Double> spending = new ArrayList<>();
    List<Double> spendingInHomeCurrency = new ArrayList<>();
    Friend payer;


    Expense (Friend creator, Friend payer, double amount, String description) {
        this.creator = creator;
        setpayer(payer);
        setamount(amount);
        setdescription(description);
        //setcategory(category);

        participants.add(payer);
    }

    public void setpayer(Friend payer) {
        this.payer = payer;
    }

    public void setSpending(List<Double> spending) {
        this.spending = spending;
    }

    public void setamount(double amount) {
        this.amount = amount;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    public List<Double> getSpending() {
        return spending;
    }

    public void setcategory(Category category) {
        this.category = category;
    }

    public boolean addparticipant(Friend friend) {
        if (friend!=null && participants.isEmpty()) {
            participants.add(friend);
            return true;
        }
        else if (friend != null && !participants.contains(friend)) {
            participants.add(friend);
            return true;
        }
        else return false;
    }

    public boolean setparticipants(List<Friend> participants) {
        try {
            this.participants = participants;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public double getAmount() {
        return amount;
    }

    public Friend getPayer() {
        return payer;
    }

    public List<Friend> getParticipants() {
        return participants;
    }

    abstract public boolean setitem(Friend friend, double part);

    abstract public double sumitems();

    abstract public void optimizeinputs();

    abstract public HashMap<Friend, Double> getinput();

    public boolean modifyparticipants(List<Friend> participants) {

        //update();
        if (this.participants.isEmpty()) {
            this.participants = participants;
            return true;
        }
        else {
            try {
            for (Friend friend: participants) {
                if(this.participants.contains(friend)) continue;
                else {
                    this.participants.add(friend);
                }
            }
            return true;

            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        }
    }

    public double getSpendingbyIndex(int index)  {
        try {
            return spending.get(index);
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }

    public boolean isparticipant(Friend friend) {
        return this.participants.contains(friend);
    }


    //Function calculates the spendings in the home currency
    public boolean calculateDebtInHomeSpendings(){
        CurrencyManager cm=new CurrencyManager();

        spendingInHomeCurrency=cm.getSpendingInHomeCurrency(spending,currency);

        return true;
    }

    public List<Double> getSpendingInHomeCurrency() {
        return spendingInHomeCurrency;
    }
}
