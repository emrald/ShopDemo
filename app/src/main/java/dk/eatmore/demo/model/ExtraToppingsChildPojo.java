package dk.eatmore.demo.model;

/**
 * Created by pallavi.b on 07-Apr-16.
 */
public class ExtraToppingsChildPojo {

    private String Name;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    boolean selected = false;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

}

