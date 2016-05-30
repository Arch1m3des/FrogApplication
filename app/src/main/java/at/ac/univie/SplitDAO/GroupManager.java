package at.ac.univie.SplitDAO;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Andy on 25.05.16.
 */
public class GroupManager implements Serializable {

    public ArrayList<Group> groupList;
    String filename;

    public GroupManager() {
        groupList = new ArrayList<>();
        filename = new String("Groups");


    }

    public ArrayList<Group> getGroupList() {
        return groupList;
    }

    public boolean setGroupList(ArrayList<Group> groupList) {
        try {
            this.groupList = groupList;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void save(){


        Log.d("Save", "saving object");
        try{

            File userInfo = new File(filename);
            if(!userInfo.isFile() && !userInfo.exists()) {
                userInfo.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(userInfo);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(groupList);
            objectOutputStream.close();
            Log.d("Save", "object saved");
        }catch(FileNotFoundException e){
            Log.e("Save", e.getMessage());
        }catch(IOException e){
            Log.e("Save", e.getMessage());
        }
    }
    public boolean load(){
        Log.d("Load", "loading object");
        Object object = null;
        try{
            FileInputStream fileInputStream = new FileInputStream(filename);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            groupList = (ArrayList<Group>) objectInputStream.readObject();
            objectInputStream.close();
            Log.d("Load", "object loaded");
            return true;
        }catch(FileNotFoundException e){
            Log.e("Load", e.getMessage());
        }catch(Exception e){
            Log.e("Load", e.getMessage());
        }
        return false;
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
            ArrayList<Group> object = (ArrayList<Group>) ois.readObject();
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
