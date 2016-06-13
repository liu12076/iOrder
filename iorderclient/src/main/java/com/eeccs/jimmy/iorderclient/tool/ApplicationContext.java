package com.eeccs.jimmy.iorderclient.tool;

/**
 * Created by User on 2016/6/13.
 */
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public static final String INSERT_ALL_ORDER = BACKEND_API_URL + "insert_all_order.php";
    public static final String INSERT_CONTENT_BY_ID = BACKEND_API_URL + "insert_content_by_id.php";

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


    public static void insert_all_order( int store_id, String order_id, String customer, String pickup_location, String pickup_time,final CallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("store_id", String.valueOf(store_id));
            json.put("order_id", order_id);
            json.put("customer", customer);
            json.put("pickup_location", pickup_location);
            json.put("pickup_time", pickup_time);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, INSERT_ALL_ORDER, json, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, response.toString());
                        CallBackContent content=new CallBackContent();
                        content.result = response.toString();
                        callBack.done(content);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, error.toString());
                        callBack.done(null);
                    }
                });
        //no cache
        jsObjRequest.setShouldCache(false);
        VolleyRequestManager.getInstance(getInstance().getApplicationContext()).addToRequestQueue(jsObjRequest);
    }

    public static void insert_content_by_id(String oid,String item, String num, String total_cost,final CallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("order_id", oid);
            json.put("item", item);
            json.put("num", num);
            json.put("total_cost", total_cost);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, INSERT_CONTENT_BY_ID, json, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, response.toString());
                        CallBackContent content=new CallBackContent();
                        content.result = response.toString();
                        callBack.done(content);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, error.toString());
                        callBack.done(null);
                    }
                });
        //no cache
        jsObjRequest.setShouldCache(false);
        VolleyRequestManager.getInstance(getInstance().getApplicationContext()).addToRequestQueue(jsObjRequest);
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
