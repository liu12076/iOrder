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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

import com.eeccs.jimmy.iorderserve.tool.ApplicationContext;
import com.eeccs.jimmy.iorderserve.tool.CallBack;
import com.eeccs.jimmy.iorderserve.tool.CallBackContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class OrderDetailActivity extends Activity implements View.OnClickListener{

    public static OrderDetailListAdapter mOrderDetailListAdapter;
    public static List<OrderDetailItem> mOrderDetailItems = new ArrayList<OrderDetailItem>();
    private final String TAG = OrderDetailActivity.class.getSimpleName();
    private ListView mListViewOrderDetail;
    private Button btn_start_delivering;
    private Button btn_finish_delivering;
    private TextView text_total_cost;
    private String oid;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private double lng;
    private double lat;
    private Timer timer = new Timer();
    private int count;
    String mProviderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            verifyFineLocationPermissions(this);
            verifyCoaseLocationPermissions(this);
        }
        //this.locationMgr = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location loc) {
                Log.i(TAG, "onLocationChanged. loc: " + loc);
                if (loc != null) {
                    Log.i(TAG, "onLocationChanged. latitude: "
                            + loc.getLatitude() + " , longtitude: " + loc.getLongitude());
                    lat = loc.getLatitude();
                    lng = loc.getLongitude();
                } else {
                }
            }

            // 当系统Setting -> Location & Security -> Use wireless networks取消勾选，Use GPS                    satellites取消勾选时调用
            public void onProviderDisabled(final String s) {
                Log.i(TAG, "onProviderDisabled. ");
            }

            // 当系统Setting -> Location & Security -> Use wireless networks勾选，Use GPS satellites勾           选时调用
            public void onProviderEnabled(final String s) {
                Log.i(TAG, "onProviderEnabled. ");
            }

            public void onStatusChanged(final String s, final int i, final Bundle b) {
                Log.i(TAG, "onStatusChanged. ");
            }
        };
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
    public void onStart() {
        super.onStart();
        Location lastKnownLocation =mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        mProviderName = LocationManager.NETWORK_PROVIDER;
                if (!TextUtils.isEmpty(mProviderName)) {
                    mLocationManager.requestLocationUpdates(
                            mProviderName, 1000, 1, mLocationListener);
                }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume. Provider Name: " + mProviderName);
        if (!TextUtils.isEmpty(mProviderName)) {
            // 当GPS定位时，在这里注册requestLocationUpdates监听就非常重要而且必要。
            mLocationManager.requestLocationUpdates(mProviderName, 1000, 1,
                    mLocationListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 取消注册监听
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_start:
                ApplicationContext.notificationServiceStartBuilder(this);
                ApplicationContext.start_delivering(oid);
                btn_start_delivering.setEnabled(false);
                timer = new Timer();
                count = 0;
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        count++;
                        if(count>1)
                            ApplicationContext.up_location(String.valueOf(oid),String.valueOf(lat),String.valueOf(lng));
                    }
                }, 0, 1 * 10 * 1000);//10 sec
                break;
            case R.id.button_finish:
                ApplicationContext.cancelNotificationService(this);
                ApplicationContext.finish_delivering(oid);
                timer.cancel();
                finish();
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
