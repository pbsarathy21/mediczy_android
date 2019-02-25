package app.mediczy_com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import app.mediczy_com.R;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.imageloader.ImageLoader;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.CircularImageView;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.webservicemodel.response.ChatDetailModel;

/**
 * Created by Prithivi Raj on 08-06-2017.
 */

public class ChatDetail_Adapter extends RecyclerView.Adapter<ChatDetail_Adapter.ViewHolder> {

    private ImageLoader imageLoader;
    private Context context;
    private ArrayList<ChatDetailModel> arrayList;

    public ChatDetail_Adapter(Context doctorsList, ArrayList<ChatDetailModel> itemsData) {
        this.arrayList = itemsData;
        this.context = doctorsList;
        imageLoader=new ImageLoader(context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChatDetail_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_detail_left_adapter, null);
        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
 //       RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)viewHolder.rl.getLayoutParams();

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(560, RelativeLayout.LayoutParams.WRAP_CONTENT);

        viewHolder.tvMessage.setText(arrayList.get(position).getMessage());
        viewHolder.tvTime.setText(arrayList.get(position).getTime());

        String imagePath = IConstant_WebService.imageUrl_Doctors+arrayList.get(position).getImage();
        MLog.e("imagePath", "" + imagePath);
        imageLoader.DisplayImage(imagePath, viewHolder.iV, 4);

        if (arrayList.get(position).getPatient_id().equals(LocalStore.getInstance().getUserID(context))) {
            viewHolder.rl.setBackgroundResource(R.drawable.rectangle_relative_layout_left);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        }
        else {
            viewHolder.rl.setBackgroundResource(R.drawable.rectangle_relative_layout_right);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        }
        viewHolder.rl.setLayoutParams(params);
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvMessage;
        public TextView tvTime;
        public RelativeLayout rl;
        public CircularImageView iV;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tvMessage = (TextView) itemLayoutView.findViewById(R.id.tv_left_chat_detail_msg);
            tvTime = (TextView) itemLayoutView.findViewById(R.id.tv_left_chat_detail_time);
  //          rl = (RelativeLayout) itemLayoutView.findViewById(R.id.card_view_left_chat_detail);
  //          iV = (CircularImageView) itemLayoutView.findViewById(R.id.iv_left_chat_detail_msg);
        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}