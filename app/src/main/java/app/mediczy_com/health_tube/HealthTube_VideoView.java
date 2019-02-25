package app.mediczy_com.health_tube;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.MediaController;
import android.widget.VideoView;

import app.mediczy_com.R;
import app.mediczy_com.utility.MLog;

public class HealthTube_VideoView extends AppCompatActivity {

    Toolbar toolBar;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_video_view);
        init();
    }

    private void init() {
        toolBar = (Toolbar) findViewById(R.id.toolbar_doctor_notification_detail_video);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Article Video");

        videoView = (VideoView) findViewById(R.id.notification_video_view);

        Bundle bundle = getIntent().getExtras();
        String video = bundle.getString("title");

        MLog.e("videoPath", "" + video);
        if (video!=null && !video.equals("")) {
            videoView.setVideoURI(Uri.parse(video));
            videoView.setMediaController(new MediaController(this));
            videoView.requestFocus();
            videoView.start();
        }
    }
}
