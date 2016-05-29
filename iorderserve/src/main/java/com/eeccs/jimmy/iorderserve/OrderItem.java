package com.eeccs.jimmy.iorderserve;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2016/5/29.
 */
public class OrderItem {
    private String id;
    private String name;
    private String place;
    private List<OrderDetailItem> mOrderDetialList = new ArrayList<OrderDetailItem>();

    public OrderItem(String name, String place,String id) {
        this.name = name;
        this.place = place;
        this.id = id;
    }

    public void addDetailItem(OrderDetailItem detial) {
        this.mOrderDetialList.add(detial);
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPlace()
    {
        return this.place;
    }

    public void setPlace(String place)
    {
        this.place = place;
    }

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public List<OrderDetailItem> getmOrderDetialList()
    {
        return this.mOrderDetialList;
    }

}
