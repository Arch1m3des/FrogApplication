package at.ac.univie.frog;

import org.junit.Test;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import at.ac.univie.SplitDAO.Expense;
import at.ac.univie.SplitDAO.Friend;
import at.ac.univie.SplitDAO.Group;
import at.ac.univie.SplitDAO.SplitEqual;
import at.ac.univie.SplitDAO.SplitParts;
import at.ac.univie.SplitDAO.SplitPercent;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
        Friend max =  new Friend(1,"Glett", "Max", "ich@du.com");
        Friend andy =  new Friend(2,"Weinbahn", "Andy", "ich@du.com");
        Friend max2 =  new Friend(3,"Dreibahn", "Andy", "ich@du.com");
        Friend max3 =  new Friend(4,"Vierbah", "Andy", "ich@du.com");

        //System.out.print(max.toString());

        Group thailand = new Group(1, "Thailand");
        thailand.addMember(andy);
        thailand.addMember(max);
        thailand.addMember(max2);
        thailand.addMember(max3);


        Expense firstexpense = new SplitEqual(max ,max, 26, "Erstes Essen nach dem Krankenhaus");
        firstexpense.addparticipant(andy);

        Expense menu = new SplitParts(max, max, 30, "Eis essen bei Tichy");
        menu.addparticipant(andy);
        menu.setitem(andy, 3);


        thailand.addExpense(firstexpense);
        thailand.addExpense(menu);

        double balance = thailand.calculatebalance(max);
        double owes = thailand.calculateowes(andy, max);

        System.out.println("Balance: " + balance);

        System.out.println("Owes by max: " + owes);




        System.out.println("Adding first expense");
        System.out.println(firstexpense.isparticipant(andy));

        List<Double> debt;

       if (firstexpense.calculatedebt()) {
           debt = firstexpense.getSpending();

           int index= 0;
           for (Double x : debt) {
               System.out.println(firstexpense.getParticipants().get(index++).getName() + "\t\t" + x + " €");
           }
       }

      /*
        Expense secondexpense = new SplitPercent(max ,max, 111, "Zweites Essen nach dem Krankenhaus");
        secondexpense.addparticipant(andy);
        secondexpense.addparticipant(max2);

        for (Friend friend: secondexpense.getParticipants()) {
            secondexpense.setitem(friend, 100.0/4.0);
        }
        secondexpense.addparticipant(max3);

        System.out.print("Percent:  ");
        System.out.println(secondexpense.sumitems() + "%");

        secondexpense.optimizeinputs();
        System.out.println(secondexpense.sumitems() + "%");

        secondexpense.setitem(max, 40.0);
        secondexpense.setitem(max2, 50.0);
        secondexpense.setitem(max3, 1.0);
        System.out.println("vorher:" + secondexpense.sumitems() + "%");

        if (secondexpense.calculatedebt()) {
            debt = secondexpense.getSpending();

            Iterator it = secondexpense.getinput().values().iterator();
            DecimalFormat df = new DecimalFormat("0.00");
            double sum = 0;
            for (Double x : debt) {
                sum+= x;
                System.out.println(df.format(x) + " €\t" + it.next() + "%");
            }
            System.out.println("Summe: " + sum);
        }


        secondexpense.optimizeinputs();
        System.out.println("nachher:  " + secondexpense.sumitems() + "%");


        if (secondexpense.calculatedebt()) {
            debt = secondexpense.getSpending();

            Iterator it = secondexpense.getinput().values().iterator();
            DecimalFormat df = new DecimalFormat("0.00");
            double sum = 0;
            for (Double x : debt) {
                sum+= x;
                System.out.println(df.format(x) + " €\t" + it.next() + "%");
            }
            System.out.println("Summe: " + sum);
        }

*/


/*

        HashMapDemo bla =  new HashMapDemo();
        bla.addparticipant(max);
        bla.addparticipant(max2);
        bla.setspending(max, 2343.434);
        System.out.print(bla.getspending(max));
*/

/*

        System.out.println("\n\nDritte Ausgabe: ");

        Expense thirdexpense = new SplitParts(max ,max, 111.56, "Drittes Essen nach dem Krankenhaus");
        thirdexpense.addparticipant(andy);
        thirdexpense.addparticipant(max2);

        thirdexpense.addparticipant(max3);

        System.out.print("Total Parts:  ");
        System.out.println(thirdexpense.sumitems() + "Parts");

        //thirdexpense.optimizepartspending();
        System.out.println(thirdexpense.sumitems() + "Parts");

        thirdexpense.setitem(max, 0);
        thirdexpense.setitem(andy,1);
        thirdexpense.setitem(max2, 200);
        thirdexpense.setitem(max3, 3);
        thirdexpense.optimizeinputs();
        System.out.println("Anzahl: :" + thirdexpense.sumitems() + "Parts");

        if (thirdexpense.calculatedebt()) {
            debt = thirdexpense.getSpending();

            Iterator it = thirdexpense.getinput().values().iterator();
            DecimalFormat df = new DecimalFormat("0.00");
            double sum = 0;
            int i =0;
            for (Double x : debt) {
                sum+= x;
                System.out.println(df.format(x) + " €  \t" + i++ + "Parts");
            }
            System.out.println("Summe: " + sum);
        }


*/










    }
}