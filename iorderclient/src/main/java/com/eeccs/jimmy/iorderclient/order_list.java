package com.eeccs.jimmy.iorderclient;

import android.os.Bundle;
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
    Button status_btn;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myorder_list);
        title = (TextView)findViewById(R.id.myorder_title);
        add_btn = (ImageButton)findViewById(R.id.add_btn);
        status_btn = (Button)findViewById(R.id.status_btn);
        list = (ListView)findViewById(R.id.list);
    }
}
