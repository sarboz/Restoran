package com.restoran;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.news.restoran.R;
import com.restoran.Chat.Client;
import com.restoran.Interfase.Api;
import com.restoran.Interfase.IResive;
import com.restoran.Models.ZalStol;
import com.restoran.adapter.ZalAdapter;
import com.restoran.adapter.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<ZalStol>, IResive {

    private ZalAdapter mAdapter;
    private TabLayout tab;
    private ViewPager mViewPager;
    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tab = (TabLayout) findViewById(R.id.sliding_tabs);
        mViewPager = (ViewPager) findViewById(R.id.container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);

        setTitle(pref.getString("fio", "Null"));

        id = pref.getString("id","");
        Api api = RetrofitClient.getApiWithCache(this);
        Call<ZalStol> call = api.getZals();
        call.enqueue(this);

        Client myClient = new Client("192.168.1.3", 8888, this);
        myClient.execute();
    }

    List<ZalStol.Zal> zals;

    @Override
    public void onResponse(Call<ZalStol> call, Response<ZalStol> response) {
        this.zals = response.body().getZals();
        fillData(zals);
    }

    @Override
    public void onFailure(Call<ZalStol> call, Throwable t) {

    }

    public void fillData(List<ZalStol.Zal> zals) {

        int selected = tab.getSelectedTabPosition();
        mAdapter = new ZalAdapter(getSupportFragmentManager(), zals,id);
        tab.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(selected);
    }

    // метод барои коркарди запроси аз tcpServer меомада хизмат мекунад
    @Override
    public void OnResive(String msg) {
        for (ZalStol.Zal z : zals) {
            List<ZalStol.Stol> stols = z.getStol();
            for (ZalStol.Stol s : stols) {
                if (s.getId().equals("44")) {
                    s.setName(msg);
                }
            }
        }
        fillData(zals);
    }

    @Override
    public void OnError(String msg) {

    }
}
