package com.eeccs.jimmy.iorderclient;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.eeccs.jimmy.iorderclient.tool.ApplicationContext;
import com.eeccs.jimmy.iorderclient.tool.CallBack;
import com.eeccs.jimmy.iorderclient.tool.CallBackContent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity {
    private double pos_latitude = 24.787081;
    private double pos_longitude = 120.9971278;
    private String oid;
    private LatLng Position;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Timer timer = new Timer();
    private int count;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        //mMap.addMarker(new MarkerOptions().position(NCTU).title("NCTU"));
        //Move the center position
        mMap.setMyLocationEnabled(true);
        //       mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NCTU, 16));
        Position = new LatLng(pos_latitude, pos_longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Position, 15));
        marker = mMap.addMarker(new MarkerOptions().position(Position)
                .title("Position")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        Intent intent = getIntent();
        oid = intent.getStringExtra("order_id");
        timer = new Timer();
        count = 0;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                count++;
                if (count > 1) {
                    ApplicationContext.get_location(oid, new CallBack() {
                        @Override
                        public void done(CallBackContent content) {
                            if (content != null) {
                                marker.remove();
                                pos_latitude = content.getLat();
                                pos_longitude = content.getLng();
                                Position = new LatLng(pos_latitude, pos_longitude);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Position, 15));
                                marker = mMap.addMarker(new MarkerOptions().position(Position)
                                        .title("Position")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                            } else {
                                Log.e("TAG", "get_location failed" + "\n");
                            }
                        }
                    });
                }

            }
        }, 0, 1 * 10 * 1000);//10 sec
        /*
        mMap.addMarker(new MarkerOptions().position(S4)
                .title("Star04")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
