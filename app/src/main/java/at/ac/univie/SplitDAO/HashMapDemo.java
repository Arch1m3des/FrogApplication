package at.ac.univie.SplitDAO;

import java.util.HashMap;

/**
 * Created by Andy on 21.05.16.
 */
public class HashMapDemo {
        // create hash map
        HashMap<Friend, Double> newmap = new HashMap();


    public boolean addparticipant(Friend friend) {
        // populate hash map
        newmap.put(friend, (double) 0);
        return true;
    }

    public double setspending(Friend friend, double amount) {
        return (double) newmap.put(friend, amount);
    }

    public double getspending(Friend friend) {

        return (double) newmap.get(friend);
    }

}