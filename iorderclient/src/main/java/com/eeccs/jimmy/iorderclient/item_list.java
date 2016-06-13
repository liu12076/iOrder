package com.eeccs.jimmy.iorderclient;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sherry on 2016/6/13.
 */
public class item_list extends add_list{
    TextView tv_orderinfo,tv_item,tv_num,tv_price,tv_total;
    EditText et_item,et_num,et_price;
    ImageButton btn_add;
    ListView list_menu;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    private int total_cost = 0;
    private int num = 0;
    private int price = 0;
    //EdidText et_orderinfo,et_item,et_num,et_prive,et_total;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_comfirm);
        tv_orderinfo = (TextView)findViewById(R.id.tv_orderinfo);
        tv_item = (TextView)findViewById(R.id.tv_item);
        tv_num = (TextView)findViewById(R.id.tv_num);
        tv_price = (TextView)findViewById(R.id.tv_price);
        et_item = (EditText)findViewById(R.id.et_item);
        et_num = (EditText)findViewById(R.id.et_num);
        et_price = (EditText)findViewById(R.id.et_price);
        btn_add = (ImageButton)findViewById(R.id.btn_add);
        list_menu = (ListView)findViewById(R.id.list_menu);
        listItems = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listItems);
        list_menu.setAdapter(adapter);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = 0;
                price = 0;
                Log.e("TAG",et_num.getText().toString());
                num = Integer.parseInt(et_num.getText().toString());
                price = Integer.parseInt(et_price.getText().toString());
                listItems.add( et_item.getText().toString() +"  "+ et_num.getText().toString() + "  " + et_price.getText().toString());
                adapter.notifyDataSetChanged();//動態更新
                total_cost = total_cost + num * price;
                Log.d("status","total_cost = "+ Integer.toString(total_cost));
                et_item.setText("");
                et_num.setText("");
                et_price.setText("");

            }
        });
        list_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

}
