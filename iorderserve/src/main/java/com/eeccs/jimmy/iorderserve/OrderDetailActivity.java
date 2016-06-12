package com.eeccs.jimmy.iorderserve;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.eeccs.jimmy.iorderserve.tool.ApplicationContext;
import com.eeccs.jimmy.iorderserve.tool.CallBack;
import com.eeccs.jimmy.iorderserve.tool.CallBackContent;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends Activity implements View.OnClickListener, LocationListener{

    public static OrderDetailListAdapter mOrderDetailListAdapter;
    public static List<OrderDetailItem> mOrderDetailItems = new ArrayList<OrderDetailItem>();
    private final String TAG = OrderDetailActivity.class.getSimpleName();
    private ListView mListViewOrderDetail;
    private Button btn_start_delivering;
    private Button btn_finish_delivering;
    private TextView text_total_cost;
    private String oid;
    private LocationManager locationMgr;
    private double lng;
    private double lat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            verifyFineLocationPermissions(this);
            verifyCoaseLocationPermissions(this);
        }
        this.locationMgr = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        initView();
    }

    private void initView() {
        mOrderDetailListAdapter = new OrderDetailListAdapter(OrderDetailActivity.this, mOrderDetailItems);
        mListViewOrderDetail = (ListView) findViewById(R.id.list_order_detail);
        mListViewOrderDetail.setAdapter(mOrderDetailListAdapter);
        btn_start_delivering = (Button)findViewById(R.id.button_start);
        btn_start_delivering.setOnClickListener(this);
        btn_finish_delivering = (Button)findViewById(R.id.button_finish);
        btn_finish_delivering.setOnClickListener(this);
        text_total_cost = (TextView)findViewById(R.id.text_total_cost);
        populateList();
    }

    private void populateList() {
        mOrderDetailListAdapter.getData().clear();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
       oid =  bundle.getString(ApplicationContext.ORDER_ID);
        ApplicationContext.show_content_by_id(oid, new CallBack() {
            @Override
            public void done(CallBackContent content) {
                if (content != null) {
                    text_total_cost.setText(content.getShow_order_content().get(0).getTotal_cost());
                    for (int i = 0; i < content.getShow_order_content().size(); i++) {
                        mOrderDetailItems.add(content.getShow_order_content().get(i));
                        if (mOrderDetailItems.size() == content.getShow_order_content().size())
                            mOrderDetailListAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e(TAG, "show_child_by_id fail" + "\n");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 取得位置提供者，不下條件，讓系統決定最適用者，true 表示生效的 provider
        String provider = this.locationMgr.getBestProvider(new Criteria(), true);
        if (provider == null) {
            Log.i(TAG,"沒有 location provider 可以使用");
            return;
        }
        Log.i(TAG,"取得 provider - " + provider);

        Log.i(TAG, "requestLocationUpdates...");
        // 註冊 listener，兩個 0 不適合在實際環境使用，太耗電
        this.locationMgr.requestLocationUpdates(provider, 0, 0, this);

        Log.i(TAG, "getLastKnownLocation...");
        Location location = this.locationMgr.getLastKnownLocation(provider);
        if (location == null) {
            Log.i(TAG, "未取過 location");
            return;
        }
        Log.i(TAG, "取得上次的 location");
        this.onLocationChanged(location);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.locationMgr.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        lng = location.getLongitude();
        lat = location.getLatitude();
        Log.e(TAG,"test lng : " +lng);
        Log.e(TAG,"test lat : " + lat);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // 當 location provider 改變時
    }

    @Override
    public void onProviderEnabled(String provider) {
        // 當 location provider 有效時
    }

    @Override
    public void onProviderDisabled(String provider) {
        // 當 location provider 無效時
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_start:
                ApplicationContext.notificationServiceStartBuilder(this);
                ApplicationContext.start_delivering(oid);
                break;
            case R.id.button_finish:
                ApplicationContext.cancelNotificationService(this);
                ApplicationContext.finish_delivering(oid);
                break;
        }
    }

    public static void verifyFineLocationPermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, ApplicationContext.PERMISSIONS[0]);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    ApplicationContext.PERMISSIONS,
                    ApplicationContext.REQUEST_FINE_LOCATION
            );
        }
    }

    public static void verifyCoaseLocationPermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, ApplicationContext.PERMISSIONS[1]);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    ApplicationContext.PERMISSIONS,
                    ApplicationContext.REQUEST_COARSE_LOCATION
            );
        }
    }
}
