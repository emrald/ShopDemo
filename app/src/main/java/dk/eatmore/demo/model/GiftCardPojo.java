package dk.eatmore.demo.model;

/**
 * Created by ADMIN on 27-10-2016.
 */
public class GiftCardPojo {
    public String getGiftType() {
        return giftType;
    }

    public void setGiftType(String giftType) {
        this.giftType = giftType;
    }

    private String giftCardCode;
    private String giftExpDate;
    private String giftvalue;
    private String giftSaldo;
    private String giftType;
    private String balance;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getGiftCardCode() {
        return giftCardCode;
    }

    public void setGiftCardCode(String giftCardCode) {
        this.giftCardCode = giftCardCode;
    }

    public String getGiftExpDate() {
        return giftExpDate;
    }

    public void setGiftExpDate(String giftExpDate) {
        this.giftExpDate = giftExpDate;
    }

    public String getGiftvalue() {
        return giftvalue;
    }

    public void setGiftvalue(String giftvalue) {
        this.giftvalue = giftvalue;
    }

    public String getGiftSaldo() {
        return giftSaldo;
    }

    public void setGiftSaldo(String giftSaldo) {
        this.giftSaldo = giftSaldo;
    }
}
