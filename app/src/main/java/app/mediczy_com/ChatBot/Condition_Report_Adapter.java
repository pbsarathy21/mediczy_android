package app.mediczy_com.ChatBot;


import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import app.mediczy_com.R;
import app.mediczy_com.utility.MLog;

public class Condition_Report_Adapter extends RecyclerView.Adapter<Condition_Report_Adapter.ViewHolder> {


    public ArrayList<Bot_Condition> obj_Con = new ArrayList<>();
    public Bot_Report user_bot_report;


    Activity mActivity;
    int percentage;
    Double  perc=0.0;
    public static final String TAG = Condition_Report_Adapter.class.getSimpleName();

    public Condition_Report_Adapter(Activity activity, ArrayList<Bot_Condition> obj_Con) {

        this.mActivity = activity;
        this.obj_Con = obj_Con;

    }


    @Override
    public Condition_Report_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.chat_bot_condtion_report_adpter, viewGroup, false);
        // create ViewHolder
        Condition_Report_Adapter.ViewHolder viewHolder = new Condition_Report_Adapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final Condition_Report_Adapter.ViewHolder viewHolder, int position) {


        try {

            viewHolder.text_cat.setText(obj_Con.get(position).name);
            viewHolder.text_view.setText(obj_Con.get(position).severity);
            viewHolder.text_cat_value.setText(obj_Con.get(position).category);
            viewHolder.text_sex_value.setText("You will have to consult an" + " " + obj_Con.get(position).category);

            //set progressbar

            if(!obj_Con.get(position).probability.isEmpty()){
                if(!obj_Con.get(position).severity.isEmpty()){

                    if(obj_Con.get(position).severity.equalsIgnoreCase("mild")){

                        perc =  Double.parseDouble(obj_Con.get(position).probability);
                        MLog.e(TAG,"per==>"+perc);
                        double value = perc * 100;
                        MLog.e(TAG,"value==>"+value);
                        percentage = (int) Math.round(value);
                        MLog.e(TAG,"PERCENTAGE==>"+percentage);
                        Resources res = mActivity.getResources();
                        Rect bounds = viewHolder.progressbar.getProgressDrawable().getBounds();

                        viewHolder.progressbar.setProgressDrawable(res.getDrawable(R.drawable.chat_bot_progress));

                        viewHolder.progressbar.getProgressDrawable().setBounds(bounds);
                        viewHolder.progressbar.setProgress(percentage);

                    }else if(obj_Con.get(position).severity.equalsIgnoreCase("moderate")){

                        perc =  Double.parseDouble(obj_Con.get(position).probability);
                        MLog.e(TAG,"per==>"+perc);
                        double value = perc * 100;
                        MLog.e(TAG,"value==>"+value);
                        percentage = (int) Math.round(value);
                        MLog.e(TAG,"PERCENTAGE==>"+percentage);
                        Resources res = mActivity.getResources();
                        Rect bounds = viewHolder.progressbar.getProgressDrawable().getBounds();

                        viewHolder.progressbar.setProgressDrawable(res.getDrawable(R.drawable.chat_bot_progress_yellow));

                        viewHolder.progressbar.getProgressDrawable().setBounds(bounds);
                        viewHolder.progressbar.setProgress(percentage);

                    }else if(obj_Con.get(position).severity.equalsIgnoreCase("severe")){

                        perc =  Double.parseDouble(obj_Con.get(position).probability);
                        MLog.e(TAG,"per==>"+perc);
                        double value = perc * 100;
                        MLog.e(TAG,"value==>"+value);
                        percentage = (int) Math.round(value);
                        MLog.e(TAG,"PERCENTAGE==>"+percentage);
                        Resources res = mActivity.getResources();
                        Rect bounds = viewHolder.progressbar.getProgressDrawable().getBounds();

                        viewHolder.progressbar.setProgressDrawable(res.getDrawable(R.drawable.chat_bot_progress_severe));

                        viewHolder.progressbar.getProgressDrawable().setBounds(bounds);
                        viewHolder.progressbar.setProgress(percentage);

                    }

                }

            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {

        if (!obj_Con.isEmpty()) {
            return obj_Con.size();
        }


        return 1;

    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_cat, text_view, text_Categ, text_cat_value, text_advice, text_sex_value;
        ProgressBar progressbar;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            text_cat = (TextView) itemView.findViewById(R.id.text_cat);
            text_view = (TextView) itemView.findViewById(R.id.text_view);
            text_Categ = (TextView) itemView.findViewById(R.id.text_Categ);
            text_cat_value = (TextView) itemView.findViewById(R.id.text_cat_value);
            text_advice = (TextView) itemView.findViewById(R.id.text_advice);
            text_sex_value = (TextView) itemView.findViewById(R.id.text_sex_value);

            //progressbar
            progressbar=(ProgressBar)itemView.findViewById(R.id.progressbar);


            itemLayoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });


        }

    }

}


