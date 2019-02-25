package app.mediczy_com.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.mediczy_com.R;
import app.mediczy_com.bean.Bean_Doctor_Prescribed;
import app.mediczy_com.doctor_prescribed.Doctor_Prescribed_Detail;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.imageloader.ImageLoader;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.CircularImageView;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.Utility;

/**
 * Created by Prithivi Raj on 14-12-2015.
 */
public class Doctor_Prescribed_Adapter extends RecyclerView.Adapter<Doctor_Prescribed_Adapter.ViewHolder> {
    Context context;
    private ImageLoader imageLoader;
    private ArrayList<Bean_Doctor_Prescribed> arrayList;

    public Doctor_Prescribed_Adapter(Context doctorsList, ArrayList<Bean_Doctor_Prescribed> itemsData) {
        this.arrayList = itemsData;
        this.context = doctorsList;
        imageLoader = new ImageLoader(doctorsList);
    }

    @Override
    public Doctor_Prescribed_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_doctor_prescribed_list, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.Tv_Title.setText(arrayList.get(position).getTitle());
        viewHolder.Tv_Subject.setText(arrayList.get(position).getSubject());
        viewHolder.Tv_Desc.setText(arrayList.get(position).getDesc());
        viewHolder.Tv_Date.setText(arrayList.get(position).getDate());

        if (arrayList.get(position).getType() != null && !arrayList.get(position).getType().equals("")) {
            if (arrayList.get(position).getType().equalsIgnoreCase(Constant.Patient)) {
                viewHolder.Tv_PatientName.setVisibility(View.GONE);
                viewHolder.Iv_Image.setVisibility(View.VISIBLE);
                String imagePath = arrayList.get(position).getImage();
                MLog.e("imagePath", "" + imagePath);
                imageLoader.DisplayImage(imagePath, viewHolder.Iv_Image, 4);
            }
            if (arrayList.get(position).getType().equalsIgnoreCase(Constant.Doctor)) {
                if (arrayList.get(position).getTitle() != null && !arrayList.get(position).getTitle().equals("")) {
                    viewHolder.Tv_PatientName.setVisibility(View.VISIBLE);
                    viewHolder.Iv_Image.setVisibility(View.GONE);
                    String name = arrayList.get(position).getTitle();
                    name = String.valueOf(name.charAt(0));
                    MLog.e("Name_FirstLetter", "" + name);
                    viewHolder.Tv_PatientName.setText(name);
                }
            }
        } else {
            //           Utility.getInstance().showToast("Type is null", context);
        }

        viewHolder.Rl_Click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = arrayList.get(position).getId();
                Intent i = new Intent(context, Doctor_Prescribed_Detail.class);
                i.putExtra("id", id);
                context.startActivity(i);
                ((Activity) context).overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView Tv_Title;
        public TextView Tv_Subject;
        public TextView Tv_Desc;
        public TextView Tv_Date;
        public RelativeLayout Rl_Click;
        public CircularImageView Iv_Image;
        public TextView Tv_PatientName;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            Tv_PatientName = (TextView) itemLayoutView.findViewById(R.id.tv_desc_first_name_adapter);
            Tv_Title = (TextView) itemLayoutView.findViewById(R.id.textView_adapter_doctor_prescribed_name);
            Tv_Subject = (TextView) itemLayoutView.findViewById(R.id.textView_adapter_doctor_prescribed_study);
            Tv_Desc = (TextView) itemLayoutView.findViewById(R.id.textView_adapter_doctor_prescribed_desc);
            Tv_Date = (TextView) itemLayoutView.findViewById(R.id.textview_adapter_doctor_prescribed_date);
            Iv_Image = (CircularImageView) itemLayoutView.findViewById(R.id.imageView_adapter_doctor_prescribed_photo);
            Rl_Click = (RelativeLayout) itemLayoutView.findViewById(R.id.relative_view_doctor_prescribed_adapter);
        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}