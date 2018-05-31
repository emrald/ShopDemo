package dk.eatmore.demo.model;

/**
 * Created by ADMIN on 10-11-2016.
 */

public class ExtraToppingCartAddingModel {
    private String tsgd_id;
    private String tsg_id;
    private String id;
    private boolean isSelected;
    private String name;
    private String tm_id;
    private String price;
    private String pad_id;

    public String getPad_id() {
        return pad_id;
    }

    public void setPad_id(String pad_id) {
        this.pad_id = pad_id;
    }

    @Override
    public String toString() {
        return "ExtraToppingCartAddingModel{" +
                "tsgd_id='" + tsgd_id + '\'' +
                ", tsg_id='" + tsg_id + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

    public String getTsgd_id() {
        return tsgd_id;
    }

    public void setTsgd_id(String tsgd_id) {
        this.tsgd_id = tsgd_id;
    }

    public String getTsg_id() {
        return tsg_id;
    }

    public void setTsg_id(String tsg_id) {
        this.tsg_id = tsg_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public String getTm_id() {
        return tm_id;
    }

    public void setTm_id(String tm_id) {
        this.tm_id = tm_id;
    }

}


