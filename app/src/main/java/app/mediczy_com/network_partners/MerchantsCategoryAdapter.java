package app.mediczy_com.network_partners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.mediczy_com.DoctorsList;
import app.mediczy_com.R;
import app.mediczy_com.bean.Home_Type_Bean;
import app.mediczy_com.dashboard.HomeFragment;
import app.mediczy_com.delivery_form.DeliveryFormActivity;
import app.mediczy_com.home.OnClickListenerObserver;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.imageloader.ImageLoader;
import app.mediczy_com.utility.CircularImageView;
import app.mediczy_com.utility.MLog;

/**
 * Created by Prithivi Raj on 02-1-2018.
 */
public class MerchantsCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    Context ctx;
    private ArrayList<Home_Type_Bean> arrayList;
    private Bundle bundle;
    public String Id, latitude="", longitude="", name="";
    private ImageLoader imageLoader;

    public MerchantsCategoryAdapter(Context context, ArrayList<Home_Type_Bean> arrayList, Bundle bundle) {
        this.ctx = context;
        this.arrayList = arrayList;
        imageLoader=new ImageLoader(ctx);

        this.bundle = bundle;
        Id = this.bundle.getString("id");
        name = this.bundle.getString("name");
        latitude = this.bundle.getString("lat");
        longitude = this.bundle.getString("long");
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case Home_Type_Bean.TYPE_CATEGORY:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_screen_list, parent, false);
                return new ViewHolderCategory(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        Home_Type_Bean object = arrayList.get(listPosition);
        if (object != null) {
            switch (object.getType()) {
                case Home_Type_Bean.TYPE_CATEGORY:
                    setCategoryData(((ViewHolderCategory) holder), object);
                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {

        switch (arrayList.get(position).getType()) {
            case 0:
                return Home_Type_Bean.TYPE_CATEGORY;
            default:
                return -1;
        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolderCategory extends RecyclerView.ViewHolder {
        ImageView Iv__image;
        TextView Tv_Name;
        TextView Tv_Count;
        CardView cardView;

        public ViewHolderCategory(View itemLayoutView) {
            super(itemLayoutView);
            Iv__image = (ImageView) itemLayoutView.findViewById(R.id.imageView_adapter_home_type);
            Tv_Name = (TextView) itemLayoutView.findViewById(R.id.textView_adapter_home_type_name);
            Tv_Count = (TextView) itemLayoutView.findViewById(R.id.textView_adapter_home_type_count);
            cardView = (CardView) itemLayoutView.findViewById(R.id.card_relative_layout_home_adapter);
        }
    }

    private void setCategoryData(final ViewHolderCategory holder, final Home_Type_Bean object) {
        String imagePath = IConstant_WebService.imageUrl_Cat + object.getImage();
        MLog.e("imagePath", "" + imagePath);
        holder.Iv__image.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.Tv_Name.setText(object.getTypeName());
        holder.Tv_Count.setText(object.getCount());
        Picasso.with(ctx).load(imagePath).
                into(holder.Iv__image, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError() {
                        // TODO Auto-generated method stub
                        super.onError();
                        holder.Iv__image.setImageResource(R.drawable.no_image_plain);
                        MLog.e("ImageError", "" + "ImageError");
                    }
                });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ctx, DoctorsList.class);
                i.putExtra("id", object.getCategory_id());
                i.putExtra("hospital_id", object.getHospital_id());
                i.putExtra("name", object.getTypeName());
                i.putExtra("lat", String.valueOf(latitude));
                i.putExtra("long", String.valueOf(longitude));
                i.putExtra(Constant.isFrom, "MerchantsCategoryActivity");
                ctx.startActivity(i);
                ((Activity)ctx).overridePendingTransition(R.anim.enter, R.anim.exit);

            }
        });
    }
}