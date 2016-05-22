package at.ac.univie.SplitDAO;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by Andy on 12.05.16.
 */

public class Group {

    long id;
    String name;
    String iconColor;
    GregorianCalendar startDate;
    List<Friend> members;
    List<Expense> expenses;

    public Group(long id, String name) {
        this.id = id;
        this.name = name;
        this.members = new ArrayList();
        this.expenses = new ArrayList();
        this.iconColor = generateIconColor();
    }

    public String generateIconColor() {
        Random rand = new Random();

        switch (rand.nextInt(10)+1) {
            case 1 : return "#d9b3ff";
            //  break;
            case 2 : return "#ff99cc";
            // break;
            case 3 : return "#9999ff";
            //break;
            case 4 : return "#00cc99";
            //break;
            case 5 : return "#009999";
            //break;
            case 6 : return "#cc6699";
            // break;
            case 7 : return "#2eb8b8";
            // break;
            case 8 : return "#53c68c";
            //break;
            case 9 : return "#CC6699";
            //break;
            case 10 : return "#336699";
            //break;
            default : return "#FF0000";
        }
        //return rand.nextInt(10)+1;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public void setName(String groupname) {
        this.name = groupname;
    }

    public void setStartDate(GregorianCalendar startDate) {
        this.startDate = (GregorianCalendar) GregorianCalendar.getInstance();
    }


    public String getName() {
        return name;
    }

    public List<Friend> getMembers() {
        return members;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }


    //Methods
    public boolean addMember(Friend friend) throws Exception {
        if (friend != null && !members.contains(friend)) {
            members.add(friend);
            return true;
        }
        else return false;
    }

    public boolean addExpense(Expense newexpense) {
        return expenses.add(newexpense);
    }

    public double calculatebalance(Friend me) {
        double balance = 0;

        for (Expense expense: expenses) {
            double debt;
            if(expense.isparticipant(me)) {
                debt = 0;
                debt = expense.getSpendingbyIndex(expense.participants.indexOf(me));
                balance += debt;
            }

        }
        return balance;
    }

    public double calculateowes(Friend me, Friend friend) {
        double owes = 0;

        for (Expense expense: expenses) {
            if (expense.isparticipant(me) && expense.isparticipant(friend)) {
                int indexpayer;
                int indexdebtor;
                if (expense.getPayer() == me) {
                    owes += expense.getSpendingbyIndex(expense.participants.indexOf(friend));
                } if(expense.getPayer() == friend) {
                    owes -= expense.getSpendingbyIndex(expense.participants.indexOf(me));
                }
            }
        }

        return owes;
    }
}
