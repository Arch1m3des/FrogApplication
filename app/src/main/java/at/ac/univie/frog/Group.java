package at.ac.univie.frog;

import java.util.ArrayList;

/**
 * Created by tamara on 28.05.16.
 */

public class Group {

    private String name;
    private ArrayList<String> items;

    public Group(String name, ArrayList<String> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
            this.name = name;
        }

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> Items) {
        this.items = Items;
    }

}
