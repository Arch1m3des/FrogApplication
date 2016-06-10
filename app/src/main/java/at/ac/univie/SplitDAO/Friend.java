package at.ac.univie.SplitDAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Andy on 03.05.16.
 */

public class Friend implements Serializable {

    int FriendID;
    private String name;
    private String surname;
    private String initials;
    private UUID uniqueId;
    private String mailaddress;
    private String iconColor;
    //setlater = UUID.randomUUID();


    public Friend(int friendID, String surname, String name, String mailaddress) {
        this.FriendID = friendID;
        this.setUniqueid();
        this.surname = surname;
        this.name = name;
        this.initials = name.substring(0,1).toUpperCase() + surname.substring(0,1).toUpperCase();
        this.mailaddress =  mailaddress;
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

    public void setUniqueid() {
        this.uniqueId = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return "Friend{" +
                "FriendID=" + FriendID +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", uniqueid=" + uniqueId + '\'' +
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
        return uniqueId;
    }

    public String getInitials() { return initials; }

    public String getIconColor()  { return iconColor; }

    public String getMailaddress() {return mailaddress;  }
}
