package at.ac.univie.SplitDAO;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Andy on 12.05.16.
 */
public class Group {
    String groupname;
    GregorianCalendar startDate;
    List<Friend> members;
    List<Expense> expenses;

    public Group(String groupname, List<Friend> members) {
        this.groupname = groupname;
        this.members = members;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public void setStartDate(GregorianCalendar startDate) {
        this.startDate = (GregorianCalendar) GregorianCalendar.getInstance();
    }


    public String getGroupname() {
        return groupname;
    }

    public List<Friend> getMembers() {
        return members;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }


    //Methods
    public void addmember(Friend friend) throws Exception {
        if (friend != null && !members.contains(friend)) {
            members.add(friend);
        }
        throw new Exception("Member already in list");
    }

    public double calculateowes(Friend friend) {

        double calculatedexpenses = 0;

        return calculatedexpenses;
    }
}
