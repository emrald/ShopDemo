package dk.eatmore.demo.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ExtraToppeningCategory {

    private String pad_id;
    public ArrayList<ExtraToppeningCategory> children;
    public ArrayList<String> selection;
    public String name, price, id;

    public String tsgd_id, tsg_id, tm_id;

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

    @Override
    public String toString() {
        return "ExtraToppeningCategory{" +
                "children=" + children +
                ", selection=" + selection +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", id='" + id + '\'' +
                ", tsgd_id='" + tsgd_id + '\'' +
                ", tsg_id='" + tsg_id + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ExtraToppeningCategory() {
        children = new ArrayList<ExtraToppeningCategory>();
        selection = new ArrayList<String>();
    }

    public ExtraToppeningCategory(String id, String name, String price, String tsgd_id, String tsg_id, String mtm_id) {
        this();
        this.id = id;
        this.name = name;
        this.price = price;
        this.tsgd_id = tsgd_id;
        this.tsg_id = tsg_id;
        this.tm_id = mtm_id;
    }

    public ExtraToppeningCategory(String id, String name, String price, String tsgd_id, String tsg_id, String mtm_id, String pad_id) {
        this();
        this.id = id;
        this.name = name;
        this.price = price;
        this.tsgd_id = tsgd_id;
        this.tsg_id = tsg_id;
        this.tm_id = mtm_id;
        this.pad_id = pad_id;
    }

    public ExtraToppeningCategory(String name, String mId) {
        this();
        this.name = name;
        this.id = mId;
    }

    private void generateChildren1(JSONArray subMenuItemArray, String tm_id, String pad_id) {

        int submenudetCounterSize = subMenuItemArray.length();

        Log.e("Aniket", "pad_id" + pad_id);
        Log.e("Aniket", "" + subMenuItemArray);


        Log.e("tm_id", "tm_id" + tm_id);

        for (int submenudetCounter = 0; submenudetCounter < submenudetCounterSize; submenudetCounter++) {
            JSONObject subMenuDetailsObj = null;
            try {
                subMenuDetailsObj = subMenuItemArray.getJSONObject(submenudetCounter);

                String sunMenuId = subMenuDetailsObj.getString("i_id");
                String subMenuPrice = subMenuDetailsObj.getString("t_price");
                String subMenuName = subMenuDetailsObj.getString("i_name");
                String tsgd_id = subMenuDetailsObj.getString("tsgd_id");
                String tsg_id = subMenuDetailsObj.getString("tsg_id");
                if (subMenuDetailsObj.has("pad_id"))
                    pad_id = subMenuDetailsObj.getString("pad_id");

//                ExtraToppeningCategory cat = new ExtraToppeningCategory(sunMenuId, subMenuName, subMenuPrice, tsgd_id, tsg_id, tm_id);
                ExtraToppeningCategory cat = new ExtraToppeningCategory(sunMenuId, subMenuName, subMenuPrice, tsgd_id, tsg_id, tm_id, pad_id);
                this.children.add(cat);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("child ", "child " + e);
            }
        }
    }

//    private void generateChildren1(JSONArray subMenuItemArray, String tm_id) {
//
//        int submenudetCounterSize = subMenuItemArray.length();
//
//        Log.e("Aniket", "" + subMenuItemArray);
//
//
//        Log.e("tm_id", "tm_id" + tm_id);
//
//        for (int submenudetCounter = 0; submenudetCounter < submenudetCounterSize; submenudetCounter++) {
//            JSONObject subMenuDetailsObj = null;
//            try {
//                subMenuDetailsObj = subMenuItemArray.getJSONObject(submenudetCounter);
//
//                String sunMenuId = subMenuDetailsObj.getString("i_id");
//                String subMenuPrice = subMenuDetailsObj.getString("t_price");
//                String subMenuName = subMenuDetailsObj.getString("i_name");
//                String tsgd_id = subMenuDetailsObj.getString("tsgd_id");
//                String tsg_id = subMenuDetailsObj.getString("tsg_id");
//                String pad_id = "";
//                if (subMenuDetailsObj.has("pad_id"))
//                    pad_id = subMenuDetailsObj.getString("pad_id");
//
//                ExtraToppeningCategory cat = new ExtraToppeningCategory(sunMenuId, subMenuName, subMenuPrice, tsgd_id, tsg_id, tm_id);
//                this.children.add(cat);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Log.e("child ", "child " + e);
//            }
//        }
//    }

    public static ArrayList<ExtraToppeningCategory> getCategories(JSONArray jsonArray, String padId) {
        Log.e("subMenuItemArray", "subMenuItemArray" + jsonArray);

        ArrayList<ExtraToppeningCategory> categories = new ArrayList<ExtraToppeningCategory>();
        String toppingSubMenuName, toppingSubMenuId;
        try {
            int arraySize = jsonArray.length();
            for (int i = 0; i < arraySize; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //	String toppingMenuName= jsonObject.getString("tg_name");
                JSONArray subMenuArray = jsonObject.getJSONArray("topping_subgroup_list");
                int subMenuArrayListAarraySize = subMenuArray.length();
                for (int sunmenuCounter = 0; sunmenuCounter < subMenuArrayListAarraySize; sunmenuCounter++) {
                    JSONObject sunMenuObject = subMenuArray.getJSONObject(sunmenuCounter);

                    toppingSubMenuName = sunMenuObject.getString("tsg_name");
                    toppingSubMenuId = sunMenuObject.getString("tm_id");
                    ExtraToppeningCategory cat = new ExtraToppeningCategory(toppingSubMenuName, toppingSubMenuId);
                    cat.generateChildren1(sunMenuObject.getJSONArray("topping_subgroup_details"), toppingSubMenuId, padId);
                    categories.add(cat);

                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return categories;
    }

    public static ArrayList<ExtraToppeningCategory> getCategoriesWithJson(JSONObject jsonObject) {
        Log.e("subMenuItemArray", "subMenuItemArray" + jsonObject);

        ArrayList<ExtraToppeningCategory> categories = new ArrayList<ExtraToppeningCategory>();
        String toppingSubMenuName, toppingSubMenuId;
        try {
//			int arraySize=jsonArray.length();
//			for (int i = 0; i <arraySize ; i++) {
//				JSONObject jsonObject=jsonArray.getJSONObject(i);
            //	String toppingMenuName= jsonObject.getString("tg_name");
            JSONArray subMenuArray = jsonObject.getJSONArray("topping_subgroup_list");
            int subMenuArrayListAarraySize = subMenuArray.length();
            for (int sunmenuCounter = 0; sunmenuCounter < subMenuArrayListAarraySize; sunmenuCounter++) {
                JSONObject sunMenuObject = subMenuArray.getJSONObject(sunmenuCounter);

                toppingSubMenuName = sunMenuObject.getString("tsg_name");
                toppingSubMenuId = sunMenuObject.getString("tm_id");
                ExtraToppeningCategory cat = new ExtraToppeningCategory(toppingSubMenuName, toppingSubMenuId);
                cat.generateChildren1(sunMenuObject.getJSONArray("topping_subgroup_details"), toppingSubMenuId, "");
                categories.add(cat);

                //	}

            }

        } catch (Exception ex) {
            Log.e("parent ", "parent " + ex);
        }

        return categories;
    }

    public String getPad_id() {
        return pad_id;
    }

    public void setPad_id(String pad_id) {
        this.pad_id = pad_id;
    }
}