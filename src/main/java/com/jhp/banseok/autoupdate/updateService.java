package com.jhp.banseok.autoupdate;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.jhp.banseok.R;
import com.jhp.banseok.bap.BapActivity;
import com.jhp.banseok.bap.tool.Preference;
import com.jhp.banseok.bap.tool.ProcessTask;
import com.jhp.banseok.bap.tool.Tools;

import java.util.Calendar;

/**
 * Created by whdghks913 on 2015-12-01.
 */
public class updateService extends Service {
    Calendar mCalendar;
    Preference mPref;
    BapDownloadTask mProcessTask;

    boolean showNotification;
    boolean onlyWIFI;

    private final int WIFI_ERROR = -1;
    private final int NET_ERROR = -2;
    private final int GET_ERROR = -3;
    private final int SUCCESS = 1;

    private final int DOWNLOAD_ID = 777;
    private final int NOTIFICATION_ID = 1004;

    @Override
    public void onCreate() {
        super.onCreate();

        mCalendar = Calendar.getInstance();
        mPref = new Preference(getApplicationContext());
        showNotification = mPref.getBoolean("updateNotifi", false);
        onlyWIFI = mPref.getBoolean("updateWiFi", true);

        if (Tools.isOnline(getApplicationContext())) {
            // 네트워크 연결됨
            if (onlyWIFI && !Tools.isWifi(getApplicationContext())) {
                // 와이파이에서만 업데이트 && 와이파이 연결안됨
                if (showNotification) {
                    // 상단바 알림
                    updateAlarm updateAlarm = new updateAlarm(this);
                    updateAlarm.wifiOFF();
                    mNotification(WIFI_ERROR);
                }
                stopSelf();
            }

            // 토요일일경우 하루를 추가해줌
            if (mCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
                mCalendar.add(Calendar.DATE, 1);

            mProcessTask = new BapDownloadTask(this);
            mProcessTask.execute(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));

        } else {
            // 네트워크 연결 안됨
            if (showNotification) {
                // 상단바 알림
                updateAlarm updateAlarm = new updateAlarm(this);
                updateAlarm.wifiOFF();
                mNotification(NET_ERROR);
            }
            stopSelf();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class BapDownloadTask extends ProcessTask {
        public BapDownloadTask(Context mContext) {
            super(mContext);
        }

        @Override
        public void onPreDownload() {
            startServiceNotification();
        }

        @Override
        public void onUpdate(int progress) {

        }

        @Override
        public void onFinish(long result) {
            NotificationManager nm = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(DOWNLOAD_ID);

            if (result == -1l) {
                // 급식 다운로드 실패
                if (showNotification)
                    mNotification(GET_ERROR);
                stopSelf();
                return;
            }

            // 급식 다운로드 성공
            if (showNotification)
                mNotification(SUCCESS);
            stopSelf();
        }
    }

    public void startServiceNotification() {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mCompatBuilder = new NotificationCompat.Builder(this);
        mCompatBuilder.setSmallIcon(R.drawable.ic_download_file);
        mCompatBuilder.setTicker(getString(R.string.bapUpdateNotification_ticker));
        mCompatBuilder.setWhen(System.currentTimeMillis());
//        mCompatBuilder.setAutoCancel(true);
        mCompatBuilder.setContentTitle(getString(R.string.bapUpdateNotification_title));
        mCompatBuilder.setContentText(getString(R.string.bapUpdateNotification_msg));
        mCompatBuilder.setContentIntent(null);
        mCompatBuilder.setOngoing(true);

        nm.notify(DOWNLOAD_ID, mCompatBuilder.build());
    }

    public void mNotification(int notificationCode) {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, new Intent(this, updateService.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mCompatBuilder = new NotificationCompat.Builder(this);
        mCompatBuilder.setSmallIcon(R.drawable.ic_download_file);
        mCompatBuilder.setTicker(getString(R.string.bapUpdateNotification_notification));
        mCompatBuilder.setWhen(System.currentTimeMillis());
        mCompatBuilder.setAutoCancel(true);

        String mTitle = "", mText = "";

        switch (notificationCode) {
            case WIFI_ERROR:
                mTitle = getString(R.string.bapUpdate_Error_Net_title);
                mText = getString(R.string.bapUpdate_Error_Net_msg);
                break;
            case NET_ERROR:
                mTitle = getString(R.string.bapUpdate_Error_Net_title);
                mText = getString(R.string.bapUpdate_Error_Net_msg);
                break;
            case GET_ERROR:
                mTitle = getString(R.string.bapUpdate_Error_get_title);
                mText = getString(R.string.bapUpdate_Error_get_msg);
                break;
            case SUCCESS:
                mTitle = getString(R.string.bapUpdate_Success_title);
                mText = getString(R.string.bapUpdate_Success_msg);
                pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, BapActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
                break;
        }

        mCompatBuilder.setContentIntent(pendingIntent);
        mCompatBuilder.setContentTitle(mTitle);
        mCompatBuilder.setContentText(mText);

        nm.notify(NOTIFICATION_ID, mCompatBuilder.build());
    }
}
