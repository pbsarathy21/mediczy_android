package app.mediczy_com.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.mediczy_com.DoctorsList;
import app.mediczy_com.R;
import app.mediczy_com.bean.Bean_Spa_Salon;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.imageloader.ImageLoader;
import app.mediczy_com.utility.MLog;

/**
 * Created by Prithivi Raj on 14-12-2015.
 */
public class Spa_List_Adapter extends RecyclerView.Adapter<Spa_List_Adapter.ViewHolder>
{
    Context context;
    private ImageLoader imageLoader;
    private ArrayList<Bean_Spa_Salon> arrayList;
    private List<Bean_Spa_Salon> items;

    public Spa_List_Adapter(DoctorsList doctorsList, ArrayList<Bean_Spa_Salon> itemsData) {
        this.arrayList = itemsData;
        this.context = doctorsList;
        imageLoader=new ImageLoader(doctorsList);
        this.items = new ArrayList<Bean_Spa_Salon>();
        this.items.addAll(itemsData);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Spa_List_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_spa_list, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        viewHolder.Tv_Name.setText(arrayList.get(position).getSpa_Title());
        viewHolder.Tv_Address.setText(arrayList.get(position).getSpa_Address());
        viewHolder.Tv_Date.setText(arrayList.get(position).getSpa_StartDay()+" - "+arrayList.get(position).getSpa_StartDay());
        viewHolder.Tv_Time.setText(arrayList.get(position).getSpa_StartTime()+" - "+arrayList.get(position).getSpa_EndTime());
        viewHolder.Tv_Type.setText(arrayList.get(position).getSpa_Service() + " only Spa");

        String imagePath = IConstant_WebService.imageUrl_spa_image+arrayList.get(position).getSpa_Image();
        MLog.e("imagePath", "" + imagePath);
        imageLoader.DisplayImage(imagePath, viewHolder.Iv_Image, 4);
/*        Picasso.with(context).load(imagePath).
                into(viewHolder.Iv_Image, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        // TODO Auto-generated method stub
                        super.onError();
                        viewHolder.Iv_Image.setImageResource(R.drawable.no_image_plain);
                        MLog.e("ImageError", "" + "ImageError");
                    }
                });*/

        viewHolder.Iv_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Number = arrayList.get(position).getSpa_Number();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + Number));
                context.startActivity(callIntent);
            }
        });
    }

    private void custom_dialog_App_conform(String Name) {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_appoinment_conform);
        TextView tv_cancel = (TextView) dialog.findViewById(R.id.textView_app_conform_cancel);
        TextView tv_send = (TextView) dialog.findViewById(R.id.textView_app_conform_continue);
        TextView tv_title = (TextView) dialog.findViewById(R.id.textView1_app_conform_title);
        tv_title.setText("You will be notified when "+ Name +" is available.");
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView Tv_Name;
        public TextView Tv_Address;
        public TextView Tv_Date;
        public TextView Tv_Time;
        public TextView Tv_Type;
        public ImageView Iv_Image;
        public ImageView Iv_Call;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            Tv_Name = (TextView) itemLayoutView.findViewById(R.id.textView_adapter_doctor_name_spa);
            Tv_Address = (TextView) itemLayoutView.findViewById(R.id.textView_adapter_doctor_study_spa);
            Tv_Date = (TextView) itemLayoutView.findViewById(R.id.textView_adapter_doctor_exp_spa);
            Tv_Time = (TextView) itemLayoutView.findViewById(R.id.textView_adapter_doctor_available_spa);
            Tv_Type = (TextView) itemLayoutView.findViewById(R.id.textView_adapter_doctor_type_spa);
            Iv_Image = (ImageView) itemLayoutView.findViewById(R.id.imageView_adapter_doctor_photo_spa);
            Iv_Call = (ImageView) itemLayoutView.findViewById(R.id.imageView_adapter_status_spa);
        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        if (charText.length() == 0) {
            arrayList.clear();
            arrayList.addAll(items);
        }
        else {
            for (Bean_Spa_Salon wp : items) {
                if (wp.getSpa_Title().toLowerCase(Locale.getDefault()).contains(charText)) {
                    arrayList.clear();
                    arrayList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}