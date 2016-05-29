package com.eeccs.jimmy.iorderserve;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends Activity implements View.OnClickListener{
    public static final int NOTIFY_SERVICE_ID = 100;
    public static final int REQUEST_NOTIFICATION_SERVICE = 0x01 << 14;
    public static OrderDetailListAdapter mOrderDetailListAdapter;
    public static List<OrderDetailItem> mOrderDetailItems = new ArrayList<OrderDetailItem>();
    private final String TAG = OrderDetailActivity.class.getSimpleName();
    private ListView mListViewOrderDetail;
    private Button btn_start_delivering;
    private Button btn_finish_delivering;

    public static void notificationServiceStartBuilder(Activity activity) {
        final int notifyID = NOTIFY_SERVICE_ID; // 通知的識別號碼
        final boolean autoCancel = false; // 點擊通知後是否要自動移除掉通知
        final Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); // 通知音效的URI，在這裡使用系統內建的通知音效
        final int requestCode = REQUEST_NOTIFICATION_SERVICE; // PendingIntent的Request Code
        final Intent intent = activity.getIntent(); // 目前Activity的Intent
        intent.setClass(activity, OrderDetailActivity.class);


        final int flags = PendingIntent.FLAG_UPDATE_CURRENT; // ONE_SHOT：PendingIntent只使用一次；CANCEL_CURRENT：PendingIntent執行前會先結束掉之前的；NO_CREATE：沿用先前的PendingIntent，不建立新的PendingIntent；UPDATE_CURRENT：更新先前PendingIntent所帶的額外資料，並繼續沿用
        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), requestCode, intent, flags); // 取得PendingIntent

        final NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE); // 取得系統的通知服務
        final Notification notification = new Notification.Builder(activity.getApplicationContext()).setSmallIcon(R.drawable.base_main).setContentTitle(activity.getString(R.string.notification_title)).setContentText(activity.getString(R.string.notification_content)).setSound(soundUri).setContentIntent(pendingIntent).setAutoCancel(autoCancel).build(); // 建立通知
        notification.flags = Notification.FLAG_ONGOING_EVENT; //將訊息常駐在status bar
        notificationManager.notify(notifyID, notification); // 發送通知
    }

    public static void cancelNotificationService(Activity activity) {
        final int notifyID = NOTIFY_SERVICE_ID;
        final NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE); // 取得系統的通知服務
        notificationManager.cancel(notifyID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
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
        populateList();
    }

    private void populateList() {
        mOrderDetailListAdapter.getData().clear();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int num_order_detail =  bundle.getInt(MainActivity.ORDER_DETAIL_NUM, 0);
        if(num_order_detail !=0)
        {
            for(int i=0; i<num_order_detail; i++) {
                String good = bundle.getString(MainActivity.ORDER_DETAIL_GOOD+i);
                String amount = bundle.getString(MainActivity.ORDER_DETAIL_AMOUNT+i);
                OrderDetailItem mDetail = new OrderDetailItem(good, amount);
                mOrderDetailItems.add(mDetail);
            }
            mOrderDetailListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_start:
                notificationServiceStartBuilder(this);
                break;
            case R.id.button_finish:
                cancelNotificationService(this);
                break;
        }
    }
}
