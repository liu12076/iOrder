package com.eeccs.jimmy.iorderserve;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2016/5/29.
 */
public class OrderItem {
    private String oid;
    private String customer;
    private String pickup_location;
    private String pickup_time;
    private int start_flag;
    private int end_flag;
    private List<OrderDetailItem> mOrderDetialList = new ArrayList<OrderDetailItem>();

    public OrderItem(String oid, String customer,String pickup_location, String pickup_time, int start_flag, int end_flag) {
        this.oid = oid;
        this.customer = customer;
        this.pickup_location = pickup_location;
        this.pickup_time = pickup_time;
        this.start_flag = start_flag;
        this.end_flag = end_flag;
    }


    public void addDetailItem(OrderDetailItem detial) {
        this.mOrderDetialList.add(detial);
    }

    public void start_delivering()
    {
        this.start_flag = 1;
    }

    public void finish_delivering()
    {
        this.end_flag = 1;
    }

    public String getOid()
    {
        return this.oid;
    }

    public String getCustomer()
    {
        return this.customer;
    }

    public String getPickup_location()
    {
        return this.pickup_location;
    }

    public String getPickup_time()
    {
        return this.pickup_time;
    }

    public int getStart_flag()
    {
        return this.start_flag;
    }

    public int getEnd_flag()
    {
        return this.end_flag;
    }

    public List<OrderDetailItem> getmOrderDetialList()
    {
        return this.mOrderDetialList;
    }

}
