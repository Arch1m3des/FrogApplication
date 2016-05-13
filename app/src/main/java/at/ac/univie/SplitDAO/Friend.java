package at.ac.univie.SplitDAO;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Andy on 03.05.16.
 */
public class Friend implements Serializable {
    int FriendID;
    String name;
    String surname;
    UUID uniqueid;
    String mailaddress;
    //setlater = UUID.randomUUID();


    public Friend(int friendID, String surname, String name, String mailaddress) {
        FriendID = friendID;
        this.setUniqueid();
        this.surname = surname;
        this.name = name;
        this.mailaddress =  mailaddress;
    }

    public void setUniqueid() {
        this.uniqueid = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return "Friend{" +
                "FriendID=" + FriendID +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", uniqueid=" + uniqueid +
                '}';
    }

    public int getFriendID() {
        return FriendID;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public UUID getUniqueid() {
        return uniqueid;
    }
}
