package com.domain.porter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.domain.porter.model.Parcel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pratik on 29/8/15.
 */
public class ParcelAdapter extends ArrayAdapter<Parcel> {

    private ArrayList<Parcel> mParcels;
    private Context mContext;

    static class ViewHolder {
        public TextView name;
        public TextView phone;
        public TextView date;
    }

    public ParcelAdapter(Context context, ArrayList<Parcel> parcels) {
        super(context, R.layout.list_parcel_item, parcels);
        mContext = context;
        mParcels = parcels;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =  convertView;
        if (view == null) {
            ViewHolder viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.list_parcel_item, null);
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.phone = (TextView) view.findViewById(R.id.phone);
            viewHolder.date = (TextView) view.findViewById(R.id.date);
            view.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        Parcel parcel = mParcels.get(position);
        holder.name.setText(parcel.getName());
        holder.phone.setText(parcel.getPhone());
        holder.date.setText(getDateFromEpoch(parcel.getDate()));
        return view;
    }

    private String getDateFromEpoch(String timestamp) {
        Date date = new Date(Long.parseLong(timestamp) * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy' 'HH");
        return simpleDateFormat.format(date) + " hours";
    }
}
