package com.eeccs.jimmy.iorderclient;
import android.app.Activity;
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
import java.util.List;

/**
 * Created by Sherry on 2016/6/10.
 */
public class order_list extends Activity /*implements AdapterView.OnItemClickListener*/ {

    TextView title;
    ImageButton add_btn;
    public static DeliveryListAdapter mDeliveryListAdapter;
    public static List<DeliveryItem> mDeliveryItems = new ArrayList<DeliveryItem>();
    private final String TAG = MainActivity.class.getSimpleName();
    private ListView mListViewOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myorder_list);
        title = (TextView) findViewById(R.id.myorder_title);
        add_btn = (ImageButton) findViewById(R.id.add_btn);
        mDeliveryListAdapter = new DeliveryListAdapter(order_list.this, mDeliveryItems);
        mListViewOrder = (ListView) findViewById(R.id.order_list);
        mListViewOrder.setAdapter(mDeliveryListAdapter);
        //mListViewOrder.setOnItemClickListener(this);

       /* add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(order_list.this, add_list.class);
                //requestCode(識別碼) 型別為 int ,從B傳回來的物件將會有一樣的requestCode
                //startActivityForResult(intent, 100);
                //order_list.this.finish();
            }
        });*/
    }

    private void populateList() {
        mDeliveryListAdapter.getData().clear();
        Log.i(TAG, "Initializing ListView....." + mDeliveryListAdapter.getData().size());

        ApplicationContext.show_all_order(1, new CallBack() {
            @Override
            public void done(CallBackContent content) {
                if (content != null) {

                    for (int i = 0; i < content.getShow_order().size(); i++) {
                        DeliveryItem object = content.getShow_order().get(i);
                        mDeliveryItems.add(object);
                        if (mDeliveryItems.size() == content.getShow_order().size())
                            mDeliveryListAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e(TAG, "show_all_order fail" + "\n");
                }
            }
        });
    }
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(RESULT_OK){//resultCode是剛剛妳A切換到B時設的resultCode
            case RESULT_OK://當B傳回來的Intent的requestCode 等於當初A傳出去的話
                //String result = data.getExtras().getString("name");
                String customer = data.getExtras().getString("customer");
                Log.d("status","order_list get customer : "+customer);
                int store_id = data.getExtras().getInt("store_id");
                Log.d("status","order_list get store_id : "+store_id);
                mDeliveryListAdapter.notifyDataSetChanged();
                //Button btn = new Button(this);
                break;
        }

    }*/
    @Override
    protected void onResume() {
        super.onResume();
        populateList();
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
/*
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("TAG", "listview clicked");
    }*/
}
