package com.example.xinan.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.xinan.chooseFragment;
import com.example.xinan.util.HttpUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UpdateService extends Service {
    chooseFragment chooseFragment;
    public UpdateService() {
        chooseFragment = new chooseFragment();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateMain();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int tenSeconds = 10 * 1000; // 这是8小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + tenSeconds;
        Intent i = new Intent(this, UpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    public void updateMain(){
//        Message msg = Message.obtain();
//        msg.what = 1;
//        Handler handler = chooseFragment.getHandler();
//        if(HttpUtil.getToken() != null)  handler.sendMessage(msg);
        Log.d("TAG","TEST");
    }
}
