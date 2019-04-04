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
import java.util.List;
import app.mediczy_com.R;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.imageloader.ImageLoader;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.webservicemodel.response.NetworkPartnerListResponse;

/**
 * Created by Prithivi Raj on 10-10-2017.
 */
public class Insurance_Adapter extends RecyclerView.Adapter<Insurance_Adapter.ViewHolder> {

    private ImageLoader imageLoader;
    Context context;
    private List<NetworkPartnerListResponse> arrayList;

    public Insurance_Adapter(Context context, List<NetworkPartnerListResponse> networkPartnerListResponses) {
        this.arrayList = networkPartnerListResponses;
        this.context = context;
        imageLoader=new ImageLoader(context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Insurance_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_insurance_list, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        viewHolder.Tv_Name.setText(arrayList.get(position).name);
        viewHolder.Tv_Location.setText(arrayList.get(position).address);
        viewHolder.Tv_Offer.setText(arrayList.get(position).offers);
        viewHolder.Tv_Km.setText(arrayList.get(position).kilometer+" Km");

        String imagePath = arrayList.get(position).image;
        MLog.e("imagePath", "" + imagePath);
        imageLoader.DisplayImage(imagePath, viewHolder.image, 4);

        if (arrayList.get(position).verify_status.equalsIgnoreCase("0")) {
            viewHolder.Iv_Like.setVisibility(View.GONE);
        }else {
            viewHolder.Iv_Like.setVisibility(View.VISIBLE);
        }

        viewHolder.Iv_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String Number = arrayList.get(position).number;
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

        public TextView Tv_Name;
        public TextView Tv_Location;
        public TextView Tv_Offer;
        public ImageView Iv_Call;
        public ImageView Iv_Like;
        public TextView Tv_Km;
        public ImageView image;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            Tv_Name = (TextView) itemLayoutView.findViewById(R.id.textview_insurance_cmp_name);
            Tv_Location = (TextView) itemLayoutView.findViewById(R.id.textview_vd_insurance_comp_address);
            Tv_Km = (TextView) itemLayoutView.findViewById(R.id.textview_insurance_km);
            Iv_Like = (ImageView) itemLayoutView.findViewById(R.id.imageView1_insurance_comp_like);
            Tv_Offer = (TextView) itemLayoutView.findViewById(R.id.textview_vd_insurance_comp_offer);
            Iv_Call = (ImageView) itemLayoutView.findViewById(R.id.imageView1_insurance_comp_call);
            image = (ImageView) itemLayoutView.findViewById(R.id.imageView_insurance_km);
        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}