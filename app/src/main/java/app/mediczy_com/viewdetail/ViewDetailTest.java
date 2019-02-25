package app.mediczy_com.viewdetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import app.mediczy_com.R;

public class ViewDetailTest extends AppCompatActivity {

    private Fragment fragmentView;
    private FrameLayout ContactLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_detail_test);

/*        ContactLayout = (FrameLayout) findViewById(R.id.contact_new_fl_builder);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentView = new ViewDetail();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
 //           response = (BuilderOverview_Response) bundle.getSerializable("BUILDER_RESPONSE");
        }

        fragmentView.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.contact_new_fl_builder, fragmentView, "")
                .commit();*/
    }
}
