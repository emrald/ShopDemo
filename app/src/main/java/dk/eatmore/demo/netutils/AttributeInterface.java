package dk.eatmore.demo.netutils;


import android.widget.ListView;
import android.widget.TextView;

import dk.eatmore.demo.model.AttributeCartAddingModel;
import dk.eatmore.demo.model.ProductAttributeValue;

import java.util.ArrayList;

/**
 * Created by ADMIN on 09-12-2016.
 */

public interface AttributeInterface {
    void attributeClick(int position, ListView expandableListView, String tm_id,
                        AttributeCartAddingModel attributeCartAddingModel, TextView ectratopping_ind, int parentPos, ArrayList<ProductAttributeValue> productAttributeLists);
}
