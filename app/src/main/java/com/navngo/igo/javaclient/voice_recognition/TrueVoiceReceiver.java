package com.navngo.igo.javaclient.voice_recognition;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import com.mapbox.services.android.navigation.testapp.activity.navigationui.NavigationViewActivity;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

/**
 * Created by kai on 8/23/17.
 * TrueVoiceReceiver
 */

public class TrueVoiceReceiver extends BroadcastReceiver{
    private final static String TAG = "TrueVoiceReceiver";
    public static final String MBTK_STARTED = "MBTK_STARTED";
    public static final String MBTK_RESULT_ACCEPT = "MBTK_RESULT_ACCEPT";
    public static final String MBTK_RESULT_REJECT = "MBTK_RESULT_REJECT";
    public static final String MBTK_RESULT_CANCEL = "MBTK_RESULT_CANCEL";
    public static final String MBTK_REPORT_STATE = "MBTK_REPORT_STATE";
    public static final String MBTK_AUDIO_INDICATOR = "MBTK_AUDIO_INDICATOR";

    public static final int STATE_IDLE = 0;
    public static final int STATE_ACTIVATED = 1;

    private void log(String msg){
        Log.d(TAG,msg);
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        //Log.d("MGBroadcastReceiver","onReceive intent = " + intent);
        if (intent.getAction().equals(MBTK_STARTED)) {
            Toast.makeText(context, "received speech broadcast:" + intent.getAction(), Toast.LENGTH_LONG).show();
            // when VR engine has been started.
//            Intent activityIntent = new Intent(context, MainActivity.class);
//            activityIntent.addFlags (FLAG_ACTIVITY_SINGLE_TOP);
//            int assign_state = intent.getIntExtra("state", STATE_IDLE);
//            activityIntent.putExtra("state", assign_state);
//            activityIntent.setAction(MBTK_STARTED);
//            log("state :"+assign_state);
//            context.startActivity(activityIntent);
        } else if (intent.getAction().equals(MBTK_RESULT_ACCEPT)) {
            Toast.makeText(context, "received speech broadcast:" + intent.getAction(), Toast.LENGTH_LONG).show();

            //接受
            //直接去地图 做  poi 查询
            Bundle result = intent.getBundleExtra("result");
            if (result != null) {
                String keyword = "";
                String mode = result.getString("mode");
                Boolean wake_up_word = result.getBoolean("wake_up_word");
                Float confidence = result.getFloat("confidence");
                String command = result.getString("command");
                if (command == null) {
                    command = "";
                }
                //这个意味着回家 去工作
                if (command.equals("DIRECTION") || command.equals("POI_SEARCH")) {
                    if (command.equals("DIRECTION")) {
                        //target destinations
                        Bundle temp = result.getBundle("keyword");
                        String des = temp.getString("destination");
                        Toast.makeText(context, "onReceive :收到了广播 des word is=" +des, Toast.LENGTH_LONG).show();
                        Bundle result_bundle = intent.getBundleExtra("result");
//                        String poi =result_bundle.getString("keyword","Tambon Bang Pla Soi, Amphoe Mueang Chon Buri, Chang Wat Chon Buri 20000");
//                        Toast.makeText(context,"start to search  poi:"+poi,Toast.LENGTH_LONG).show();

                        Intent activityIntent = new Intent(context, NavigationViewActivity.class);
                        activityIntent.addFlags(FLAG_ACTIVITY_SINGLE_TOP);
                        activityIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        activityIntent.putExtra("destination", des);
                        activityIntent.putExtra("from","broadcast");
                        context.startActivity(activityIntent);
                    } else {
                        log("onReceive :收到了广播=" + intent.getAction() + ",but search POI not support now!");
                        Toast.makeText(context, "onReceive :收到了广播=" + intent.getAction() + ",but search POI not support now!", Toast.LENGTH_LONG).show();
                    }
                }
            }


            } else if (intent.getAction().equals(MBTK_RESULT_REJECT)) {
                // when result reject
//            Intent activityIntent = new Intent(context, MainActivity.class);
//            activityIntent.setAction(MBTK_RESULT_REJECT);
//            activityIntent.addFlags (FLAG_ACTIVITY_SINGLE_TOP);
//            activityIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//            Bundle result_bundle = intent.getBundleExtra("result");
//            activityIntent.putExtra("result", result_bundle);
//            context.startActivity(activityIntent);
            } else if (intent.getAction().equals(MBTK_RESULT_CANCEL)) {
                // when result cancelled_ DIRECTION
//            Intent activityIntent = new Intent(context, MainActivity.class);
//            activityIntent.setAction(MBTK_RESULT_CANCEL);
//            activityIntent.addFlags (FLAG_ACTIVITY_SINGLE_TOP);
//            activityIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//            Bundle result_bundle = intent.getBundleExtra("result");
//            activityIntent.putExtra("result", result_bundle);
//            context.startActivity(activityIntent);
            } else if (intent.getAction().equals(MBTK_REPORT_STATE)) {
//            Intent activityIntent = new Intent(context, MainActivity.class);
//            activityIntent.setAction(MBTK_REPORT_STATE);
//            activityIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//            activityIntent.addFlags(FLAG_ACTIVITY_SINGLE_TOP);
//            int report_state = intent.getIntExtra("state", STATE_IDLE);
//            activityIntent.putExtra("state", report_state);
//            context.startActivity(activityIntent);
            } else if (intent.getAction().equals(MBTK_AUDIO_INDICATOR)) {
            /*
            // if want to get audio indicator
            Intent activityIntent = new Intent(context, MainActivity.class);
            activityIntent.setAction(MBTK_AUDIO_INDICATOR);
            activityIntent.addFlags(FLAG_ACTIVITY_SINGLE_TOP);
            activityIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            int volume = intent.getIntExtra("volume", 0);
            activityIntent.putExtra("volume", volume);
            context.startActivity(activityIntent);
            */
            } else if (intent.getAction().equals("iGoBroadcastReceiverTest")) {

            }
    }

    public static boolean isiGoRunning(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (String activeProcess : processInfo.pkgList) {
                    Log.d("iGoTest", "isiGoRunning package " + activeProcess);
                    if (activeProcess.equals("com.nng.igo.primong")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
