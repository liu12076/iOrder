package com.eeccs.jimmy.iorderserve.tool;

import com.eeccs.jimmy.iorderserve.OrderDetailItem;
import com.eeccs.jimmy.iorderserve.OrderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2016/6/11.
 */
public class CallBackContent {
    List<OrderItem> show_order = new ArrayList<OrderItem>();
    List<OrderDetailItem> show_order_content = new ArrayList<OrderDetailItem>();

    public List<OrderItem> getShow_order() {
        return this.show_order;
    }
    public List<OrderDetailItem> getShow_order_content ()
    {
        return this.show_order_content;
    }
}
