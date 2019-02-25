package app.mediczy_com.ChatBot;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.mediczy_com.R;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.Consts;
import app.mediczy_com.utility.MLog;

/*
 * Created by l on 15-09-2018.
 */

public class CoverFlowAdapter extends BaseAdapter {

    //  private ArrayList<Game> data;
    private AppCompatActivity activity;

    public ArrayList<Symptom_Pack> pac = new ArrayList<>();
    private String userId;

    public CoverFlowAdapter(AppCompatActivity context, ArrayList<Symptom_Pack> pac) {
        this.activity = context;
        this.pac = pac;

        userId = LocalStore.getInstance().getUserID(context);
        MLog.e("userId==>", "" + userId);
    }

    @Override
    public int getCount() {
        return pac.size();
    }

    @Override
    public Object getItem(int position) {
        return pac.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_bot_item_flow, null, false);

            viewHolder = new ViewHolder(convertView);

            viewHolder.text_title.setText(pac.get(position).name);
            viewHolder.text_prize.setText(pac.get(position).amount);
            viewHolder.text_check.setText(pac.get(position).checker_count);
            viewHolder.coum.setText(pac.get(position).consultation_count + " Online Consultation");


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //viewHolder.gameImage.setImageResource(data.get(position).getImageSource());


        convertView.setOnClickListener(onClickListener(position));


        return convertView;
    }

    private View.OnClickListener onClickListener(final int position) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent objIntent = new Intent(activity, PaymentGateway.class);
                String Url = Consts.payment_url + "/" + userId + "/" + pac.get(position).id;
                objIntent.putExtra("URL", Url);
                activity.startActivity(objIntent);
                activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                activity.finish();



              /*  final Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.dialog_game_info);
                dialog.setCancelable(true); // dimiss when touching outside
                dialog.setTitle("Package");


                LinearLayout linear_Buy = (LinearLayout) dialog.findViewById(R.id.linear_Buy);
                TextView text_title = (TextView) dialog.findViewById(R.id.text_title);
                TextView text_prize = (TextView) dialog.findViewById(R.id.text_prize);
                TextView text_check = (TextView) dialog.findViewById(R.id.text_check);
                TextView coum = (TextView) dialog.findViewById(R.id.coum);


                text_title.setText(pac.get(position).name);
                text_prize.setText(pac.get(position).amount);
                text_check.setText(pac.get(position).checker_count);
                coum.setText(pac.get(position).consultation_count + "Online Consultation");

               *//* TextView text = (TextView) dialog.findViewById(R.id.name);
                text.setText(data.get(position).getName());
                ImageView image = (ImageView) dialog.findViewById(R.id.image);
                image.setImageResource(data.get(position).getImageSource());*//*

                dialog.show();*/
            }
        };
    }


    private static class ViewHolder {
        private TextView text_title, text_prize, text_check, coum, text;
        private LinearLayout linear_B;

        public ViewHolder(View v) {
            linear_B = (LinearLayout) v.findViewById(R.id.linear_B);
            text_title = (TextView) v.findViewById(R.id.text_title);
            text_prize = (TextView) v.findViewById(R.id.text_prize);
            text_check = (TextView) v.findViewById(R.id.text_check);
            text = (TextView) v.findViewById(R.id.text);

            coum = (TextView) v.findViewById(R.id.coum);
        }
    }
}
