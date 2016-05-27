package at.ac.univie.SplitDAO;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Andy on 25.05.16.
 */
public class GroupManager {

    public List<Group> groupList;

    public GroupManager() {
        groupList = new ArrayList<>();

    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public boolean setGroupList(List<Group> groupList) {
        try {
            this.groupList = groupList;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public boolean saveGroupData(Context context, String key) throws IOException {
        FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        try {
            oos.writeObject(this.groupList);
            oos.close();
            fos.close();
            return true;
        } catch (IOException e) {
            throw new IOException("Error GroupDAO: Error writing file! \n " + e.getMessage());
        } finally { try { oos.close(); return false; } catch ( Exception e ) { e.printStackTrace(); } }

    }



    public boolean loadGroupData(Context context, String key) throws IOException, ClassNotFoundException {
        try {
            FileInputStream fis = context.openFileInput(key);
            ObjectInputStream ois = new ObjectInputStream(fis);
            List<Group> object = (List<Group>) ois.readObject();
            this.groupList = object;
            return true;
        } catch (IOException e) {
            throw new IOException("Error GroupDAO: Error reading file! \n " + e.getMessage());
        }
    }

    public boolean addGroup(Group newgroup) {
        if(!groupList.contains(newgroup)) {
            groupList.add(newgroup);
            return true;
        }
        else return false;
    }

    public boolean removeGroup(Group newgroup) {
        if(groupList.contains(newgroup)) {
            groupList.add(newgroup);
            return true;
        }
        else return false;
    }



}
