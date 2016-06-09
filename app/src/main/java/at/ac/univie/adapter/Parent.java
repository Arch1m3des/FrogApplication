package at.ac.univie.adapter;

import java.util.ArrayList;

import at.ac.univie.adapter.Child;

/**
 * Created by tamara on 28.05.16.
 */

public class Parent {

    private String name;
    private ArrayList<Child> items;
    int size;

    public Parent(String name, ArrayList<Child> items) {
        this.name = name;
        this.items = items;
        this.size = items.size();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
            this.name = name;
        }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<Child> getItems() {
        return items;
    }

    public void setItems(ArrayList<Child> Items) {
        this.items = Items;
    }

}
