package com.eeccs.jimmy.iorderserve;

/**
 * Created by User on 2016/5/29.
 */
public class OrderDetailItem {
    public String good;
    public String amount;
    public String total_cost;

    public OrderDetailItem(String good, String amount, String total_cost) {
        this.good = good;
        this.amount = amount;
        this.total_cost = total_cost;
    }

    public String getGood()
    {
        return this.good;
    }

    public void setGood(String good)
    {
        this.good = good;
    }

    public String getAmount()
    {
        return this.amount;
    }

    public void setAmount(String place)
    {
        this.amount = amount;
    }

    public String getTotal_cost()
    {
        return this.total_cost;
    }

    public void setTotal_cost(String total_cost)
    {
        this.total_cost = total_cost;
    }
}
