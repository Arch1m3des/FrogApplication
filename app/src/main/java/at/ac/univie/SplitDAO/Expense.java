package at.ac.univie.SplitDAO;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Andy on 12.05.16.
 */
<<<<<<< HEAD
class Expense {
/*    GregorianCalendar curr_date;
=======
public class Expense {
    GregorianCalendar curr_date;
>>>>>>> ecd37de17d5750ab3581d4852994315e4316d486
    Friend creator;
    double amount;
    String description;
    enum Category { travelling, food, drinks, fun, moneytransfer, medical , misc } //maybe more
    Category category;


    List<Friend> participants;
    List<Double> spending;
    Friend payer;

    Expense (Friend creator, Friend payer, double amount, String description) {
        this.creator = creator;
        setpayer(payer);
        setamount(amount);
        setdescription(description);
        //setcategory(category);

    }

    public void setpayer(Friend payer) {
        this.payer = payer;
    }

    public void setamount(double amount) {
        this.amount = amount;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    public void setcategory(Category category) {
        this.category = category;
    }

    public boolean addparticipant(Friend friend) throws Exception {
        if (friend != null && !participants.contains(friend)) {
            participants.add(friend);
        }
        throw new Exception("Member already in list");
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

    public boolean modifyparticipants(List<Friend> participants) {
<<<<<<< HEAD
        this.participants
        //pseudocode
    	////
    	wenn participants nicht gesetzt, dann überschreiben
    	Wenn neue participants gleiche anzahl haben, dann überschreiben
    	wenn anzahl unterschiedlich, spending array zwischenspeichern. beträge die nicht auf neues participants
    	array passen, auf payer aufsummieren

		danach immer update der spendings. Spendingsgröße muss angepasst werden.
    	////

        //update();
=======
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
>>>>>>> ecd37de17d5750ab3581d4852994315e4316d486
    }

    public boolean isparticipant(Friend friend) {
        return this.participants.contains(friend);
    }







*/
}
