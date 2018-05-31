package dk.eatmore.demo.model;

import java.util.ArrayList;

/**
 * Created by ADMIN on 09-09-2016.
 */
public class menuPojo {
    String menuName;
ArrayList<MenuItemPojo> menuItem;

    public ArrayList<MenuItemPojo> getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(ArrayList<MenuItemPojo> menuItem) {
        this.menuItem = menuItem;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}
