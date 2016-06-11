package com.eeccs.jimmy.iorderserve;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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
import com.eeccs.jimmy.iorderserve.tool.ApplicationContext;
import com.eeccs.jimmy.iorderserve.tool.CallBack;
import com.eeccs.jimmy.iorderserve.tool.CallBackContent;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener{
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

    @Override
    public void onResume() {
        super.onResume();
        populateList();
    }

    private void initView() {
        mOrderListAdapter = new OrderListAdapter(MainActivity.this, mOrderItems);
        mListViewOrder = (ListView) findViewById(R.id.list_order);
        mListViewOrder.setAdapter(mOrderListAdapter);
        mListViewOrder.setOnItemClickListener(this);
    }

    private void populateList() {
        mOrderListAdapter.getData().clear();
        Log.i(TAG, "Initializing ListView....." + mOrderListAdapter.getData().size());
        ApplicationContext.show_all_order(1, new CallBack() {
            @Override
            public void done(CallBackContent content) {
                if (content != null) {
                    for (int i = 0; i < content.getShow_order().size(); i++) {
                        mOrderItems.add(content.getShow_order().get(i));
                        if (mOrderItems.size() == content.getShow_order().size())
                            mOrderListAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e(TAG, "show_child_by_id fail" + "\n");
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OrderItem object = mOrderListAdapter.getData().get(position);
        final String oid = object.getOid();
        Bundle bundle = new Bundle();
        bundle.putString(ApplicationContext.ORDER_ID, oid);
        Log.e(TAG,position+"\n");
        Log.e(TAG, oid + "\n");
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}
