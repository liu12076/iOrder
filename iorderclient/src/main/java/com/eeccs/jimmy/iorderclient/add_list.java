package com.eeccs.jimmy.iorderclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eeccs.jimmy.iorderclient.tool.ApplicationContext;
import com.eeccs.jimmy.iorderclient.tool.CallBack;
import com.eeccs.jimmy.iorderclient.tool.CallBackContent;

/**
 * Created by Sherry on 2016/6/12.
 */
    public class add_list extends order_list{
        TextView tv_store_name,tv_orderer,tv_pickup_location,tv_pickup_time;
        EditText et_store_name,et_orderer,et_pickup_location,et_pickup_time;
        Button btn_startorder;
        private int store_id=1;
        private String oid, customer, pickup_location, pickup_time;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.add_item);
            tv_store_name = (TextView) findViewById(R.id.tv_store_name);
            tv_orderer = (TextView) findViewById(R.id.tv_orderer);
            tv_pickup_location = (TextView) findViewById(R.id.tv_pickup_location);
            tv_pickup_time = (TextView) findViewById(R.id.tv_pickup_time);
            et_store_name = (EditText) findViewById(R.id.et_store_name);
            et_orderer = (EditText) findViewById(R.id.et_orderer);
            et_pickup_location = (EditText) findViewById(R.id.et_pickup_location);
            et_pickup_time = (EditText) findViewById(R.id.et_pickup_time);
            btn_startorder =(Button) findViewById(R.id.btn_startorder);
            btn_startorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    Bundle bundle = intent.getExtras();
                    //oid =  bundle.getString(ApplicationContext.ORDER_ID);
                    store_id = Integer.parseInt(et_store_name.getText().toString());
                    customer = et_orderer.getText().toString();
                    pickup_location = et_pickup_location.getText().toString();
                    pickup_time = et_pickup_time.getText().toString();
                    ApplicationContext.insert_all_order(store_id,customer, pickup_location, pickup_time, new CallBack(){
                        @Override
                        public void done(CallBackContent content) {
                            Log.d("status","insert orderinfo success !");
                        }
                    });
                    Intent it = new Intent();
                    it.setClass(add_list.this, item_list.class);
                    startActivity(it);
                }
            });
         }




}
