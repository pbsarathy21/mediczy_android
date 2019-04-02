package app.mediczy_com.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import app.mediczy_com.R;
import app.mediczy_com.bean.BeanYourAppointment;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.imageloader.ImageLoader;
import app.mediczy_com.patient_problem_form.PatientFormDetail;
import app.mediczy_com.payment.Payment;
import app.mediczy_com.utility.CircularImageView;
import app.mediczy_com.utility.MLog;

/**
 * Created by Prithivi Raj on 14-12-2015.
 */
public class YourAppointmentPatient_Adapter extends RecyclerView.Adapter<YourAppointmentPatient_Adapter.ViewHolder> {
    Context context;
    private ImageLoader imageLoader;
    private ArrayList<BeanYourAppointment> arrayList;

    public YourAppointmentPatient_Adapter(Context doctorsList, ArrayList<BeanYourAppointment> itemsData) {
        this.arrayList = itemsData;
        this.context = doctorsList;
        imageLoader=new ImageLoader(doctorsList);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public YourAppointmentPatient_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_your_appointment_patient, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.TvName.setText(arrayList.get(position).getName());
        viewHolder.TvExp.setText(arrayList.get(position).getExperience());
        viewHolder.TvSpecial.setText(arrayList.get(position).getSpecialist());
//        viewHolder.Tvdate.setText(arrayList.get(position).getDate());
        viewHolder.Tvdate.setText(arrayList.get(position).getDate()+" "+arrayList.get(position).getTime());
        String imagePath = arrayList.get(position).getImage();
        MLog.e("imagePath", "" + imagePath);
        imageLoader.DisplayImage(imagePath, viewHolder.IvImage, 1);

        String status = arrayList.get(position).getStatus();
        if (status.equalsIgnoreCase("PENDING")) {
            viewHolder.btnStatus.setText("PENDING");
            viewHolder.btnStatus.setBackgroundResource(R.drawable.btn_border_app_color);
        }if (status.equalsIgnoreCase("PAY NOW")) {
            viewHolder.btnStatus.setText("PAY NOW");
            viewHolder.btnStatus.setBackgroundResource(R.drawable.btn_border_app_color);
        }if (status.equalsIgnoreCase("PAID")) {
            viewHolder.btnStatus.setText("PAID");
            viewHolder.btnStatus.setBackgroundResource(R.drawable.btn_border_green);
        }if (status.equalsIgnoreCase("REJECTED")) {
            viewHolder.btnStatus.setText("REJECTED");
            viewHolder.btnStatus.setBackgroundResource(R.drawable.btn_border_app_color);
        }

        viewHolder.btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayList.get(position).getStatus().equalsIgnoreCase("PAY NOW")){
                   /* arrayList.get(position).setStatus("paid");
                    notifyItemChanged(position);*/
                    String id = arrayList.get(position).getId();
                    Intent i = new Intent(context, Payment.class);
                    i.putExtra("Id", id);
                    i.putExtra(Constant.isFrom, "YourAppointmentPatient");
                    context.startActivity(i);
                    ((Activity)context).overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            }
        });

        viewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = arrayList.get(position).getId();
                Intent i = new Intent(context, PatientFormDetail.class);
                i.putExtra("appointment_id", id);
                context.startActivity(i);
                ((Activity)context).overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView TvName, TvExp, TvSpecial, Tvdate;
        public Button btnStatus;
        public CircularImageView IvImage;
        public CardView cv;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            TvName = (TextView) itemLayoutView.findViewById(R.id.textView1_appointment_name_patient);
            TvExp = (TextView) itemLayoutView.findViewById(R.id.textView_appointment_age_patient);
            TvSpecial = (TextView) itemLayoutView.findViewById(R.id.textView_appointment_title_patent);
            Tvdate = (TextView) itemLayoutView.findViewById(R.id.textView1_appointment_date_patient);
            btnStatus = (Button) itemLayoutView.findViewById(R.id.button1_appointment_patent_status);
            IvImage = (CircularImageView) itemLayoutView.findViewById(R.id.imageView_appointment_image_patient);
            cv = (CardView) itemLayoutView.findViewById(R.id.card_relative_layout_patient_adapter);
        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}