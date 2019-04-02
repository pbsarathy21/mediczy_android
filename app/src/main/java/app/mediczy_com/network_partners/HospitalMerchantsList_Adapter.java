package app.mediczy_com.network_partners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.mediczy_com.R;
import app.mediczy_com.imageloader.ImageLoader;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.webservicemodel.response.HospitalsMerchantsListResponse;
import app.mediczy_com.webservicemodel.response.MerchantsListResponse;

/**
 * Created by Prithivi Raj on 26-12-2017.
 */
public class HospitalMerchantsList_Adapter extends RecyclerView.Adapter<HospitalMerchantsList_Adapter.ViewHolder> {

    private Context context;
    private Bundle bundle;
    private ImageLoader imageLoader;
    public String Id, latitude="", longitude="", name="";
    private ArrayList<HospitalsMerchantsListResponse> arrayList;

    public HospitalMerchantsList_Adapter(Context ctx, ArrayList<HospitalsMerchantsListResponse> itemsData, Bundle bundle) {
        this.arrayList = itemsData;
        this.context = ctx;
        this.bundle = bundle;
        imageLoader=new ImageLoader(context);
        Id = this.bundle.getString("id");
        name = this.bundle.getString("name");
        latitude = this.bundle.getString("lat");
        longitude = this.bundle.getString("long");
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HospitalMerchantsList_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_hospital_network_partners_list, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        String imagePath = arrayList.get(position).image;
        MLog.e("imagePath", "" + imagePath);
        imageLoader.DisplayImage(imagePath, viewHolder.iv, 4);

        viewHolder.tv_title.setText(arrayList.get(position).title);
        viewHolder.tv_desc.setText(arrayList.get(position).description);
        viewHolder.tv_Km.setText(arrayList.get(position).kilometer);

        viewHolder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hospital_id = arrayList.get(position).hospital_id;
                MLog.e("hospital_id", "" + arrayList.get(position).hospital_id);
                Intent i = new Intent(context, MerchantsCategoryActivity.class);
                i.putExtra("id", hospital_id);
                i.putExtra("name", name);
                i.putExtra("lat", String.valueOf(latitude));
                i.putExtra("long", String.valueOf(longitude));
                context.startActivity(i);
                ((Activity)context).overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_desc;
        public TextView tv_title;
        public TextView tv_Offer;
        public TextView tv_Km;
        public RatingBar Rb;
        public ImageView iv;
        public RelativeLayout rl;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tv_desc = (TextView) itemLayoutView.findViewById(R.id.textview_desc_network_part);
            tv_title = (TextView) itemLayoutView.findViewById(R.id.textview_title_network_part);
            tv_Km = (TextView) itemLayoutView.findViewById(R.id.textview_distance_network_part);
            tv_Offer = (TextView) itemLayoutView.findViewById(R.id.textview_offer_network_part);
            Rb = (RatingBar) itemLayoutView.findViewById(R.id.ratingBar_network_part);
            iv = (ImageView) itemLayoutView.findViewById(R.id.image_network_part);
            rl = (RelativeLayout) itemLayoutView.findViewById(R.id.relative_view__network_part_all);
        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}