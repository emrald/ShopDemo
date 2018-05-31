package dk.eatmore.demo.model;

/**
 * Created by pallavi.b on 05-Apr-16.
 */
public class MyAccountPojo {

    public String title;
    public String number;
    public int imageIcon;
    public int imageNext;

    public MyAccountPojo(String title, String number, int imageIcon, int imageNext) {
        this.title = title;
        this.imageIcon = imageIcon;
        this.imageNext = imageNext;
        this.number = number;
    }
}


