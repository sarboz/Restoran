package com.restoran;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.news.restoran.R;
import com.restoran.Chat.Client;
import com.restoran.Interfase.Api;
import com.restoran.Interfase.IResive;
import com.restoran.Interfase.Istol;
import com.restoran.Models.ZalStol;
import com.restoran.adapter.RetrofitClient;
import com.restoran.adapter.ZalAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<ZalStol>, IResive {

    private ZalAdapter mAdapter;
    private TabLayout tab;
    private ViewPager mViewPager;
    TextView statusBar;
    String id = "";
    String ip = "192.168.1.10";
    public static Istol istol;
    Api api;
    Call<ZalStol> call;
    Client myClient = null;
    List<ZalStol.Zal> zals = new ArrayList<>();

    public static boolean conStatus = false;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tab = (TabLayout) findViewById(R.id.sliding_tabs);
        mViewPager = (ViewPager) findViewById(R.id.container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        statusBar = (TextView) findViewById(R.id.statusBar);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        setTitle(pref.getString("fio", "Null"));

        id = pref.getString("id", "");
        api = RetrofitClient.getApi();
        call = api.getZals();
        call.enqueue(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        myClient = Client.getInstance(ip, 8888, this);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }


    @Override
    public void onResponse(Call<ZalStol> call, Response<ZalStol> response) {
        this.zals = response.body().getZals();
        fillData(zals);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onFailure(Call<ZalStol> call, Throwable t) {
    }

    public void fillData(List<ZalStol.Zal> zals) {
        mAdapter = new ZalAdapter(getSupportFragmentManager(), zals, id);
        tab.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(mAdapter);
    }

    // метод барои коркарди запроси аз tcpServer меомада хизмат мекунад
    @Override
    public void OnResive(String msg) {
        if (msg.equals("update")) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            recreate();
        }else {
            String [] s=msg.split(":");
            if(pref.getString("id","0").equals(s[0])){
                showNotify(s[1],s[2]);
            }
        }
    }

    private long[] pattern = {0, 400, 800, 600, 400, 800, 100, 500};
    private Vibrator v = null;

    private void showNotify(String tit,String content) {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(tit)
                        .setContentText(content)
                        .setSound(Uri.parse(pref.getString("ringtone", "")))
                        .setChannelId("Notifi");

        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel("Notifi", getResources().getString(R.string.app_name), importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(1996, notification);

        if(pref.getBoolean("vib",false)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                v.vibrate(VibrationEffect.createWaveform(pattern, VibrationEffect.DEFAULT_AMPLITUDE));
            else {
                v.vibrate(pattern, -1);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(getApplication(), SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnError(String msg) {
        conStatus = false;
        statusBar.setText("Нет подключение");
        statusBar.setBackgroundResource(R.color.colorAccent);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Client.getInstance(ip, 8888, MainActivity.this);
            }
        }, 3000);
    }

    @Override
    public void OnDisconnected() {
        conStatus = false;
        statusBar.setText("Нет подключение");
        statusBar.setBackgroundResource(R.color.colorAccent);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Client.getInstance(ip, 8888, MainActivity.this);
            }
        }, 3000);

    }

    @Override
    public void OnConnected() {
        conStatus = true;
        statusBar.setText("Подключено");
        statusBar.setBackgroundResource(R.color.colorStatus);
    }
}
