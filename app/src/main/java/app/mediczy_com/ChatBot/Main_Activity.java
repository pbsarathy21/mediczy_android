package app.mediczy_com.ChatBot;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import app.mediczy_com.Session.Session;

/**
 * Created by bala on 9/13/2018.
 */

public class Main_Activity extends AppCompatActivity {
    Session session;

    Button button1, button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.act_mai);
        session = new Session(this);

        //button1 = (Button) findViewById(R.id.button1);
      //  button2 = (Button) findViewById(R.id.button2);

        new DownloadFile().execute("http://192.168.1.15/mediczy-laravel/api/mobile/download_report/" + session.getView_id() + ".pdf", "report.pdf");





        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/Medi_Report/" + "report.pdf");  // -> filename = maven.pdf
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(Main_Activity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }


    }


    public void download(View v) {
        new DownloadFile().execute("http://192.168.1.15/mediczy-laravel/api/mobile/download_report/" + session.getView_id() + ".pdf", "report.pdf");
    }

    public void view(View v) {
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/Medi_Report/" + "report.pdf");  // -> filename = maven.pdf
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(Main_Activity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            try {
                String fileUrl = strings[0];
                //  MLog.e("splir==>", "split==>" + fileUrl.split("download_report/"));// -> http://maven.apache.org/maven-1.x/maven.pdf
                String fileName = strings[1];  // -> maven.pdf
                String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                File folder = new File(extStorageDirectory, "Medi_Report");
                folder.mkdir();

                File pdfFile = new File(folder, fileName);

                try {
                    pdfFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileDownloader.downloadFile(fileUrl, pdfFile);


            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }


            return null;
        }
    }


}