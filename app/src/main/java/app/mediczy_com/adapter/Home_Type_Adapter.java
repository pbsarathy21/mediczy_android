package app.mediczy_com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.mediczy_com.R;
import app.mediczy_com.bean.Home_Type_Bean;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.imageloader.ImageLoader;
import app.mediczy_com.storage.LocalStore;
import app.mediczy_com.utility.MLog;

/**
 * Created by Prithivi Raj on 11-12-2015.
 */
public class Home_Type_Adapter extends BaseAdapter
{
    LayoutInflater mInflater;
    Context ctx;
    private ImageLoader imageLoader;
    ArrayList<Home_Type_Bean> arrayList;
    private int selectedPos = -1;
    private String Type;

    public Home_Type_Adapter(Context context, ArrayList<Home_Type_Bean> array_bitmap_grid_adpater) {
        this.mInflater = LayoutInflater.from(context);
        this.ctx = context;
        imageLoader=new ImageLoader(ctx);
        this.arrayList = array_bitmap_grid_adpater;
        Type = LocalStore.getInstance().getType(ctx);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder1 holder;
        if (convertView == null) {
            holder = new ViewHolder1();
            convertView = mInflater.inflate(R.layout.adapter_home_screen, null);
            holder.Iv__image = (ImageView) convertView.findViewById(R.id.imageView_adapter_home_type);
            holder.Tv_Name = (TextView) convertView.findViewById(R.id.textView_adapter_home_type_name);
            holder.Tv_Count = (TextView) convertView.findViewById(R.id.textView_adapter_home_type_count);
            holder.Rl_bookAppointment = (RelativeLayout) convertView.findViewById(R.id.rl_adapter_book_appo);
            holder.Iv__image.setTag(position);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder1) convertView.getTag();
        }
        String imagePath = IConstant_WebService.imageUrl_Cat+arrayList.get(position).getImage();
        MLog.e("imagePath", "" + imagePath);

        holder.Iv__image.setScaleType(ImageView.ScaleType.FIT_XY);
//        holder.Iv__image.setImageBitmap(arrayList.get(position).getImage());
        holder.Tv_Name.setText(arrayList.get(position).getTypeName());
        holder.Tv_Count.setText(arrayList.get(position).getCount());
        imageLoader.DisplayImage(imagePath, holder.Iv__image, 4);
        holder.Rl_bookAppointment.setVisibility(View.GONE);
/*        if (Type.equals("Patient")) {
            holder.Rl_bookAppointment.setVisibility(View.VISIBLE);
        }else {
            holder.Rl_bookAppointment.setVisibility(View.GONE);
        }*/

        holder.Rl_bookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
/*        Picasso.with(ctx).load(imagePath).
                into(holder.Iv__image, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        // TODO Auto-generated method stub
                        super.onError();
//              Picasso.with(getActivity()).load(imageURL.get(position)).error(R.drawable.image_not_found);
                        holder.Iv__image.setImageResource(R.drawable.no_image_plain);
                        MLog.e("ImageError", "" + "ImageError");
                    }
                });*/

        return convertView;
    }

    class ViewHolder1 {
        ImageView Iv__image;
        TextView Tv_Name;
        TextView Tv_Count;
        RelativeLayout Rl_bookAppointment;
    }
}
