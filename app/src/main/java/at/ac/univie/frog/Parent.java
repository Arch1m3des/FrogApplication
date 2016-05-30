package at.ac.univie.frog;

import java.util.ArrayList;

/**
 * Created by tamara on 28.05.16.
 */

public class Parent {

    private String name;
    private ArrayList<Child> items;

    public Parent(String name, ArrayList<Child> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
            this.name = name;
        }

    public ArrayList<Child> getItems() {
        return items;
    }

    public void setItems(ArrayList<Child> Items) {
        this.items = Items;
    }

}
