package app.mediczy_com.Slider;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import app.mediczy_com.R;
import app.mediczy_com.Retrofit.ListDetails;
import app.mediczy_com.Retrofit.RetrofitInterface;
import app.mediczy_com.Slider.Animations.DescriptionAnimation;
import app.mediczy_com.Slider.SliderTypes.BaseSliderView;
import app.mediczy_com.Slider.SliderTypes.DefaultSliderView;
import app.mediczy_com.Slider.Tricks.ViewPagerEx;
import app.mediczy_com.utility.MLog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SActivity extends AppCompatActivity implements View.OnClickListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private SliderLayout mDemoSlider;

    List<Slider_list> obj_sliderList;
    RetrofitInterface apiInterface;

    public HashMap<String, String> url_maps;

    //  List<Integer> image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_bot_activity_s);

        mDemoSlider = (SliderLayout) findViewById(R.id.slider);


        //Retrofit


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiInterface = retrofit.create(RetrofitInterface.class);


        Slider();
    }

    public void Slider() {


        Call<ListDetails> Slider_Pack = apiInterface.slide();
        Slider_Pack.enqueue(new Callback<ListDetails>() {
            @Override
            public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {

                MLog.e("", "Package Status:==>" + response.body());

                MLog.e("", "Package Count:==>" + response.body().status);


                obj_sliderList = response.body().splash_screens;
                if (obj_sliderList != null)
                    if (!obj_sliderList.isEmpty()) {

                        MLog.e("", "List size :==>" + response.body().splash_screens.size());

                        try {

                            url_maps = new HashMap<String, String>();
                            for (int arrIndx = 0; arrIndx < response.body().splash_screens.size(); arrIndx++) {
                                MLog.e("", "List Data image :==>" + response.body().splash_screens.get(arrIndx).image_path);
                            //    MLog.e("", "List Data Image Path :==>" + response.body().sliders.get(arrIndx).image_path);
                              //  MLog.e("", "List Data alt :==>" + response.body().sliders.get(arrIndx).alt);
                                url_maps.put(String.valueOf(response.body().splash_screens.get(arrIndx).image_path),"");

                                // mDemoSlider.setContentDescription(responseDetails.sliders.get(arrIndx).alt);
                            }

                            if (url_maps.size() == response.body().splash_screens.size()) {
                                for (String name : url_maps.keySet()) {
                                    MLog.e("", "Url:==>" + url_maps.get(name));
                                    DefaultSliderView textSliderView = new DefaultSliderView(SActivity.this);
                                    // initialize a SliderLayout
                                    textSliderView.image(url_maps.get(name))
                                            .setScaleType(BaseSliderView.ScaleType.Fit);
                                          //  .setOnSliderClickListener(this);

                                    //add your extra information
                                    textSliderView.bundle(new Bundle());
                                    textSliderView.getBundle().putString("image", name);
                                    textSliderView.getBundle().putString("image_path", url_maps.get(name));
                                    mDemoSlider.addSlider(textSliderView);
                                }

                                mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
                                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                                mDemoSlider.setDuration(2000);
                                //mDemoSlider.addOnPageChangeListener(this);
                            }

                        } catch (NullPointerException e) {
                            Log.e("HOME SILIDER", "NullPointerException" + e);
                        }


                    }


            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {

                Toast.makeText(SActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();


            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
