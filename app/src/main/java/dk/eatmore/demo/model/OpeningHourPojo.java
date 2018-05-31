package dk.eatmore.demo.model;

/**
 * Created by ADMIN on 04-07-2016.
 */
public class OpeningHourPojo {
    public OpeningHourPojo(String openingDays, String openingTime, Boolean openingFlag) {
        this.openingDays = openingDays;
        this.openingTime = openingTime;
        this.openingFlag = openingFlag;
    }

    String openingDays,openingTime;
    Boolean openingFlag;

    public String getOpeningDays() {
        return openingDays;
    }

    public void setOpeningDays(String openingDays) {
        this.openingDays = openingDays;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public Boolean getOpeningFlag() {
        return openingFlag;
    }

    public void setOpeningFlag(Boolean openingFlag) {
        this.openingFlag = openingFlag;
    }
}
