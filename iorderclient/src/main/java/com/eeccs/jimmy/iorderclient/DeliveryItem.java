package com.eeccs.jimmy.iorderclient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sherry on 2016/6/17.
 */
public class DeliveryItem {
    private String oid;
    private String store_name;
    private String customer;
    private String pickup_location;
    private String pickup_time;
    private String total_cost;
    private int start_flag;
    private int end_flag;
    private List<DeliveryDetailItem> mDeliveryDetialList = new ArrayList<DeliveryDetailItem>();
    public DeliveryItem(String oid, String store_name, String customer,String pickup_location, String pickup_time, int start_flag, int end_flag) {
        this.oid = oid;
        this.store_name = store_name;
        this.customer = customer;
        this.pickup_location = pickup_location;
        this.pickup_time = pickup_time;
        this.start_flag = start_flag;
        this.end_flag = end_flag;

    }

    public void addDetailItem(DeliveryDetailItem detial) {
        this.mDeliveryDetialList.add(detial);
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
    public String getStoreName()
    {
        return this.store_name;
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


    public List<DeliveryDetailItem> getmDeliveryDetialList()
    {
        return this.mDeliveryDetialList;
    }


}
