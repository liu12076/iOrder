package com.eeccs.jimmy.iorderclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sherry on 2016/6/18.
 */
public class DeliveryListAdapter extends BaseAdapter {
    Context context;
    private List<String> listOids = new ArrayList<String>();
    private List<String> listSids = new ArrayList<String>();
    private List<String> listLocations = new ArrayList<String>();
    private List<String> listStartFlags = new ArrayList<String>();
    private static LayoutInflater inflater=null;
    public DeliveryListAdapter(Activity a, List<String> Oids, List<String> Sids, List<String> Locations, List<String> StartFlags) {
        // TODO Auto-generated constructor stub
        context=a;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listOids = Oids;
        listSids = Sids;
        listLocations = Locations;
        listStartFlags = StartFlags;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listOids.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        Button delivering_status;
        TextView name;
        TextView place;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_delivery_item, null);
        holder.name=(TextView) rowView.findViewById(R.id.tv_name);
        holder.place=(TextView) rowView.findViewById(R.id.tv_place);
        //holder.like_amount=(TextView) rowView.findViewById(R.id.like_amount);

        //new UI

        holder.delivering_status = (Button) rowView.findViewById(R.id.btn_status);
        holder.delivering_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtra("order_id", listOids.get(position));
                context.startActivity(intent);
            }
        });

        if(listStartFlags.get(position).equals("1"))
        {
            holder.delivering_status.setText("配送中");
            holder.delivering_status.setEnabled(true);
        }
        else
        {
            holder.delivering_status.setText("未配送");
            holder.delivering_status.setEnabled(false);
        }

        holder.name.setText(listOids.get(position));
        holder.place.setText(listLocations.get(position));
/*
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
            }
        });
        */
        return rowView;
    }
}