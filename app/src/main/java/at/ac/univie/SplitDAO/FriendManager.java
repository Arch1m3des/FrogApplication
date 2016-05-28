package at.ac.univie.SplitDAO;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 21.05.16.
 */
public class FriendManager {


    public ArrayList<Friend> friendList;

    public FriendManager() {
        friendList = new ArrayList<>();

    }

    public ArrayList<Friend> getFriendList() {
        return friendList;
    }

    public boolean setFriendList(ArrayList<Friend> friendList) {
        try {
            this.friendList = friendList;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public boolean saveFriendData(Context context, String key) throws IOException {
        FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        try {
            oos.writeObject(this.friendList);
            oos.close();
            fos.close();
            return true;
        } catch (IOException e) {
            throw new IOException("Error FriendDAO: Error writing file! \n " + e.getMessage());
        } finally { try { oos.close(); return false; } catch ( Exception e ) { e.printStackTrace(); } }

    }



    public boolean loadFriendData(Context context, String key) throws IOException, ClassNotFoundException {
        try {
            FileInputStream fis = context.openFileInput(key);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<Friend> object = (ArrayList<Friend>) ois.readObject();
            this.friendList = object;
            return true;
        } catch (IOException e) {
            throw new IOException("Error FriendDAO: Error reading file! \n " + e.getMessage());
        }
    }

    public boolean addFriend(Friend newfriend) {
        if(!friendList.contains(newfriend)) {
            friendList.add(newfriend);
            return true;
        }
        else return false;
    }

    public boolean removeFriend(Friend newfriend) {
        if(friendList.contains(newfriend)) {
            friendList.add(newfriend);
            return true;
        }
        else return false;
    }


}
