package at.ac.univie.frog;

/**
 * Created by tamara on 28.05.16.
 */

public class Child {

    private String name;
    private boolean selected;

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

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


}
