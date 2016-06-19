package com.eeccs.jimmy.iorderclient;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sherry on 2016/6/18.
 */
public class DeliveryListAdapter extends BaseAdapter{
    private final static String TAG = DeliveryListAdapter.class.getSimpleName();
    private List<DeliveryItem> mData;
    private Context mContext;
    //private int isMaster=0;
    public DeliveryListAdapter(final Context context, final List<DeliveryItem> mData) {
        this.mData = mData;
        this.mContext = context;
    }

    public List<DeliveryItem> getData() {
        return mData;
    }

    private static class ViewHolder {
        public TextView name;
        public TextView place;
        public Button btn_status;
    }
    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return mData != null ? mData.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = new ViewHolder();

        if (view == null) {
            // inflate the layout, see how we can use this context reference?
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            view = inflater.inflate(R.layout.list_delivery_item, parent, false);
            Log.e(TAG, String.format("Get view %d", position));
            // we'll set up the ViewHolder
            viewHolder.name      = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.place     = (TextView) view.findViewById(R.id.tv_place);
            viewHolder.btn_status  = (Button) view.findViewById(R.id.btn_status);
            //store the holder with the view.
            view.setTag(viewHolder);

        } else {
            // we've just avoided calling findViewById() on resource every time
            // just use the viewHolder instead
            viewHolder = (ViewHolder) view.getTag();
        }

        // object item based on the position
        DeliveryItem obj = mData.get(position);

        // assign values if the object is not null
        if (mData != null) {
            viewHolder.name.setText(obj.getStoreName());
            viewHolder.place.setText(obj.getStoreName());
            viewHolder.btn_status.setText("未配送");
        }
        return convertView;
    }
}
