package app.mediczy_com.Splash_Side;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import app.mediczy_com.R;

public class MoveActivity extends AppCompatActivity {

    List<Integer> color;
    List<String> colorName;
    List<Integer> image;
    ViewPager viewPager;
    TabLayout indicator;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_bot_activity_move);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        indicator = (TabLayout) findViewById(R.id.indicator);

        color = new ArrayList<>();
        color.add(Color.RED);
        color.add(Color.GREEN);
        color.add(Color.BLUE);
        color.add(Color.CYAN);


        image = new ArrayList<>();
        image.add(R.drawable.chat_bot_one);
        image.add(R.drawable.chat_bot_two);
        image.add(R.drawable.chat_bot_three);
        image.add(R.drawable.chat_bot_four);

        colorName = new ArrayList<>();
        colorName.add("RED");
        colorName.add("GREEN");
        colorName.add("BLUE");
        colorName.add("dhd");

        viewPager.setAdapter(new SliderAdapter(this, color, colorName, image));
        indicator.setupWithViewPager(viewPager);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 2000, 3000);



        /*After setting the adapter use the timer */
      /*  final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == viewPager.getCurrentItem()-1) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer .schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }
*/

    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            MoveActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() < image.size() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }

                }
            });
        }
    }
}

