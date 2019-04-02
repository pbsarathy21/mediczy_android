package app.mediczy_com.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import app.mediczy_com.iconstant.Constant;
import app.mediczy_com.utility.MLog;

/**
 * Created by Prithivi Raj on 26-11-2015.
 */
public class AlertDialogFinish
{
    public static void Show(final Activity ctx, String alert_msg, boolean cancelStatus)
    {
        MLog.e("alert_msg", "" + alert_msg);
        MLog.e("cancelStatus", "" + cancelStatus);
        if (cancelStatus) {
            new AlertDialog.Builder(ctx)
                    .setTitle(Constant.Alart_AppName_Internet)
                    .setMessage(alert_msg)
                    .setCancelable(true)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
      //                      ctx.finish();
                        }
                    }).show();
        } else {
            new AlertDialog.Builder(ctx)
                    .setTitle(Constant.Alart_AppName_Internet)
                    .setMessage(alert_msg)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ctx.finish();
                        }
                    }).show();
        }
    }

    public static void Update(final Activity ctx, String alert_msg, boolean cancelStatus)
    {
        MLog.e("alert_msg", "" + alert_msg);
        MLog.e("cancelStatus", "" + cancelStatus);
        new AlertDialog.Builder(ctx)
                .setTitle(Constant.Alert_Update_ReqTittle)
                .setMessage(alert_msg)
                .setCancelable(true)
                .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final String appPackageName = ctx.getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                }).show();
    }
}
