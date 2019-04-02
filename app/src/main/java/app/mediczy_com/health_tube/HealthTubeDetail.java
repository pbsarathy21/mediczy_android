package app.mediczy_com.health_tube;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import app.mediczy_com.R;
import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.iconstant.IConstant_WebService;
import app.mediczy_com.imageloader.ImageLoader;
import app.mediczy_com.utility.MLog;

public class HealthTubeDetail extends AppCompatActivity {

    Toolbar toolBar;
    TextView Tv_Title, Tv_Desc;
    ImageView Iv;
    private ImageLoader imageLoader;
    VideoView videoView;
    private WebView webView;
    private String title, desc, imageVideo, imagePath, date, type, isFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_detail);

        init();

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.start();
            }
        });
/*
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // This is just to show image when loaded
                mp.start();
                mp.pause();
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // setLooping(true) didn't work, thats why this workaround
 //               videoView.setVideoPath(image);
 //               videoView.start();
                MediaController mediaController = new MediaController(HealthTubeDetail.this);
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(Uri.parse(imagePath));
                videoView.start();

*//*                videoView.setVideoURI(Uri.parse(imagePath));
                videoView.setMediaController(new MediaController(HealthTubeDetail.this));
                videoView.requestFocus();
                videoView.start();*//*
            }
        });*/
    }

    private void init() {
        toolBar = (Toolbar) findViewById(R.id.toolbar_doctor_notification_detail);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Health Tube Detail");

        webView = (WebView) findViewById(R.id.notification_webView);
        videoView = (VideoView) findViewById(R.id.notification_video_view);
        Tv_Title = (TextView)findViewById(R.id. textView_heading_notification_title_detail);
        Tv_Desc = (TextView)findViewById(R.id.textView_heading_notification_title_detail_desc);
        Iv = (ImageView) findViewById(R.id.imageView_notification_detail);
        imageLoader=new ImageLoader(this);

        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        desc = bundle.getString("desc");
        date = bundle.getString("date");
        type = bundle.getString("type");
        imageVideo = bundle.getString("image");
        isFrom = bundle.getString(Constant.isFrom);
        Tv_Title.setText(""+title);
        Tv_Desc.setText(""+desc);
        MLog.e("isFrom", "" + isFrom);
        MLog.e("type", "" + type);

        if (type.equalsIgnoreCase("image")) {
            Iv.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            webView.setVisibility(View.GONE);
            if (isFrom.equalsIgnoreCase("HomeFragment")) {
                imagePath = imageVideo;
            } else if(isFrom.equalsIgnoreCase("HealthTubeFragment")) {
                imagePath = imageVideo;
            }
            MLog.e("imagePath", "" + imagePath);

            if (imagePath!=null && !imagePath.isEmpty()){
//            setImage(image);
//            imageLoader.DisplayImage(image, Iv, 4);
                Picasso.with(this).load(imagePath).
                        into(Iv, new Callback.EmptyCallback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                                // TODO Auto-generated method stub
                                super.onError();
//              Picasso.with(getActivity()).load(imageURL.get(position)).error(R.drawable.image_not_found);
                                Iv.setImageResource(R.drawable.no_image);
                                MLog.e("ImageError", "" + "ImageError");
                            }
                        });
            }

        }else if (type.equalsIgnoreCase("video")) {
            imagePath = IConstant_WebService.imageUrl_Notification+imageVideo;
            showVideo();
        }else if (type.equalsIgnoreCase("Youtube")) {
            imagePath = imageVideo;
/*            LoadUrl loadUrl = new LoadUrl();
            loadUrl.execute();*/
            Iv.setVisibility(View.GONE);
            videoView.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
            webView.loadUrl(imagePath + "?autoplay=1&vq=small");
            webView.setWebChromeClient(new WebChromeClient());
        }
    }

    private void showVideo() {
        Iv.setVisibility(View.GONE);
        webView.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);
        if (imagePath!=null && !imagePath.equals("")) {
            videoView.setVideoURI(Uri.parse(imagePath));
            videoView.setMediaController(new MediaController(this));
            videoView.requestFocus();
            videoView.start();
/*            MediaController mediaController = new MediaController(HealthTubeDetail.this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(Uri.parse(imagePath));
            videoView.start();*/
        }
    }

    //LoadUrl video
    public class LoadUrl extends AsyncTask<String, String, String> {

        String newVersion;
        ProgressDialog pDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HealthTubeDetail.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("loading..."); // typically you will define such
            // strings in a remote file.
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String path = getRTSPVideoUrl(imagePath);
            return path;
        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);
            pDialog.dismiss();

            Iv.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);

            videoView.setVideoURI(Uri.parse(path));
            videoView.setMediaController(new MediaController(HealthTubeDetail.this));
            videoView.requestFocus();
            videoView.start();
        }
    }

    public String getRTSPVideoUrl(String urlYoutube) {
        try {
            String gdy = "http://gdata.youtube.com/feeds/api/videos/";
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            String id = extractYoutubeId(urlYoutube);
            URL url = new URL(gdy + id);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            Document doc = dBuilder.parse(connection.getInputStream());
            Element el = doc.getDocumentElement();
            NodeList list = el.getElementsByTagName("media:content");
            String cursor = urlYoutube;
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node != null) {
                    NamedNodeMap nodeMap = node.getAttributes();
                    HashMap<String, String> maps = new HashMap<String, String>();
                    for (int j = 0; j < nodeMap.getLength(); j++) {
                        Attr att = (Attr) nodeMap.item(j);
                        maps.put(att.getName(), att.getValue());
                    }
                    if (maps.containsKey("yt:format")) {
                        String f = maps.get("yt:format");
                        if (maps.containsKey("url"))
                            cursor = maps.get("url");
                        if (f.equals("1"))
                            return cursor;
                    }
                }
            }
            return cursor;
        } catch (Exception ex) {
            return urlYoutube;
        }
    }

    public String extractYoutubeId(String url) throws MalformedURLException {
        String query = new URL(url).getQuery();
        String[] param = query.split("&");
        String id = null;
        for (String row : param) {
            String[] param1 = row.split("=");
            if (param1[0].equals("v")) {
                id = param1[1];
            }
        }
        return id;
    }

    private void setImage(String image) {
        Picasso.with(this).load(image).
                into(Iv, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        // TODO Auto-generated method stub
                        super.onError();
//              Picasso.with(getActivity()).load(imageURL.get(position)).error(R.drawable.image_not_found);
                        Iv.setImageResource(R.drawable.no_image);
                        MLog.e("ImageError", "" + "ImageError");
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishActivity();
                break;
        }
        return false;
    }

    private void finishActivity() {
        webView.stopLoading();
        webView.destroy();
        webView=null;
        finish();
        overridePendingTransition(R.anim.left_right, R.anim.right_left);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
            finishActivity();
            return true;
        }
        return false;
    }
}
