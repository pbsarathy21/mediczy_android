package app.mediczy_com.ChatBot;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.mediczy_com.R;

/**
 * Created by bala on 9/10/2018.
 */

public class Initial_Report extends RecyclerView.Adapter<Initial_Report.ViewHolder> {


    ArrayList<User_bot_Sys> obj_intial = new ArrayList<>();

    public Bot_Report user_bot_report;


    Activity mActivity;

    public Initial_Report(Activity activity, ArrayList<User_bot_Sys> obj_intial) {

        this.mActivity = activity;
        this.obj_intial = obj_intial;

    }


    @Override
    public Initial_Report.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.chat_bot_intial_adapter, viewGroup, false);
        // create ViewHolder
        Initial_Report.ViewHolder viewHolder = new Initial_Report.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final Initial_Report.ViewHolder viewHolder, int position) {


        try {

            viewHolder.rep.setText("Reported");
            viewHolder.rep_value.setText(obj_intial.get(position).name);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {

        if (!obj_intial.isEmpty()) {
            return obj_intial.size();
        }


        return 1;

    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rep, rep_value;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            rep = (TextView) itemView.findViewById(R.id.rep);
            rep_value = (TextView) itemView.findViewById(R.id.rep_value);

            itemLayoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });


        }

    }

}



