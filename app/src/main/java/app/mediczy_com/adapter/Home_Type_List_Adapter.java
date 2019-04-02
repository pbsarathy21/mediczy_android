package app.mediczy_com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.mediczy_com.R;
import app.mediczy_com.bean.Bean_Doctor_List;
import app.mediczy_com.bean.Home_Type_Bean;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.utility.MLog;

/**
 * Created by Prithivi Raj on 14-12-2015.
 */
public class Home_Type_List_Adapter extends RecyclerView.Adapter<Home_Type_List_Adapter.ViewHolderCategory1>
{
    Context ctx;
    private ArrayList<Home_Type_Bean> arrayList;
    private List<Bean_Doctor_List> items;
    private int selectedPos = -1;

    public Home_Type_List_Adapter(Context context, ArrayList<Home_Type_Bean> array_bitmap_grid_adpater) {
        this.ctx = context;
        this.arrayList = array_bitmap_grid_adpater;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Home_Type_List_Adapter.ViewHolderCategory1 onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_screen_list, null);
        // create ViewHolder
        ViewHolderCategory1 viewHolder = new ViewHolderCategory1(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolderCategory1 viewHolder, int position) {

        String imagePath = IConstant_WebService.imageUrl_Cat+arrayList.get(position).getImage();
        MLog.e("imagePath", "" + imagePath);

        viewHolder.Iv__image.setScaleType(ImageView.ScaleType.FIT_XY);
//        holder.Iv__image.setImageBitmap(arrayList.get(position).getImage());
        viewHolder.Tv_Name.setText(arrayList.get(position).getTypeName());
        viewHolder.Tv_Count.setText(arrayList.get(position).getCount());

        Picasso.with(ctx).load(imagePath).
                into(viewHolder.Iv__image, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        // TODO Auto-generated method stub
                        super.onError();
//              Picasso.with(getActivity()).load(imageURL.get(position)).error(R.drawable.image_not_found);
                        viewHolder.Iv__image.setImageResource(R.drawable.no_image_plain);
                        MLog.e("ImageError", "" + "ImageError");
                    }
                });
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolderCategory1 extends RecyclerView.ViewHolder {
        ImageView Iv__image;
        TextView Tv_Name;
        TextView Tv_Count;

        public ViewHolderCategory1(View itemLayoutView) {
            super(itemLayoutView);
            Iv__image = (ImageView) itemLayoutView.findViewById(R.id.imageView_adapter_home_type);
            Tv_Name = (TextView) itemLayoutView.findViewById(R.id.textView_adapter_home_type_name);
            Tv_Count = (TextView) itemLayoutView.findViewById(R.id.textView_adapter_home_type_count);
        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}