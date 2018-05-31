package dk.eatmore.demo.model;

/**
 * Created by ADMIN on 23-09-2016.
 */
public class ProductAttributeValue {
    String padId,pamId,tmId,attrubuteName,attributePrice;

    public String getPadId() {
        return padId;
    }

    public void setPadId(String padId) {
        this.padId = padId;
    }

    public String getPamId() {
        return pamId;
    }

    public void setPamId(String pamId) {
        this.pamId = pamId;
    }

    public String getTmId() {
        return tmId;
    }

    public void setTmId(String tmId) {
        this.tmId = tmId;
    }

    public String getAttrubuteName() {
        return attrubuteName;
    }

    @Override
    public String toString() {
        return "ProductAttributeValue{" +
                "padId='" + padId + '\'' +
                ", pamId='" + pamId + '\'' +
                ", tmId='" + tmId + '\'' +
                ", attrubuteName='" + attrubuteName + '\'' +
                ", attributePrice='" + attributePrice + '\'' +
                '}';
    }

    public void setAttrubuteName(String attrubuteName) {
        this.attrubuteName = attrubuteName;
    }

    public String getAttributePrice() {
        return attributePrice;
    }

    public void setAttributePrice(String attributePrice) {
        this.attributePrice = attributePrice;
    }
}

