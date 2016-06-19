package com.eeccs.jimmy.iorderclient;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.eeccs.jimmy.iorderclient.tool.ApplicationContext;
import com.eeccs.jimmy.iorderclient.tool.CallBack;
import com.eeccs.jimmy.iorderclient.tool.CallBackContent;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Sherry on 2016/6/10.
 */
public class order_list extends MainActivity{
    TextView title;
    ImageButton add_btn;
    //Button status_btn;
    ListView list;
    ArrayList<String> listOids;
    ArrayList<String> listSids;
    ArrayList<String> listLocations;
    ArrayList<String> listStartFlags;
    DeliveryListAdapter deliveryListAdapter;
    private Timer timer = new Timer();
    private final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myorder_list);
        title = (TextView)findViewById(R.id.myorder_title);
        add_btn = (ImageButton)findViewById(R.id.add_btn);
        //status_btn = (Button)findViewById(R.id.status_btn);
        list = (ListView)findViewById(R.id.order_list);
        listOids = new ArrayList<String>();
        listSids = new ArrayList<String>();
        listLocations = new ArrayList<String>();
        listStartFlags = new ArrayList<String>();
        deliveryListAdapter = new DeliveryListAdapter(order_list.this,listOids,listSids,listLocations,listStartFlags);
        list.setAdapter(deliveryListAdapter);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(order_list.this, add_list.class);
                startActivity(intent);
            }
        });

/*
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(order_list.this, MapsActivity.class);
                intent.putExtra("order_id", listItems.get(position));
                startActivity(intent);
            }
        });*/
    }
    private void populateList() {
        //adapter.getData().clear();
        //Log.i(TAG, "Initializing ListView....." + mDeliveryListAdapter.getData().size());

        ApplicationContext.show_all_order(1, new CallBack() {
            @Override
            public void done(CallBackContent content) {
                if (content != null) {
                    listOids.clear();
                    listSids.clear();
                    listLocations.clear();
                    listStartFlags.clear();
                    for (int i = 0; i < content.getShow_order().size(); i++) {
                        //DeliveryItem object = content.getShow_order().get(i);
                        listOids.add(content.getShow_order().get(i).getOid());
                        listSids.add("1");
                        listLocations.add(content.getShow_order().get(i).getPickup_location());
                        listStartFlags.add(String.valueOf(content.getShow_order().get(i).getStart_flag()));
                        if ( listOids.size() == content.getShow_order().size())
                            deliveryListAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e(TAG, "show_all_order fail" + "\n");
                }
            }
        });
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(RESULT_OK){//resultCode是剛剛妳A切換到B時設的resultCode
            case RESULT_OK://當B傳回來的Intent的requestCode 等於當初A傳出去的話
                //String result = data.getExtras().getString("name");
                String customer = data.getExtras().getString("customer");
                Log.d("status","order_list get customer : "+customer);
                int store_id = data.getExtras().getInt("store_id");
                Log.d("status","order_list get store_id : "+store_id);
                listItems.add(customer + "     " + store_id);
                adapter.notifyDataSetChanged();
                Button btn = new Button(this);
                break;
        }
    }*/
    @Override
    protected void onResume() {
        super.onResume();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                populateList();
            }
        }, 0, 1 * 10 * 1000);//10 sec
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}




