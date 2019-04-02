package app.mediczy_com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import app.mediczy_com.R;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.imageloader.ImageLoader;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.MLog;
import app.mediczy_com.utility.Utility;
import app.mediczy_com.webservicemodel.response.ChatDetailModel;

/**
 * Created by Prithivi Raj on 08-06-2017.
 */

public class ChatDetailList_Adapter extends BaseAdapter {

    private ImageLoader imageLoader;
    private Context context;
    private ArrayList<ChatDetailModel> arrayList;
    private LayoutInflater myInflater;
    private String userID;

    public ChatDetailList_Adapter(Context ctx, ArrayList<ChatDetailModel> itemsData) {
        this.arrayList = itemsData;
        this.context = ctx;
        imageLoader = new ImageLoader(context);
        myInflater = LayoutInflater.from(context);
        userID = LocalStore.getInstance().getUserID(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return arrayList.size();
    }

/*    @Override
    public int getViewTypeCount() {
        return 2;
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = myInflater.inflate(R.layout.chat_detail_right_adapter,  parent, false);
/*            if (arrayList.get(position).getPatient_id().equals(userID)) {
                convertView = myInflater.inflate(R.layout.chat_detail_right_adapter,  parent, false);
            }
            else {
                convertView = myInflater.inflate(R.layout.chat_detail_left_adapter,  parent, false);
            }*/
            viewHolder = new ViewHolder();
            viewHolder.tvMessage = (TextView) convertView.findViewById(R.id.tv_left_chat_detail_msg);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_left_chat_detail_time);
            viewHolder.tvFileLeft = (TextView) convertView.findViewById(R.id.tv_left_text_file);
            viewHolder.iVMsg = (ImageView) convertView.findViewById(R.id.iv_left_chat_detail_msg1);
            viewHolder.iVFileLeft = (ImageView) convertView.findViewById(R.id.imageView_chat_left_logo);
            viewHolder.llFileLeft = (LinearLayout) convertView.findViewById(R.id.linear_file_left);
            viewHolder.llTextLeft = (LinearLayout) convertView.findViewById(R.id.linear_text_left_1);
            viewHolder.flLeft = (FrameLayout) convertView.findViewById(R.id.frame_left);

            viewHolder.tvMessageLeft = (TextView) convertView.findViewById(R.id.tv_left_chat_detail_msg_left);
            viewHolder.tvTimeLeft = (TextView) convertView.findViewById(R.id.tv_left_chat_detail_time_left);
            viewHolder.tvFileRight = (TextView) convertView.findViewById(R.id.tv_right_text_file);
            viewHolder.iVMsgLeft = (ImageView) convertView.findViewById(R.id.iv_left_chat_detail_msg1_left);
            viewHolder.iVFileRight = (ImageView) convertView.findViewById(R.id.imageView_chat_right_logo);
            viewHolder.llFileRight = (LinearLayout) convertView.findViewById(R.id.linear_file_right);
            viewHolder.llTextRight = (LinearLayout) convertView.findViewById(R.id.linear_text_right);
            viewHolder.flRight = (FrameLayout) convertView.findViewById(R.id.frame_right);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvMessage.setText(arrayList.get(position).getMessage());
        viewHolder.tvTime.setText(arrayList.get(position).getTime());
        viewHolder.tvFileRight.setText(arrayList.get(position).getFile_name());

        viewHolder.tvMessageLeft.setText(arrayList.get(position).getMessage());
        viewHolder.tvTimeLeft.setText(arrayList.get(position).getTime());
        viewHolder.tvFileLeft.setText(arrayList.get(position).getFile_name());

        if (arrayList.get(position).getSender_id().equals(userID)) {
            setRightSide(position, viewHolder);
        } else {
            setLeftSide(position, viewHolder);
        }

        viewHolder.iVMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Utility.getInstance().showImage(viewHolder.iVMsg, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        viewHolder.iVMsgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Utility.getInstance().showImage(viewHolder.iVMsgLeft, context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return convertView;
    }

    private void setLeftSide(int position, ViewHolder viewHolder) {
        viewHolder.flRight.setVisibility(View.GONE);
        viewHolder.flLeft.setVisibility(View.VISIBLE);

        if (arrayList.get(position).getType().equalsIgnoreCase("IMAGE")) {
            String imagePath = arrayList.get(position).getImage();
            MLog.e("imagePath", "" + imagePath);
            imageLoader.DisplayImage(imagePath, viewHolder.iVMsgLeft, 4);

            viewHolder.tvMessageLeft.setVisibility(View.GONE);
            viewHolder.llFileLeft.setVisibility(View.GONE);
            viewHolder.iVMsgLeft.setVisibility(View.VISIBLE);
            viewHolder.llTextLeft.setVisibility(View.VISIBLE);
        }else if (arrayList.get(position).getType().equalsIgnoreCase("bitmap")) {
            viewHolder.tvMessageLeft.setVisibility(View.GONE);
            viewHolder.llFileLeft.setVisibility(View.GONE);
            viewHolder.iVMsgLeft.setVisibility(View.VISIBLE);
            viewHolder.llTextLeft.setVisibility(View.VISIBLE);
            viewHolder.iVMsgLeft.setImageBitmap(arrayList.get(position).getBitmap());
        }else if (arrayList.get(position).getType().equalsIgnoreCase("TEXT")) {
            viewHolder.iVMsgLeft.setVisibility(View.GONE);
            viewHolder.llFileLeft.setVisibility(View.GONE);
            viewHolder.tvMessageLeft.setVisibility(View.VISIBLE);
            viewHolder.llTextLeft.setVisibility(View.VISIBLE);
        }else if (arrayList.get(position).getType().equalsIgnoreCase("file")) {
            viewHolder.llTextLeft.setVisibility(View.GONE);
            viewHolder.llFileLeft.setVisibility(View.VISIBLE);
            String fileExtension = arrayList.get(position).getFile_extension();
            if (fileExtension.equalsIgnoreCase("pdf")) {
                viewHolder.iVFileLeft.setBackgroundResource(R.drawable.pdf_logo);
            }else if (fileExtension.endsWith(".txt") || fileExtension.endsWith(".doc")|| fileExtension.endsWith(".DOC")
                    || fileExtension.endsWith(".docx")|| fileExtension.endsWith(".DOCX")) {
                viewHolder.iVFileLeft.setBackgroundResource(R.drawable.word_doc);
            }else {
                viewHolder.iVFileLeft.setBackgroundResource(R.drawable.lock);
            }
        }
    }

    private void setRightSide(int position, ViewHolder viewHolder) {

        viewHolder.flLeft.setVisibility(View.GONE);
        viewHolder.flRight.setVisibility(View.VISIBLE);

        if (arrayList.get(position).getType().equalsIgnoreCase("IMAGE")) {
            String imagePath = arrayList.get(position).getImage();
            MLog.e("imagePath", "" + imagePath);
            imageLoader.DisplayImage(imagePath, viewHolder.iVMsg, 4);

            viewHolder.tvMessage.setVisibility(View.GONE);
            viewHolder.llFileRight.setVisibility(View.GONE);
            viewHolder.iVMsg.setVisibility(View.VISIBLE);
            viewHolder.llTextRight.setVisibility(View.VISIBLE);
        }else if (arrayList.get(position).getType().equalsIgnoreCase("bitmap")) {
            viewHolder.tvMessage.setVisibility(View.GONE);
            viewHolder.llFileRight.setVisibility(View.GONE);
            viewHolder.iVMsg.setVisibility(View.VISIBLE);
            viewHolder.llTextRight.setVisibility(View.VISIBLE);
            viewHolder.iVMsg.setImageBitmap(arrayList.get(position).getBitmap());
        }else if (arrayList.get(position).getType().equalsIgnoreCase("TEXT")) {
            viewHolder.iVMsg.setVisibility(View.GONE);
            viewHolder.llFileRight.setVisibility(View.GONE);
            viewHolder.tvMessage.setVisibility(View.VISIBLE);
            viewHolder.llTextRight.setVisibility(View.VISIBLE);
        }else if (arrayList.get(position).getType().equalsIgnoreCase("file")) {
            viewHolder.llTextRight.setVisibility(View.GONE);
            viewHolder.llFileRight.setVisibility(View.VISIBLE);
            String fileExtension = arrayList.get(position).getFile_extension();
            if (fileExtension.equalsIgnoreCase("pdf")) {
                viewHolder.iVFileRight.setBackgroundResource(R.drawable.pdf_logo);
            }else if (fileExtension.endsWith(".txt") || fileExtension.endsWith(".doc")|| fileExtension.endsWith(".DOC")
                    || fileExtension.endsWith(".docx")|| fileExtension.endsWith(".DOCX")) {
                viewHolder.iVFileRight.setBackgroundResource(R.drawable.word_doc);
            }else {
                viewHolder.iVFileRight.setBackgroundResource(R.drawable.lock);
            }
        }
    }

    // inner class to hold a reference to each item of ListView
    public static class ViewHolder {
        public TextView tvMessage;
        public TextView tvTime;
        public ImageView iVMsg;

        public TextView tvMessageLeft;
        public TextView tvTimeLeft;
        public TextView tvFileLeft;
        public TextView tvFileRight;

        public ImageView iVMsgLeft;
        public ImageView iVFileLeft;
        public ImageView iVFileRight;

        public FrameLayout flLeft;
        public FrameLayout flRight;
        public LinearLayout llFileRight;
        public LinearLayout llFileLeft;
        public LinearLayout llTextRight;
        public LinearLayout llTextLeft;
    }
}