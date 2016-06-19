package com.eeccs.jimmy.iorderclient.tool;

import com.eeccs.jimmy.iorderclient.DeliveryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2016/6/13.
 */
public class CallBackContent {
    String result;
    String oid;
    List<DeliveryItem> show_order = new ArrayList<DeliveryItem>();
    double lat;
    double lng;
    public String getResult()
    {
        return this.result;
    }
    public String getOid()
    {
        return this.oid;
    }
    public List<DeliveryItem> getShow_order() {
        return this.show_order;
    }
    public double getLat()
    {
        return this.lat;
    }

    public double getLng()
    {
        return this.lng;
    }
}
