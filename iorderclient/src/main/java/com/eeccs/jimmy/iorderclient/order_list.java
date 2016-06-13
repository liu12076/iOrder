package com.eeccs.jimmy.iorderclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Sherry on 2016/6/10.
 */
public class order_list extends MainActivity{
    TextView title;
    ImageButton add_btn;
    //Button status_btn;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myorder_list);
        title = (TextView)findViewById(R.id.myorder_title);
        add_btn = (ImageButton)findViewById(R.id.add_btn);
        //status_btn = (Button)findViewById(R.id.status_btn);
        list = (ListView)findViewById(R.id.list);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(order_list.this, add_list.class);
               // startActivity(intent);
                //order_list.this.finish();
            }
        });
    }
}
