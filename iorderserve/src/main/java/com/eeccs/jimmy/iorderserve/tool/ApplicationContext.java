package com.eeccs.jimmy.iorderserve.tool;
import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.eeccs.jimmy.iorderserve.OrderDetailActivity;
import com.eeccs.jimmy.iorderserve.OrderDetailItem;
import com.eeccs.jimmy.iorderserve.OrderItem;
import com.eeccs.jimmy.iorderserve.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by User on 2016/6/11.
 */
public class ApplicationContext extends Application{
    private static final String TAG = ApplicationContext.class.getSimpleName();
    public static final String ORDER_ID = "ORDER_ID";
    private static ApplicationContext mInstance;
    public static final String APPLICATION_PREFERENCES = "APPLICATION_PREFERENCES";
    public static final int NOTIFY_SERVICE_ID = 100;
    public static final int REQUEST_NOTIFICATION_SERVICE = 0x01 << 14;
    public static int sid;

    public static String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    public static final int REQUEST_FINE_LOCATION = 0x01 << 12;
    public static final int REQUEST_COARSE_LOCATION = 0x01 << 11;

    //for server request
    public static final String BACKEND_API_URL = "http://140.113.203.226/~jimmy/iOrderPHP/";
    public static final String SHOW_ALL_ORDER = BACKEND_API_URL + "show_all_order.php";
    public static final String SHOW_CONTENT_BY_ID = BACKEND_API_URL + "show_content_by_id.php";
    public static final String UP_LOCATION = BACKEND_API_URL + "up_location.php";
    public static final String START_DELIVERING = BACKEND_API_URL + "start_delivering.php";
    public static final String FINISH_DELIVERING = BACKEND_API_URL + "finish_delivering.php";

    public static ApplicationContext getInstance(){
        ApplicationContext mApplication = mInstance;
        if(mInstance == null){
            mInstance = new ApplicationContext();
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        init();
    }


    //Initialize lists
    private void init() {
        SharedPreferences mPref = getSharedPreferences(APPLICATION_PREFERENCES, Context.MODE_PRIVATE);
    }


    public static void show_all_order( int store_id, final CallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("store_id", String.valueOf(store_id));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, SHOW_ALL_ORDER, json, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            Log.i(TAG, response.toString());
                            CallBackContent content=new CallBackContent();
                            JSONArray data = response.getJSONArray("data");
                            for(int i=0; i<data.length(); i++) {
                                String order_id =  ((JSONObject) data.get(i)).getString("order_id");
                                String customer = ((JSONObject) data.get(i)).getString("customer");
                                String pickup_location = ((JSONObject) data.get(i)).getString("pickup_location");
                                String pickup_time = ((JSONObject) data.get(i)).getString("pickup_time");
                                int start_flag =  Integer.parseInt(((JSONObject) data.get(i)).getString("start_flag"));
                                int end_flag =  Integer.parseInt(((JSONObject) data.get(i)).getString("end_flag"));
                                OrderItem returnedOrder = new OrderItem(order_id,customer,pickup_location,pickup_time,start_flag,end_flag);
                                content.show_order.add(returnedOrder);
                            }
                            callBack.done(content);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, error.toString());
                    }
                });
        //no cache
        jsObjRequest.setShouldCache(false);
        VolleyRequestManager.getInstance(getInstance().getApplicationContext()).addToRequestQueue(jsObjRequest);
    }

    public static void show_content_by_id(final String oid,final CallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("order_id", oid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, SHOW_CONTENT_BY_ID, json, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            Log.i(TAG, response.toString());
                            JSONObject data = response.getJSONObject("data");
                            CallBackContent content=new CallBackContent();
                            String item = data.getString("item");
                            String num = data.getString("num");
                            String total_cost =  data.getString("total_cost");
                            if(!item.equals("") && !item.equals("null"))
                            {
                                String[] itemList = item.split(",");
                                String[] numList = num.split(",");
                                for(int i=0; i<itemList.length;i++)
                                {
                                    OrderDetailItem returnedContent = new OrderDetailItem(itemList[i],numList[i],total_cost);
                                    content.show_order_content.add(returnedContent);
                                }
                            }
                            callBack.done(content);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, error.toString());
                    }
                });
        //no cache
        jsObjRequest.setShouldCache(false);
        VolleyRequestManager.getInstance(getInstance().getApplicationContext()).addToRequestQueue(jsObjRequest);
    }

    public static void up_location(String oid, String lat, String lng) {
        JSONObject json = new JSONObject();
        try {
            json.put("order_id", oid);
            json.put("lat", lat);
            json.put("lng",lng);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, UP_LOCATION, json, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, error.toString());
                    }
                });
        //no cache
        jsObjRequest.setShouldCache(false);
        VolleyRequestManager.getInstance(getInstance().getApplicationContext()).addToRequestQueue(jsObjRequest);
    }

    public static void start_delivering(String oid) {
        JSONObject json = new JSONObject();
        try {
            json.put("order_id", oid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, START_DELIVERING, json, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, error.toString());
                    }
                });
        //no cache
        jsObjRequest.setShouldCache(false);
        VolleyRequestManager.getInstance(getInstance().getApplicationContext()).addToRequestQueue(jsObjRequest);
    }

    public static void finish_delivering(String oid) {
        JSONObject json = new JSONObject();
        try {
            json.put("order_id", oid);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, FINISH_DELIVERING, json, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, error.toString());
                    }
                });
        //no cache
        jsObjRequest.setShouldCache(false);
        VolleyRequestManager.getInstance(getInstance().getApplicationContext()).addToRequestQueue(jsObjRequest);
    }

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

    public static boolean checkInternetConnection(Activity activity){
        ConnectivityManager cm=(ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni=cm.getActiveNetworkInfo();
        if(ni!=null && ni.isConnected()){
            // System.out.println("ni.isConnected() = "+ni.isConnected());
            return ni.isConnected();
        }else{
            // System.out.println("ni.isConnected() = "+ni.isConnected());
            return false;
        }
    }
}
