package app.mediczy_com.Splash_Side;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.List;

import app.mediczy_com.R;
import app.mediczy_com.dashboard.Register;

/**
 * Created by SPECBEE on 8/4/2017.
 */

class SliderAdapter extends PagerAdapter {
 //   private Context context;
    private List<Integer> color;
    private List<String> colorName;
    private List<Integer> image;
    Activity activity;

    public SliderAdapter(Activity activity, List<Integer> color, List<String> colorName, List<Integer> image) {
      //  this.context = context;
        this.color = color;
        this.colorName = colorName;
        this.image = image;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return color.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.chat_bot_item_slider, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linear_view);

        //textView.setText(colorName.get(position));
        // linearLayout.setBackgroundColor(color.get(position));


        Picasso.with(activity).load(image.get(position)).fit().into(imageView);
        // imageView.setImageResource(image.get(position));

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, Register.class);
                activity.startActivity(i);
                activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                activity.finish();
                //  Toast.makeText(context, "Check_it", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
