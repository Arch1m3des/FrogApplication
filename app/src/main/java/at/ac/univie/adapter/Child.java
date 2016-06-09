package at.ac.univie.adapter;

/**
 * Created by tamara on 28.05.16.
 */

public class Child {

    private String name;
    private boolean selected;
    private int id;

    public Child(String name) {
        this.name = name;
        selected = false;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


}
