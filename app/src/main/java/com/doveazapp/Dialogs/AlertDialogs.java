package com.doveazapp.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.doveazapp.Utils.SessionManager;

/**
 * Created by Karthik on 11/24/2015.
 */
public class AlertDialogs {

    public static void openGPS(final Activity activity) {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(activity);
        alertbox.setTitle("GPS not enabled");
        alertbox.setMessage("Goto GPS settings to turn on");
        alertbox.setPositiveButton("Yes", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent to_gpssettings = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        activity.startActivity(to_gpssettings);
                    }
                });
        alertbox.setNegativeButton("No", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        alertbox.show();
    }

    public static void askforLogout(final Activity activity) {
        final AlertDialog.Builder alertbox = new AlertDialog.Builder(activity);
        alertbox.setTitle("Alert!");
        alertbox.setMessage("Do you wish to Logout?");
        alertbox.setPositiveButton("Yes", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        // Session Manager Class
                        SessionManager session;
                        activity.finish();
                        // Session class instance
                        session = new SessionManager(activity);
                        session.logoutUser();
                    }
                });
        alertbox.setNegativeButton("No", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        alertbox.show();
    }

    public static void askforExit(final Activity activity) {
        final AlertDialog.Builder alertbox = new AlertDialog.Builder(activity);
        alertbox.setTitle("Alert!");
        alertbox.setMessage("Do you wish to Exit?");
        alertbox.setPositiveButton("Yes", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        // Session Manager Class
                        /*activity.finish();
                        System.exit(0);*/
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(intent);
                        //android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
        alertbox.setNegativeButton("No", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        alertbox.show();
    }

    public static void showDialogMessage(String message) {
        Activity activity = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("PayuMoney Payment");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }
}

