package at.ac.univie.SplitDAO;

import android.content.Context;
import android.location.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 *
 * Created by Andy on 12.05.16.
 */

public abstract class Expense implements IExpensecalculations, Serializable {

    Date date;
    Friend creator;
    double amount;
    double amountInHomeCurrency;
    String description;
    Context context;
    Location location;
    String category;
    String currency="USD";
    int splitOption;

    HashMap<Friend, Double> inputFields = new HashMap();
    List<Friend> participants = new ArrayList();
    List<Double> spending = new ArrayList<>();
    List<Double> spendingInHomeCurrency = new ArrayList<>();
    Friend payer;

    public Expense(Context context){
        this.context=context;
    }

    public Expense (Friend creator, Friend payer, double amount, String description, String category, int splitOption) {
        this.creator = creator;
        date = new Date();
        setPayer(payer);
        setAmount(amount);
        setDescription(description);
        this.category = category;
        this.splitOption = splitOption;
        participants.add(payer);
    }

    public void setPayer(Friend payer) {
        this.payer = payer;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setSpending(List<Double> spending) {
        this.spending = spending;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Double> getSpending() {
        return spending;
    }

    public boolean addParticipant(Friend friend) {
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

    public boolean setParticipants(List<Friend> participants) {
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

    public boolean modifyParticipants(List<Friend> participants) {

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

    public double getSpendingByIndex(int index)  {
        try {
            return spending.get(index);
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }

    public int getParticipantIndex(Friend friend) {
        for (int i=0; i<participants.size(); i++) {
            if (participants.get(i).getFriendID() == friend.getFriendID()) {
                return i;
            }
        }
        return 0;
    }

    public double getSpendinginHomeCurrencybyIndex(int index)  {
        try {
            return spendingInHomeCurrency.get(index);
        } catch (IndexOutOfBoundsException e) {
            return 0;
        }
    }

    public boolean isParticipant(Friend friend) {
        boolean participated = false;
        for (Friend temp : this.participants) {
            if (temp.getFriendID() == friend.getFriendID()) {
                participated = true;
                break;
            }
        }
        return participated;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public void setAmountinHomeCurrency(double amountinhomecurr){
        this.amountInHomeCurrency = amountinhomecurr;
    }



    //Function calculates the spendings in the home currency
    public boolean calculateDebtInHomeSpendings(){
        CurrencyManager cm=new CurrencyManager(context);

        spendingInHomeCurrency=cm.getSpendingInHomeCurrency(spending,currency);
        amountInHomeCurrency = cm.getAmountinHomeCurrency(amount, currency);
        return true;
    }

    //Function returning the spendings in hom Currency
    public List<Double> getSpendingInHomeCurrency() {
        return spendingInHomeCurrency;
    }
}
