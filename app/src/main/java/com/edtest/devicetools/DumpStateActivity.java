package com.edtest.devicetools;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DumpStateActivity extends AppCompatActivity {
    public static final String TAG = "DEVICE_TOOLS";
    public static final String TAG2 = "DUMP_STATE_ACTIVITY: ";

    Button lowButton, midButton, highButton, logButton;
    RecyclerView logRecyclerView;

    ArrayList<String> values;
    private DumpLogRecyclerViewAdapter adapter;

    private static int LOG_LOW = 0;
    private static int LOG_MID = 1;
    private static int LOG_HIGH = 2;
    int logLevel;
    int logButtonPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dumpstate);

        //TODO load shared prefs logging level currently set to
        logLevel = LOG_LOW;
        logButtonPressed = LOG_LOW;

        lowButton = findViewById(R.id.lowButton);
        midButton = findViewById(R.id.midButton);
        highButton = findViewById(R.id.highButton);
        logButton = findViewById(R.id.logButton);

/*
        new AlertDialog.Builder(this)
                .setTitle("Reboot Required")
                .setMessage("Changing Log Level requires a reboot, are you sure?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                    }
                })
                .setNegativeButton("NO", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


        LayoutInflater inflater = getLayoutInflater();
        AlertDialog.Builder rebootBuilder = new AlertDialog.Builder(this);
        //rebootBuilder.setTitle("Restart Required");
        //rebootBuilder.setMessage("Changing logging level requires a restart - please confirm:");
        rebootBuilder.setView(inflater.inflate(R.layout.alert_log_restart_layout, null));
        rebootBuilder.setCancelable(true);
        rebootBuilder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.w(TAG, TAG2 + "POSITION:" + position + " ID:" + id );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rebootBuilder.setPositiveButton(
                "Restart",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logLevelToggle(logButtonPressed);
                        dialog.cancel();
                    }
                });

        rebootBuilder.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog rebootAlert = rebootBuilder.create();
*/

        LogRestartDialog alert = new LogRestartDialog();
        Activity activity = this;

        lowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.showDialog(activity, LOG_LOW);
            }
        });

        midButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.showDialog(activity, LOG_MID);
            }
        });

        highButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.showDialog(activity, LOG_HIGH);
            }
        });

        GenerateLogDialog generateLogDialog = new GenerateLogDialog();

        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateLogDialog.showDialog(activity);
            }
        });

        //setup recycler view i'll show all data in here
        logRecyclerView = findViewById(R.id.logListRecyclerView);

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        logRecyclerView.setLayoutManager(verticalLayoutManager);

        values = new ArrayList<>();

        adapter = new DumpLogRecyclerViewAdapter(this, values);
        logRecyclerView.setAdapter(adapter);

    }

    private void logLevelToggle(int l) {
        if (logLevel != l) {
            //alert reboot
            logLevel = l; //TODO save this to shared prefs
            switch (l) {
                case 0:
                    lowButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_active_layer_list));
                    midButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_inactive_layer_list));
                    highButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_inactive_layer_list));
                    break;
                case 1:
                    lowButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_inactive_layer_list));
                    midButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_active_layer_list));
                    highButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_inactive_layer_list));
                    break;
                case 2:
                    lowButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_inactive_layer_list));
                    midButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_inactive_layer_list));
                    highButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_active_layer_list));
                    break;
                default:
                    break;
            }
        }
    }

    private void addLogItem() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateTs = dateFormatter.format(date);
        DateFormat timeFormatter = new SimpleDateFormat("hh-mm-ss");
        String timeTs = timeFormatter.format(date);
        String logTitle = "LOG-" + dateTs + "_" + timeTs + ".txt";
        values.add(0,logTitle);
        adapter.notifyDataSetChanged();
    }

    public class LogRestartDialog {

        public void showDialog(Activity activity, int logLevel){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_log_restart_layout);

            Button confirmButton = (Button) dialog.findViewById(R.id.logRestartConfirmButton);
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logLevelToggle(logLevel);
                    dialog.dismiss();
                }
            });

            Button cancelButton = (Button) dialog.findViewById(R.id.logRestartCancelButton);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();

        }
    }

    public class GenerateLogDialog {

        public void showDialog(Activity activity){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.log_generate_layout);

            dialog.show();

            Handler logHandler = new Handler(Looper.getMainLooper());
            logHandler.postDelayed(() -> {
                addLogItem();
                dialog.dismiss();
            }, 3000);
        }
    }

}
