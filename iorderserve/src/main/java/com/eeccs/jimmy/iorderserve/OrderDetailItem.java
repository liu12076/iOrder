package com.eeccs.jimmy.iorderserve;

/**
 * Created by User on 2016/5/29.
 */
public class OrderDetailItem {
    public String good;
    public String amount;

    public OrderDetailItem(String good, String amount) {
        this.good = good;
        this.amount = amount;
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

}
