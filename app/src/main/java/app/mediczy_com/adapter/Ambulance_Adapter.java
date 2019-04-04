package app.mediczy_com.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import app.mediczy_com.R;
import app.mediczy_com.bean.Bean_Ambulance;

/**
 * Created by Prithivi Raj on 14-12-2015.
 */
public class Ambulance_Adapter extends RecyclerView.Adapter<Ambulance_Adapter.ViewHolder> {
    Context context;
    private ArrayList<Bean_Ambulance> arrayList;

    public Ambulance_Adapter(Context doctorsList, ArrayList<Bean_Ambulance> itemsData) {
        this.arrayList = itemsData;
        this.context = doctorsList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Ambulance_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ambulance_list, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        viewHolder.Tv_HospitalName.setText(arrayList.get(position).getHospital_Name());
        viewHolder.Tv_Location.setText(arrayList.get(position).getLocation());
        viewHolder.Tv_Km.setText(arrayList.get(position).getKm());
        viewHolder.Tv_Offer.setText(arrayList.get(position).getOffer());

        viewHolder.Iv_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* String Number = arrayList.get(position).getNumber();
                if (Build.VERSION.SDK_INT >= 23) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + Number));
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                            == PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        context.startActivity(callIntent);
                    }else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    }
                }else {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + Number));
                    context.startActivity(callIntent);
                }*/
            }
        });
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView Tv_HospitalName;
        public TextView Tv_Location;
        public TextView Tv_Offer;
        public TextView Tv_Km;
        public ImageView Iv_Call;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            Tv_HospitalName = (TextView) itemLayoutView.findViewById(R.id.textview_vd_ambulance_hospital_name);
            Tv_Location = (TextView) itemLayoutView.findViewById(R.id.textview_vd_ambulance_hospital_address);
            Tv_Km = (TextView) itemLayoutView.findViewById(R.id.textview_vd_ambulance_km);
            Tv_Offer = (TextView) itemLayoutView.findViewById(R.id.textview_vd_ambulance_hospital_offer);
            Iv_Call = (ImageView) itemLayoutView.findViewById(R.id.imageView1_vd_full_data_ambulance_call);
        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}