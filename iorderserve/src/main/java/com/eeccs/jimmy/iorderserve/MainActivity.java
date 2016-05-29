package com.eeccs.jimmy.iorderserve;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.eeccs.jimmy.iorderserve.OrderItem;
import com.eeccs.jimmy.iorderserve.OrderListAdapter;
import com.eeccs.jimmy.iorderserve.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener{
    public static final String ORDER_DETAIL_NUM = "ORDER_DETAIL_NUM";
    public static final String ORDER_DETAIL_GOOD = "ORDER_DETAIL_GOOD";
    public static final String ORDER_DETAIL_AMOUNT = "ORDER_DETAIL_AMOUNT";
    public static OrderListAdapter mOrderListAdapter;
    public static List<OrderItem> mOrderItems = new ArrayList<OrderItem>();
    private final String TAG = MainActivity.class.getSimpleName();
    private ListView mListViewOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mOrderListAdapter = new OrderListAdapter(MainActivity.this, mOrderItems);
        mListViewOrder = (ListView) findViewById(R.id.list_order);
        mListViewOrder.setAdapter(mOrderListAdapter);
        mListViewOrder.setOnItemClickListener(this);
        populateList();
    }

    private void populateList() {
        mOrderListAdapter.getData().clear();
        Log.i(TAG, "Initializing ListView....." + mOrderListAdapter.getData().size());
        for(int i=1; i<5; i++)
        {
            OrderItem object = new OrderItem(String.valueOf(i)+" name",String.valueOf(i)+" place",String.valueOf(i));
            for(int j=1; j<i+1; j++)
            {
                OrderDetailItem detail = new OrderDetailItem(String.valueOf(j)+"good",String.valueOf(j));
                object.addDetailItem(detail);
            }
            mOrderItems.add(object);
        }
        Log.i(TAG, "Initialized ListView....." + mOrderListAdapter.getData().size());
        mOrderListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OrderItem object = mOrderListAdapter.getData().get(position);
        final String oid = object.getId();
        Bundle bundle = new Bundle();

        int num_order_detail = object.getmOrderDetialList().size();
        if(num_order_detail != 0)
        {
            bundle.putInt(MainActivity.ORDER_DETAIL_NUM,num_order_detail);
            for (int i = 0; i < num_order_detail; i++) {
                bundle.putString(MainActivity.ORDER_DETAIL_GOOD + i, object.getmOrderDetialList().get(i).getGood());
                bundle.putString(MainActivity.ORDER_DETAIL_AMOUNT + i, object.getmOrderDetialList().get(i).getAmount());
            }
        }
        Log.e(TAG,position+"\n");
        Log.e(TAG,num_order_detail+"\n");
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
